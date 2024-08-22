import { Component, OnInit } from '@angular/core';
import { ProductService } from '../_services/product.service';
import { Product } from '../_model/product.model';
import { map } from 'rxjs/operators';
import { ImageProcessingService } from '../image-processing.service';
import { Router } from '@angular/router';
import { Category } from '../_model/category.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  
  products: Product[] = [];
  categories: Category[] = [];

  constructor(
    private productService: ProductService, 
    private imageProcessingService: ImageProcessingService, 
    private router: Router
  ) { }

  //fetch all products and categories
  ngOnInit(): void {
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
    console.log('Navigating to product details for product ID:', productId);
    this.router.navigate(['/productViewDetails', { productId: productId }]);
  }

  //get all products
  public getAllProducts() {
    console.log('Fetching products');
    this.productService.getAllProducts()
      .pipe(map((products: Product[]) => {
        console.log('Processing images for products');
        return products.map((product: Product) => this.imageProcessingService.createImages(product));
      }))
      .subscribe({
        next: (data) => {
          console.log('Products fetched and processed:', data);
          this.products = data;
        },
        error: (error) => {
          console.log('Error fetching products:', error);
        }
      });
  }
}
