import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, MaybeAsync, Resolve, RouterStateSnapshot } from '@angular/router';
import { Product } from './_model/product.model';
import { map, Observable, of } from 'rxjs';
import { ProductService } from './_services/product.service';
import { ImageProcessingService } from './image-processing.service';

@Injectable({
  providedIn: 'root'
})
export class ProductResolveService implements Resolve<Product> {

  constructor(private productService: ProductService, private imageProcessingService: ImageProcessingService) { }

  //resolve product data before navigating to a route
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Product> {
    const id = route.paramMap.get("productId");
    if (id) {
      return this.productService.getProductById(Number(id)).pipe(map(p => this.imageProcessingService.createImages(p)));
    }
    else {
      return of(this.getProductDetails());
    }
  }

  //get default product details
  getProductDetails() {
    return {
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
    };
  }
}
