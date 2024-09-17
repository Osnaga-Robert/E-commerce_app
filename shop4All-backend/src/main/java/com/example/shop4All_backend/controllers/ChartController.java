package com.example.shop4All_backend.controllers;

import com.example.shop4All_backend.services.ChartService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ChartController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final ChartService chartService;

    // Handle GET request to seller/chart/main to get the main page with statistics
    @GetMapping("seller/chart/main")
    public ResponseEntity<HashMap<String, Object>> getMainCharts() {
        logger.info("Get main charts");
        return new ResponseEntity<>(chartService.getMainCharts(), HttpStatus.OK);
    }

    // Handle GET request to seller/chart/totalSales/{period} to get totalSales from a period
    @GetMapping("seller/chart/totalSales/{period}")
    public ResponseEntity<HashMap<String, Object>> getTotalSales(@PathVariable("period") String period) {
        logger.info("Get total sales from this " + period);
        return new ResponseEntity<>(chartService.getTotalSales(period), HttpStatus.OK);
    }

    // Handle GET request to seller/chart/totalVisitors to get totalVisitors
    @GetMapping("seller/chart/totalVisitors")
    public ResponseEntity<HashMap<String, Integer>> geTotalVisitors() {
        logger.info("Get total visitors");
        return new ResponseEntity<>(chartService.getTotalVisitors(), HttpStatus.OK);
    }

    // Handle GET request to seller/chart/totalOrders/{period} to get all orders form a period
    @GetMapping("seller/chart/totalOrders/{period}")
    public ResponseEntity<HashMap<String, Integer>> getTotalOrder(@PathVariable("period") String period){
        logger.info("Get total orders from this " + period);
        return new ResponseEntity<>(chartService.getTotalOrders(period), HttpStatus.OK);
    }

    // Handle GET request to seller/chart/topProducts/{period} to get the best-selling products from a period
    @GetMapping("seller/chart/topProducts/{period}")
    public ResponseEntity<Map<String, Integer>> getTopProducts(@PathVariable("period") String period){
        logger.info("Get top products from this " + period);
        return new ResponseEntity<>(chartService.getTopProducts(period), HttpStatus.OK);
    }


}
