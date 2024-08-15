import { Component, OnInit } from '@angular/core';
import { Product } from '../_model/product.model';
import { ProductService } from '../_services/product.service';

@Component({
  selector: 'app-discounts',
  templateUrl: './discounts.component.html',
  styleUrls: ['./discounts.component.css']
})
export class DiscountsComponent implements OnInit {

  products: Product[] = [];
  discountedProducts: Product[] = [];
  appliedDiscounts: Product[] = [];
  selectedProduct: Product = {
    productId: 0,
    productName: '',
    productDescription: '',
    productDiscounted: 0,
    productFromDiscounted: '',
    productToDiscounted: '',
    productPrice: 0,
    productQuantity: 0,
    productCompanySeller: '',
    productImages: [],
    productCategory: []
  };
  discountPercentage: number = 0;
  fromDate: string = '';
  toDate: string = '';
  errorMessage: string = '';
  successMessage: string = '';

  isExtendOfferModalOpen: boolean = false;
  extendOfferProduct: Product | null = null;
  extendOfferFromDate: string = '';
  extendOfferToDate: string = '';
  extendErrorMessage: string = '';

  constructor(private productService: ProductService) { }

  //load the products
  ngOnInit(): void {
    this.loadProducts();
  }

  //get all products and filters based on discount
  loadProducts(): void {
    this.productService.getAllCompanyProducts().subscribe({
      next: (response: any) => {
        this.products = response.filter((product: Product) => product.productDiscounted === 0);
        this.discountedProducts = response.filter((product: Product) => product.productDiscounted !== 0);
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  //add a product to the discount list
  addlistDiscount() {
    this.errorMessage = '';
    this.successMessage = '';
    if (this.selectedProduct && this.discountPercentage != null && this.fromDate && this.toDate) {
      this.selectedProduct.productDiscounted = this.discountPercentage;
      this.selectedProduct.productFromDiscounted = this.fromDate;
      this.selectedProduct.productToDiscounted = this.toDate;
      this.productService.checkProductDiscount(this.selectedProduct).subscribe({
        next: (response: any) => {
          this.successMessage = response.message;
          this.appliedDiscounts.push(this.selectedProduct);
          this.products = this.products.filter(product => product.productId !== this.selectedProduct.productId);
          this.resetForm();
        },
        error: (error: any) => {
          this.errorMessage = error.error.message;
        }
      });
    } else {
      this.errorMessage = "Please fill all the fields";
    }
  }

  //apply the discounts to the selected products
  applyDiscounts() {
    for (let product of this.appliedDiscounts) {
      this.productService.setDiscount(product).subscribe({
        next: (response: any) => {
          this.successMessage = response.message;
          this.discountedProducts.push(product);
          this.appliedDiscounts = this.appliedDiscounts.filter(appliedDiscount => appliedDiscount.productId !== product.productId);
        },
        error: (error: any) => {
          this.errorMessage = error.error.message;
        }
      });
    }
  }

  //remove an applied discount from the discount list
  deleteDiscount(product: Product) {
    this.appliedDiscounts = this.appliedDiscounts.filter(appliedDiscount => appliedDiscount.productId !== product.productId);
    this.products.push(product);
    this.successMessage = 'Discount removed successfully';
    this.loadProducts();
  }

  //removes an applied discount from a product
  deleteProductDiscount(product: Product) {
    this.productService.removeDiscount(product.productId).subscribe({
      next: (response: any) => {
        console.log(response);
        this.successMessage = response.message;
        this.loadProducts();
      },
      error: (error: any) => {
        console.log(error);
        this.errorMessage = error.error.message;
      }
    });
  }

  //initialize the extending of a discount
  openExtendOfferModal(product: Product) {
    this.extendErrorMessage = '';
    this.isExtendOfferModalOpen = true;
    this.extendOfferProduct = product;
    this.extendOfferFromDate = product.productFromDiscounted;
    this.extendOfferToDate = product.productToDiscounted;
  }

  //close the extend disount panel
  closeExtendOfferModal() {
    this.isExtendOfferModalOpen = false;
  }

  //update the discount
  updateOffer() {
    this.extendErrorMessage = '';
    if (this.extendOfferProduct) {
      const fromDate = this.extendOfferProduct.productFromDiscounted;
      const toDate = this.extendOfferProduct.productToDiscounted;
      this.extendOfferProduct.productFromDiscounted = this.extendOfferFromDate;
      this.extendOfferProduct.productToDiscounted = this.extendOfferToDate;

      this.productService.setDiscount(this.extendOfferProduct).subscribe({
        next: (response: any) => {
          this.loadProducts();
          this.closeExtendOfferModal();
        },
        error: (error: any) => {
          if (this.extendOfferProduct) {
            this.extendOfferProduct.productFromDiscounted = fromDate;
            this.extendOfferProduct.productToDiscounted = toDate;
          }
          this.extendErrorMessage = "Failed to extend offer. Please try again.";
        }
      });
    }
  }

  //reset the discount form fileds
  private resetForm() {
    this.selectedProduct = {
      productId: 0,
      productName: '',
      productDescription: '',
      productDiscounted: 0,
      productFromDiscounted: '',
      productToDiscounted: '',
      productPrice: 0,
      productQuantity: 0,
      productCompanySeller: '',
      productImages: [],
      productCategory: []
    }
    this.discountPercentage = 0;
    this.fromDate = '';
    this.toDate = '';
  }
}
