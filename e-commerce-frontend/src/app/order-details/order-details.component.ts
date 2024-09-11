import { Component } from '@angular/core';
import { MyOrderDetails } from '../_model/order.model';
import { ProductService } from '../_services/product.service';
import { error } from 'console';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrl: './order-details.component.css'
})
export class OrderDetailsComponent {
  constructor(private productService : ProductService){ }

  ngOnInit(): void {
    this.getOrderDetails();
  }

  displayedColumns=["Id","ProductName","Name", "Address", "Contact No.", "Price", "Status","ChangeStatus"];
  myOrderDetails: MyOrderDetails[] = [];

  //get seller's orders
  getOrderDetails(){
    this.productService.getAllOrders().subscribe({
      next: (data : MyOrderDetails[]) => {
        console.log(data);
        this.myOrderDetails = data;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  //mark an order as delivered
  markAsDelivered(element : any){
    this.productService.markDelivered(element).subscribe({
      next: (data) => {
        this.getOrderDetails();
      },
      error: (error) => {

      }
    });
  }
}
