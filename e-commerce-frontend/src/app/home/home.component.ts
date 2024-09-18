import { Component, OnInit } from '@angular/core';
import { ProductService } from '../_services/product.service';
import { Product } from '../_model/product.model';
import { map } from 'rxjs/operators';
import { ImageProcessingService } from '../image-processing.service';
import { Router } from '@angular/router';
import { Category } from '../_model/category.model';
import { response } from 'express';
import { error } from 'console';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  products: Product[] = [];
  categories: Category[] = [];
  pageNumber: number = 0;
  showLoadButton = true;
  selectedCategory: string | null = null;

  constructor(
    private productService: ProductService,
    private imageProcessingService: ImageProcessingService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.pageNumber = 0;
    console.log('HomeComponent initialized');
    this.getAllProducts();
    this.getAllCategories();
  }

  //get all categories
  getAllCategories() {
    console.log('Fetching categories');
    this.productService.getCategories().subscribe({
      next: (response: any) => {
        console.log('Categories fetched:', response);
        this.categories = response;
      },
      error: (error: any) => {
        console.log('Error fetching categories:', error);
      }
    });
  }

  //navigate to the product details page for the selected product
  showProductDetails(productId: number) {
    this.productService.addView(productId).subscribe({
      next: (response: any) => {
        console.log('Navigating to product details for product ID:', productId);
        this.router.navigate(['/productViewDetails', { productId: productId }]);
      },
      error: (error : any) => {
        console.log(error);
      }
    });
  }

  //get all products
  public getAllProducts() {
    console.log('Fetching all products');
    this.productService.getAllProducts(this.pageNumber)
      .pipe(map((products: Product[]) => {
        console.log('Processing images for products');
        return products.map((product: Product) => this.imageProcessingService.createImages(product));
      }))
      .subscribe({
        next: (data) => {
          if (data.length != 10) this.showLoadButton = false;
          console.log('Products fetched and processed:', data);
          this.products.push(...data);
        },
        error: (error) => {
          console.log('Error fetching products:', error);
        }
      });
  }

  //load moe products based on pagination
  public loadMoreProducts() {
    this.pageNumber++;
    if (this.selectedCategory) {
      this.filterProductsByCategory(this.selectedCategory, false);
    } else {
      this.getAllProducts();
    }
  }

  //filter products by category and reset pagination
  filterProductsByCategory(categoryName: string, resetProducts: boolean = true) {
    console.log('Filtering products by category:', categoryName);
    this.selectedCategory = categoryName;
    this.pageNumber = resetProducts ? 0 : this.pageNumber;

    if (resetProducts) {
      this.products = [];
    }

    this.productService.getProductsByCategory(categoryName, this.pageNumber)
      .pipe(map((products: Product[]) => {
        return products.map((product: Product) => this.imageProcessingService.createImages(product));
      }))
      .subscribe({
        next: (data: any) => {
          if (data.length != 10) {
            this.showLoadButton = false;
          } else {
            this.showLoadButton = true;
          }
          console.log('Filtered products fetched:', data);
          this.products.push(...data);
        },
        error: (error: any) => {
          console.log('Error fetching products for category:', error);
        }
      });
  }
}