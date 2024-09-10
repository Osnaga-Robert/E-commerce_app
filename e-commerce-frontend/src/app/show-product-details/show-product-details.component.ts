import { Component, OnInit } from '@angular/core';
import { ProductService } from '../_services/product.service';
import { Product } from '../_model/product.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-show-product-details',
  templateUrl: './show-product-details.component.html',
  styleUrls: ['./show-product-details.component.css']
})
export class ShowProductDetailsComponent implements OnInit {

  displayedColumns: string[] = ['productDetails', 'actions'];
  productDetails: Product[] = [];
  pageNumber: number = 0;
  showLoadButton = true;
  showTable = false;
  errorMessage: string = "";

  constructor(private productServe: ProductService, private router: Router) { }

  ngOnInit(): void {
    this.errorMessage = "";
    this.pageNumber = 0;
    this.getAllProducts();
  }

  // Get all products based on the seller
  public getAllProducts() {
    this.showTable = false;
    this.productServe.getPaginationAllCompanyProducts(this.pageNumber).subscribe({
      next: (data) => {
        if (data.length != 10) {
          this.showLoadButton = false;
        }
        data.forEach(p => this.productDetails.push(p));
        this.showTable = true;
      },
      error: (error) => {
        console.log("Error getAllProducts: " + error);
      }
    });
  }

  // Change characteristics of a product
  changeCharacteristics(element: any) {
    this.router.navigate(['/addNewProduct', { productId: element.productId }]);
  }

  // Delete a product
  deleteProduct(element: any) {
    this.productServe.deleteProduct(element.productId).subscribe(
      data => {
        console.log("Deleted " + data);
        this.getAllProducts();
      },
      error => {
        console.log("Error at delete a product: " + error);
      }
    );
  }

  // Load more products based on pagination
  public loadMoreProducts() {
    this.pageNumber++;
    this.getAllProducts();
  }

  // Toggle the active status of a product
  toggleProductStatus(element: any) {
    // Update product's active status
    this.productServe.statusProduct(element).subscribe({
      next: (next : any) => {
        this.errorMessage = "Refresh the page to see the update";
        console.log("Update done");
      },
      error: (error : any) => {
        this.errorMessage = error.error.message;
        console.log(error.error.message);
      }
    });
  }
}
