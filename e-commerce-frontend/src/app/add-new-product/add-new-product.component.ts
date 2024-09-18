import { Component, OnInit } from '@angular/core';
import { Product } from '../_model/product.model';
import { NgForm } from '@angular/forms';
import { ProductService } from '../_services/product.service';
import { FileHandle } from '../_model/file-handle.model';
import { DomSanitizer } from '@angular/platform-browser';
import { Category } from '../_model/category.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-add-new-product',
  templateUrl: './add-new-product.component.html',
  styleUrl: './add-new-product.component.css'
})
export class AddNewProductComponent implements OnInit {

  isNewProduct = true;
  errorMessage: string = '';
  product: Product = {
    productId: 0,
    productName: '',
    productDescription: '',
    productDiscounted: 0,
    productFromDiscounted: '',
    productToDiscounted: '',
    productPrice: 0,
    productQuantity: 0,
    productCompanySeller: '',
    isActive: false,
    views: 0,
    productImages: [],
    productCategory: [],
    productReview: []
  }
  category: Category = {
    categoryName: '',
    categoryDescription: ''
  };
  categories: Category[] = [];

  constructor(private productService: ProductService, private sanitizer: DomSanitizer, private activatedRoute: ActivatedRoute) { }

  //get available product(if we update a product) and get all categories
  ngOnInit(): void {
    this.product = this.activatedRoute.snapshot.data['product'];
    console.log('Product loaded for editing:', this.product);

    if (this.product && this.product.productId) {
      this.isNewProduct = false;
      console.log('Editing existing product');
    } else {
      console.log('Adding new product');
    }

    this.productService.getCategories().subscribe({
      next: (response: any) => {
        console.log("Categories fetched:", response);
        this.categories = response;
      },
      error: (error: any) => {
        console.log('Error fetching categories:', error);
      }
    });
  }

  //add or update a product
  addProduct(productForm: NgForm) {
    this.errorMessage = '';
    this.product.productCategory.push(this.category);
    console.log('Product to be added or updated:', this.product);

    this.productService.addProduct(this.prepareFormData(this.product)).subscribe({
      next: (response: any) => {
        console.log('Product added or updated successfully:', response);
        productForm.reset();
      },
      error: (error: any) => {
        console.log('Error adding or updating product:', error);
        this.errorMessage = error.error.message;
      }
    });
  }

  //prepare the datas and the images to be sent in the request
  prepareFormData(product: Product): FormData {
    const formData = new FormData();
    formData.append(
      'product',
      new Blob([JSON.stringify(product)], { type: 'application/json' })
    );
    console.log('Form data prepared for product:', formData);

    for (let i = 0; i < product.productImages.length; i++) {
      formData.append(
        'imageFile',
        product.productImages[i].file,
        product.productImages[i].file.name
      );
      console.log('Image added to form data:', product.productImages[i].file.name);
    }
    return formData;
  }

  //handle file selection for product images
  onFileSelected(event: any) {
    if (event.target.files.length) {
      const file = event.target.files[0];
      const fileHandle: FileHandle = {
        file: file,
        url: this.sanitizer.bypassSecurityTrustUrl(window.URL.createObjectURL(file))
      }
      this.product.productImages.push(fileHandle);
      console.log('File selected and added:', file.name);
    }
  }

  //remove an image from the list
  removeImages(index: number) {
    console.log('Removing image at index:', index, 'Image:', this.product.productImages[index]);
    this.product.productImages.splice(index, 1);
  }
}
