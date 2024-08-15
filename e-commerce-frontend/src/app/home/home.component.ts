import { Component, OnInit } from '@angular/core';
import { ProductService } from '../_services/product.service';
import { Product } from '../_model/product.model';
import { map } from 'rxjs';
import { ImageProcessingService } from '../image-processing.service';
import { Router } from '@angular/router';
import { Category } from '../_model/category.model';
import { response } from 'express';
import { error } from 'console';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];

  constructor(private productService: ProductService, private imageProcessingService : ImageProcessingService, private router: Router) { }

  //fetch all prroducts and categories.
  ngOnInit(): void {
    this.getAllProducts();
    this.getAllCategories();
  }

  //get all categories
  getAllCategories() {
    this.productService.getCategories().subscribe({
      next:(response : any)=> {
        this.categories = response;
      },
      error:(error : any) => {
        console.log(error);
      }
    })
  }

  //navigate to the product details page for the selected product
  showProductDetails(productId: number) {
    this.router.navigate(['/productViewDetails',{productId: productId}]);
  }

  //get all products
  public getAllProducts() {
    this.productService.getAllProducts().pipe(map((x : Product[], index) => x.map((product: Product) => this.imageProcessingService.createImages(product)))).subscribe(
      (data) => {
        this.products = data;
        console.log(data);
      },
      (error) => {
        console.log(error)
      });
  }

}
