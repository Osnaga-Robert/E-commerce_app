package com.example.shop4All_backend.services;

import com.example.shop4All_backend.configurations.JwtRequestFilter;
import com.example.shop4All_backend.entities.OrderDetails;
import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.entities.User;
import com.example.shop4All_backend.repositories.OrderDetailsRepo;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final UserRepo userRepo;
    private final OrderDetailsRepo orderDetailsRepo;
    private final ProductRepo productRepo;
    private final OrderDetailsService orderDetailsService;

    //get the information for the main charts
    public HashMap<String, Object> getMainCharts() {
        HashMap<String, Object> mainCharts = new HashMap<>();
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepo.findByUserEmail(currentUser).get();
        List<OrderDetails> orders = orderDetailsService.getAllOrdersSeller();
        List<Product> products = productRepo.findByCompanySeller(user.getUserCompanyName());
        List<OrderDetails> deliveredOrders = orders.stream()
                .filter(order -> order.getOrderStatus().equals("Delivered"))
                .collect(Collectors.toList());
        List<OrderDetails> thisWeekOrders = getDeliveredOrdersThisWeek(deliveredOrders);
        Double totalSales = deliveredOrders.stream().mapToDouble(OrderDetails::getOrderAmount).sum();
        Integer views = products.stream().mapToInt(Product::getViews).sum();
        Double salesWeek = thisWeekOrders.stream().mapToDouble(OrderDetails::getOrderAmount).sum();
        HashMap<String, Integer> monthlySales = getMonthlySales(deliveredOrders);
        HashMap<String, Integer> topProductsSold = getTopProducts("year");
        mainCharts.put("totalSales", totalSales);
        mainCharts.put("views", views);
        mainCharts.put("salesWeek", salesWeek);
        mainCharts.put("monthlySales", monthlySales);
        mainCharts.put("topProductsSold", topProductsSold);
        return mainCharts;
    }

    //get the orders that were delivered this week
    public List<OrderDetails> getDeliveredOrdersThisWeek(List<OrderDetails> deliveredOrders) {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return deliveredOrders.stream()
                .filter(order -> {
                    LocalDate orderDate = order.getOrderDate();
                    return (orderDate.isEqual(startOfWeek) || orderDate.isAfter(startOfWeek)) &&
                            (orderDate.isEqual(endOfWeek) || orderDate.isBefore(endOfWeek));
                })
                .collect(Collectors.toList());
    }

    //get the sales from this year
    public LinkedHashMap<String, Integer> getMonthlySales(List<OrderDetails> deliveredOrders) {
        LinkedHashMap<String, Integer> monthlySales = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();

        List<OrderDetails> currentYearOrders = deliveredOrders.stream()
                .filter(order -> order.getOrderDate().getYear() == now.getYear())
                .collect(Collectors.toList());

        for (int month = 1; month <= currentMonth; month++) {
            String monthName = Month.of(month).name();
            int finalMonth = month;
            int totalSalesForMonth = currentYearOrders.stream()
                    .filter(order -> order.getOrderDate().getMonthValue() == finalMonth)
                    .mapToInt(OrderDetails::getOrderQuantity)
                    .sum();
            monthlySales.put(monthName, totalSalesForMonth);
        }

        return monthlySales;
    }

    //get the best product sold based on a period
    public HashMap<String, Integer> getTopProducts(String period) {
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepo.findByUserEmail(currentUser).get();
        List<Product> products = productRepo.findByCompanySeller(user.getUserCompanyName());
        List<String> periodDates = getPeriodDates(period);
        HashMap<String, Integer> topProducts = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Product product : products) {
            List<OrderDetails> productOrders = orderDetailsRepo.findByProduct(product);
            List<OrderDetails> allOrdersSeller = productOrders.stream()
                    .filter(orderDetails -> orderDetails.getProduct().getCompanySeller().equals(user.getUserCompanyName()))
                    .filter(orderDetails -> orderDetails.getOrderStatus().equals("Delivered"))
                    .filter(orderDetails -> isWithinPeriod(orderDetails.getOrderDate(), period, periodDates, formatter))
                    .collect(Collectors.toList());

            Integer sumProductsSold = allOrdersSeller.stream()
                    .mapToInt(OrderDetails::getOrderQuantity)
                    .sum();

            topProducts.put(product.getProductName(), sumProductsSold);
        }

        return topProducts.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    //filter based on period
    private boolean isWithinPeriod(LocalDate orderDate, String period, List<String> periodDates, DateTimeFormatter formatter) {
        switch (period) {
            case "week":
                for (String day : periodDates) {
                    LocalDate periodDate = LocalDate.parse(day, formatter);
                    if (orderDate.equals(periodDate)) {
                        return true;
                    }
                }
                break;

            case "month":
                for (String weekRange : periodDates) {
                    String[] days = weekRange.split("-");
                    int startDay = Integer.parseInt(days[0]);
                    int endDay = Integer.parseInt(days[1]);
                    LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), startDay);
                    LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), endDay);
                    if (!orderDate.isBefore(startDate) && !orderDate.isAfter(endDate)) {
                        return true;
                    }
                }
                break;

            case "year":
                for (String monthName : periodDates) {
                    Month month = Month.valueOf(monthName.toUpperCase());
                    if (orderDate.getMonth() == month) {
                        return true;
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
        return false;
    }


    //get total Sales based on a period
    public LinkedHashMap<String, Object> getTotalSales(String period) {
        List<String> getPeriods = getPeriodDates(period);
        LinkedHashMap<String, Object> totalSales = new LinkedHashMap<>();
        List<OrderDetails> orders = orderDetailsService.getAllOrdersSeller();
        List<OrderDetails> deliveredOrders = orders.stream()
                .filter(order -> order.getOrderStatus().equals("Delivered"))
                .collect(Collectors.toList());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        switch (period) {
            case "week":
                for (String day : getPeriods) {
                    LocalDate periodDate = LocalDate.parse(day, formatter);
                    double salesForDay = deliveredOrders.stream()
                            .filter(order -> order.getOrderDate().equals(periodDate))
                            .mapToDouble(OrderDetails::getOrderQuantity)
                            .sum();
                    totalSales.put(day, salesForDay);
                }
                break;

            case "month":
                for (String weekRange : getPeriods) {
                    String[] days = weekRange.split("-");
                    int startDay = Integer.parseInt(days[0]);
                    int endDay = Integer.parseInt(days[1]);
                    LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), startDay);
                    LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), endDay);
                    double salesForWeek = deliveredOrders.stream()
                            .filter(order -> !order.getOrderDate().isBefore(startDate) &&
                                    !order.getOrderDate().isAfter(endDate))
                            .mapToDouble(OrderDetails::getOrderQuantity)
                            .sum();
                    totalSales.put(weekRange, salesForWeek);
                }
                break;

            case "year":
                for (String monthName : getPeriods) {
                    Month month = Month.valueOf(monthName.toUpperCase());
                    double salesForMonth = deliveredOrders.stream()
                            .filter(order -> order.getOrderDate().getMonth() == month)
                            .mapToDouble(OrderDetails::getOrderQuantity)
                            .sum();
                    totalSales.put(monthName, salesForMonth);
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
        return totalSales;
    }

    // get total visitors
    public HashMap<String, Integer> getTotalVisitors() {
        HashMap<String, Integer> totalVisitors = new HashMap<>();
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepo.findByUserEmail(currentUser).get();
        List<Product> products = productRepo.findByCompanySeller(user.getUserCompanyName());
        for (Product product : products) {
            totalVisitors.put(product.getProductName(), product.getViews());
        }
        return totalVisitors.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    //get total orders based on a period
    public LinkedHashMap<String, Integer> getTotalOrders(String period) {
        List<String> getPeriods = getPeriodDates(period);
        LinkedHashMap<String, Integer> totalOrders = new LinkedHashMap<>();
        List<OrderDetails> orders = orderDetailsService.getAllOrdersSeller();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        switch (period) {
            case "week":
                for (String day : getPeriods) {
                    LocalDate periodDate = LocalDate.parse(day, formatter);
                    int total = orders.stream()
                            .filter(order -> order.getOrderDate().equals(periodDate))
                            .mapToInt(OrderDetails::getOrderQuantity)
                            .sum();
                    totalOrders.put(day, total);
                }
                break;

            case "month":
                for (String weekRange : getPeriods) {
                    String[] days = weekRange.split("-");
                    int startDay = Integer.parseInt(days[0]);
                    int endDay = Integer.parseInt(days[1]);
                    LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), startDay);
                    LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), endDay);
                    int salesForWeek = orders.stream()
                            .filter(order -> !order.getOrderDate().isBefore(startDate) &&
                                    !order.getOrderDate().isAfter(endDate))
                            .mapToInt(OrderDetails::getOrderQuantity)
                            .sum();
                    totalOrders.put(weekRange, salesForWeek);
                }
                break;

            case "year":
                for (String monthName : getPeriods) {
                    Month month = Month.valueOf(monthName.toUpperCase());
                    int salesForMonth = orders.stream()
                            .filter(order -> order.getOrderDate().getMonth() == month)
                            .mapToInt(OrderDetails::getOrderQuantity)
                            .sum();
                    totalOrders.put(monthName, salesForMonth);
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
        return totalOrders;
    }

    //get the months, days or interval of days based on period
    public List<String> getPeriodDates(String period) {
        LocalDate today = LocalDate.now();
        List<String> result = new ArrayList<>();

        switch (period) {
            case "week":
                DayOfWeek currentDayOfWeek = today.getDayOfWeek();
                LocalDate startOfWeek = today.minusDays(currentDayOfWeek.getValue() - 1);
                for (LocalDate date = startOfWeek; !date.isAfter(today); date = date.plusDays(1)) {
                    result.add(date.toString());
                }
                break;

            case "month":
                int currentDayOfMonth = today.getDayOfMonth();
                String[] weekRanges = {"1-7", "8-14", "15-21", "22-28", "29-31"};
                for (int i = 0; i < weekRanges.length; i++) {
                    int start = i * 7 + 1;
                    int end = Math.min((i + 1) * 7, today.lengthOfMonth());
                    if (start <= currentDayOfMonth) {
                        result.add(start + "-" + end);
                    } else {
                        break;
                    }
                }
                break;
            case "year":
                Month currentMonth = today.getMonth();
                for (Month month : Month.values()) {
                    if (month.getValue() <= currentMonth.getValue()) {
                        result.add(month.name());
                    } else {
                        break;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
        return result;
    }
}
