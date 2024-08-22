import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { OrderDetails } from '../_model/order-details.model';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { Product } from '../_model/product.model';
import { ProductService } from '../_services/product.service';

@Component({
  selector: 'app-buy-product',
  templateUrl: './buy-product.component.html',
  styleUrl: './buy-product.component.css'
})
export class BuyProductComponent implements OnInit {

  isSingleProductCheckout: string = '';
  productDetails: Product[] = [];
  orderDetails: OrderDetails = {
    fullName: '',
    fullAddress: '',
    contactNumber: '',
    products: []
  }

  constructor(private activatedRoute: ActivatedRoute, private productService: ProductService, private router: Router) { }

  //initialize each product with a quantity
  ngOnInit(): void {
    this.productDetails = this.activatedRoute.snapshot.data['productDetails'];
    this.isSingleProductCheckout = this.activatedRoute.snapshot.paramMap.get('isSingleProductCheckout') || '';
    this.productDetails.forEach(
      x => this.orderDetails.products.push(
        { productId: x.productId, quantity: 1 }
      )
    )
    console.log("Order details:");
    console.log(this.orderDetails);
  }

  //place the order based on type (one or multiple products)
  placeOrder(orderForm: NgForm) {
    this.productService.placeOrder(this.orderDetails, this.isSingleProductCheckout).subscribe({
      next: (response: any) => {
        console.log("Order placed:");
        console.log(response);
        orderForm.reset();
        this.router.navigate(['/orderConfirm']);
      },
      error(error: any) {
        console.log("Error:");
        console.log(error);
      }
    })
  }

  //get the modified quantity for each product
  getQuantityForProduct(productId: number) {
    const myProduct = this.orderDetails.products.filter(
      (productQuantity) => productQuantity.productId === productId
    );
    return myProduct[0].quantity
  }

  //get total price of a product
  getTotal(productId: number, productPrice: number, productDiscounted: number) {
    return (this.getQuantityForProduct(productId) * (productPrice - productPrice * (productDiscounted / 100))).toFixed(2);
  }

  //modify quantity
  onQuantityChanged(quantity: any, productId: number) {
    const myProduct = this.orderDetails.products.filter(
      (productQuantity) => productQuantity.productId === productId
    );
    myProduct[0].quantity = quantity;
  }

  //get total price of a cart
  getTotalAmount() {
    let total = 0;
    this.orderDetails.products.forEach(
      (productTotal) => {
        const getProduct = this.productDetails.filter(product => product.productId === productTotal.productId)
        const price = getProduct[0].productPrice * (1 - getProduct[0].productDiscounted / 100);
        total += price * productTotal.quantity;
      }
    )
    return total.toFixed(2);
  }
}
