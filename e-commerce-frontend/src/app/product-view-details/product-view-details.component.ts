import { Component, OnInit } from '@angular/core';
import { Product } from '../_model/product.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-product-view-details',
  templateUrl: './product-view-details.component.html',
  styleUrls: ['./product-view-details.component.css']
})
export class ProductViewDetailsComponent implements OnInit {

  descriptionOpen = false;
  reviewOpen = false;
  product: Product | any = null;
  currentIndex = 0;

  constructor(private activatedRoute: ActivatedRoute) { }

  //get the product details
  ngOnInit(): void {
    this.product = this.activatedRoute.snapshot.data['product'];
    if (this.product) {
      console.log('Product details:', this.product);
      if (this.product.productImages.length) {
        this.currentIndex = 0;
        console.log('Initial image index set to:', this.currentIndex);
      }
    } else {
      console.log('No product data found');
    }
  }

  //display the previous image
  previousImage() {
    if (this.product && this.product.productImages.length > 0) {
      this.currentIndex = (this.currentIndex - 1 + this.product.productImages.length) % this.product.productImages.length;
    }
  }

  //display the next image
  nextImage() {
    if (this.product && this.product.productImages.length > 0) {
      this.currentIndex = (this.currentIndex + 1) % this.product.productImages.length;
    }
  }

  //get the description of the product
  toggleDescription() {
    this.descriptionOpen = !this.descriptionOpen;
    console.log('Description panel toggled, now open:', this.descriptionOpen);
  }

  //get the reviews of the product
  toggleReview() {
    this.reviewOpen = !this.reviewOpen;
    console.log('Review panel toggled, now open:', this.reviewOpen);
  }
}
