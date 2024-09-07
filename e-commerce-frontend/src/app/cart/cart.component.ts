import { Component, OnInit } from '@angular/core';
import { ProductService } from '../_services/product.service';
import { error } from 'console';
import { response } from 'express';
import { Router } from '@angular/router';
import { Product } from '../_model/product.model';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit {

  displayedColumns: string[] = ['Name', 'Price', 'DiscountedPrice', 'Action'];
  cartDetails = [];

  constructor(private productService: ProductService, private router: Router) { }

  //initialize cart details
  ngOnInit(): void {
    this.getCartDetails();
  }

  //get cart details
  getCartDetails() {
    this.productService.getCart().subscribe({
      next: (response: any) => {
        console.log(response);
        this.cartDetails = response;
        console.log(this.cartDetails);
      },
      error: (error: any) => {
        console.log(error);
      }
    })
  }

  //buy the products
  checkout() {
    this.router.navigate(['/buyProduct', {
      isSingleProductCheckout: false, id: 0
    }]);
  }

  //delete a product from a cart
  deleteProduct(cart: any) {
    this.productService.deleteCart(cart.id).subscribe({
      next: (elem: any) => {
        console.log("Deleted Item");
        console.log(elem);
        this.getCartDetails();
      },
      error: (error: any) => {
        console.log("Error");
        console.log(error);
      }
    });
  }
}
