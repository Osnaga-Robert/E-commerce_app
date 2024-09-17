import { Component, OnInit } from '@angular/core';
import { Chart, ChartConfiguration, ChartType, registerables } from 'chart.js'; 
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-seller',
  templateUrl: './seller.component.html',
  styleUrls: ['./seller.component.css']
})
export class SellerComponent implements OnInit {

  monthlySales = {};
  public salesLabels: any = null;
  public barChartType: ChartType = 'bar';
  totalSales: number = 0;
  totalCustomers: number = 0; 
  salesWeek: number = 0;
  totalViewsWeek: number = 0;
  topProducts: any = null;

  public barChartData: ChartConfiguration['data'] | undefined;
  public barChartOptions: ChartConfiguration['options'];

  constructor(private userService : UserService) {
    Chart.register(...registerables);
  }
  
  ngOnInit(): void {
    this.userService.mainChart().subscribe({
      next: (next: any) => {
        console.log(next);
        this.monthlySales = next.monthlySales;
        this.salesWeek = next.salesWeek;
        this.totalSales = next.totalSales;
        this.totalViewsWeek = next.views;
        this.topProducts = Object.entries(next.topProductsSold).map(([name, sold]) => ({
          name,
          sold
        }));

        this.salesLabels = Object.keys(this.monthlySales);
        this.updateChartData();
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  updateChartData(): void {
    this.barChartData = {
      labels: this.salesLabels,
      datasets: [
        {
          data: Object.values(this.monthlySales),
          label: 'Monthly Sales',
          backgroundColor: '#42A5F5',
          borderColor: '#1E88E5',
          borderWidth: 1
        }
      ]
    };
    this.barChartOptions ={
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
