import { Component } from '@angular/core';
import { UserService } from '../_services/user.service';
import { Chart, registerables } from 'chart.js';


@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html',
  styleUrl: './charts.component.css'
})
export class ChartsComponent {
  public totalSalesData: any = {};
  public topVisitorsData: any = {};
  public totalOrdersData: any = {};
  public chartOptions: any;
  public selectedTotalSalesPeriod: string = 'month';
  public selectedTotalOrdersPeriod: string = 'month';
  public selectedTopProductsPeriod: string = 'month';

  public topProducts: any = null;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    Chart.register(...registerables);
    this.chartOptions = this.getChartOptions();
    this.fetchData();
  }

  fetchData(): void {
    this.getTotalSalesData(this.selectedTotalSalesPeriod);
    this.getTotalOrdersData(this.selectedTotalOrdersPeriod);
    this.getTopVisitorsData();
    this.getTopProducts(this.selectedTopProductsPeriod);
  }

  getTotalSalesData(period: string): void {
    this.userService.getTotalSales(period).subscribe(data => {
      this.totalSalesData = {
        labels: Object.keys(data),
        datasets: [
          {
            label: `Total Sales (${period})`,
            data: Object.values(data),
            backgroundColor: 'rgba(54, 162, 235, 0.6)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
          }
        ]
      };
    });
  }

  getTotalOrdersData(period: string): void {
    this.userService.getTotalOrder(period).subscribe(data => {
      this.totalOrdersData = {
        labels: Object.keys(data),
        datasets: [
          {
            label: `Total Orders (${period})`,
            data: Object.values(data),
            backgroundColor: 'rgba(75, 192, 192, 0.6)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
          }
        ]
      };
    });
  }

  getTopVisitorsData(): void {
    this.userService.getTotalVisitors().subscribe(data => {
      const topVisitors = Object.entries(data).sort((a: any, b: any) => b[1] - a[1]).slice(0, 10);
      this.topVisitorsData = {
        labels: topVisitors.map(([name]) => name),
        datasets: [
          {
            label: 'Top 10 Visitors',
            data: topVisitors.map(([, visitors]) => visitors),
            backgroundColor: 'rgba(255, 99, 132, 0.6)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1
          }
        ]
      };
    });
  }

  getTopProducts(period: string): void {
    this.userService.getTopProducts(period).subscribe(data => {
      this.topProducts = Object.entries(data).map(([name, sold]) => ({
        name,
        sold
      }));
    });
  }

  onTotalSalesPeriodChange(event: Event): void {
    this.selectedTotalSalesPeriod = (event.target as HTMLSelectElement).value;
    this.getTotalSalesData(this.selectedTotalSalesPeriod);
  }

  onTopVisitorsPeriodChange(event: Event): void {
    this.getTopVisitorsData();
  }

  onTotalOrdersPeriodChange(event: Event): void {
    this.selectedTotalOrdersPeriod = (event.target as HTMLSelectElement).value;
    this.getTotalOrdersData(this.selectedTotalOrdersPeriod);
  }

  onTopProductsPeriodChange(event: Event): void {
    this.selectedTopProductsPeriod = (event.target as HTMLSelectElement).value;
    this.getTopProducts(this.selectedTopProductsPeriod);
  }

  getChartOptions(): any {
    return {
      responsive: true,
      scales: {
        x: {
          grid: {
            display: false
          }
        },
        y: {
          beginAtZero: true,
          grid: {
            color: '#e0e0e0'
          }
        }
      },
      plugins: {
        legend: {
          display: true,
          position: 'top'
        }
      }
    };
  }
}
