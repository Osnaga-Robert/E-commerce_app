import { Component, OnInit } from '@angular/core';
import { Product } from '../_model/product.model';
import { ProductService } from '../_services/product.service';
import { ExtendOffer } from '../_model/extend-offer.model';

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
  extendOffer: ExtendOffer = {
    extendOfferProduct: null,
    extendOfferFromDate: '',
    extendOfferToDate: '',
    extendErrorMessage: ''
  };

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    console.log('DiscountsComponent initialized');
    this.loadProducts();
  }

  loadProducts(): void {
    console.log('Loading products');
    this.productService.getAllCompanyProducts().subscribe({
      next: (response: any) => {
        console.log('Products fetched:', response);
        this.products = response.filter((product: Product) => product.productDiscounted === 0);
        this.discountedProducts = response.filter((product: Product) => product.productDiscounted !== 0);
      },
      error: (error: any) => {
        console.log('Error fetching products:', error);
        this.errorMessage = 'Failed to load products.';
      }
    });
  }

  addlistDiscount() {
    console.log('Adding discount');
    this.errorMessage = '';
    this.successMessage = '';
    if (this.selectedProduct && this.discountPercentage != null && this.fromDate && this.toDate) {
      this.selectedProduct.productDiscounted = this.discountPercentage;
      this.selectedProduct.productFromDiscounted = this.fromDate;
      this.selectedProduct.productToDiscounted = this.toDate;
      this.productService.checkProductDiscount(this.selectedProduct).subscribe({
        next: (response: any) => {
          console.log('Discount added successfully:', response);
          this.successMessage = response.message;
          this.appliedDiscounts.push(this.selectedProduct);
          this.products = this.products.filter(product => product.productId !== this.selectedProduct.productId);
          this.resetForm();
        },
        error: (error: any) => {
          console.log('Error adding discount:', error);
          this.errorMessage = error.error.message;
        }
      });
    } else {
      this.errorMessage = "Please fill all the fields";
    }
  }

  applyDiscounts() {
    console.log('Applying discounts');
    for (let product of this.appliedDiscounts) {
      this.productService.setDiscount(product).subscribe({
        next: (response: any) => {
          console.log('Discount applied successfully:', response);
          this.successMessage = response.message;
          this.discountedProducts.push(product);
          this.appliedDiscounts = this.appliedDiscounts.filter(appliedDiscount => appliedDiscount.productId !== product.productId);
        },
        error: (error: any) => {
          console.log('Error applying discount:', error);
          this.errorMessage = error.error.message;
        }
      });
    }
  }

  deleteDiscount(product: Product) {
    console.log('Deleting discount for product:', product);
    this.appliedDiscounts = this.appliedDiscounts.filter(appliedDiscount => appliedDiscount.productId !== product.productId);
    this.products.push(product);
    this.successMessage = 'Discount removed successfully';
    this.loadProducts();
  }

  deleteProductDiscount(product: Product) {
    console.log('Removing product discount for product ID:', product.productId);
    this.productService.removeDiscount(product.productId).subscribe({
      next: (response: any) => {
        console.log('Product discount removed successfully:', response);
        this.successMessage = response.message;
        this.loadProducts();
      },
      error: (error: any) => {
        console.log('Error removing product discount:', error);
        this.errorMessage = error.error.message;
      }
    });
  }

  openExtendOfferModal(product: Product) {
    console.log('Opening extend offer modal for product:', product);
    this.extendOffer.extendErrorMessage = '';
    this.isExtendOfferModalOpen = true;
    this.extendOffer.extendOfferProduct = product;
    this.extendOffer.extendOfferFromDate = product.productFromDiscounted;
    this.extendOffer.extendOfferToDate = product.productToDiscounted;
  }

  closeExtendOfferModal() {
    console.log('Closing extend offer modal');
    this.isExtendOfferModalOpen = false;
  }

  updateOffer() {
    console.log('Updating extend offer');
    this.extendOffer.extendErrorMessage = '';
    if (this.extendOffer.extendOfferProduct) {
      const fromDate = this.extendOffer.extendOfferProduct.productFromDiscounted;
      const toDate = this.extendOffer.extendOfferProduct.productToDiscounted;
      this.extendOffer.extendOfferProduct.productFromDiscounted = this.extendOffer.extendOfferFromDate;
      this.extendOffer.extendOfferProduct.productToDiscounted = this.extendOffer.extendOfferToDate;

      this.productService.setDiscount(this.extendOffer.extendOfferProduct).subscribe({
        next: (response: any) => {
          console.log('Offer extended successfully:', response);
          this.loadProducts();
          this.closeExtendOfferModal();
        },
        error: (error: any) => {
          console.log('Error extending offer:', error);
          if (this.extendOffer.extendOfferProduct) {
            this.extendOffer.extendOfferProduct.productFromDiscounted = fromDate;
            this.extendOffer.extendOfferProduct.productToDiscounted = toDate;
          }
          this.extendOffer.extendErrorMessage = "Failed to extend offer. Please try again.";
        }
      });
    }
  }

  private resetForm() {
    console.log('Resetting form');
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
