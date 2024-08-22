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

  constructor(private productServe: ProductService, private router: Router) { }

  ngOnInit(): void {
    this.getAllProducts();
  }

  //get all products based on the seller
  public getAllProducts() {
    this.productServe.getAllCompanyProducts().subscribe(
      data => {
        console.log("All products: " + data);
        this.productDetails = data;
      },
      error => {
        console.log("Error getAllProducts: " + error);
      }
    );
  }

  //change characteristics of a product
  changeCharacteristics(element: any) {
    this.router.navigate(['/addNewProduct', { productId: element.productId }]);
  }

  //delete a product
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

}
