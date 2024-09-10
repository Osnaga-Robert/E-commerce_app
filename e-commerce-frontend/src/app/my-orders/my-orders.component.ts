import { Component, OnInit } from '@angular/core';
import { ProductService } from '../_services/product.service';
import { MyOrderDetails } from '../_model/order.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-my-orders',
  templateUrl: './my-orders.component.html',
  styleUrls: ['./my-orders.component.css']
})
export class MyOrdersComponent implements OnInit {

  displayedColumns = ["Name", "Address", "Contact No.", "Price", "Status", "AddReview"];
  myOrderDetails: MyOrderDetails[] = [];

  constructor(private productService: ProductService, private router: Router) { }

  ngOnInit(): void {
    this.getOrderDetails();
  }

  getOrderDetails() {
    this.productService.getMyOrders().subscribe({
      next: (data: MyOrderDetails[]) => {
        this.myOrderDetails = data;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  addReview(order: any) {
    // Logic for adding a review goes here
    console.log('Add review for:', order.product);
    this.router.navigate(['/addReview',{ productId: order.product.productId }]);
  }
}
