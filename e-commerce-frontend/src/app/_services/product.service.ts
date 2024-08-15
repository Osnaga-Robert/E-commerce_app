import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Product } from '../_model/product.model';
import { Observable } from 'rxjs';
import { Category } from '../_model/category.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private httpClient: HttpClient) { }

  //fetch all categories
  getCategories(): Observable<Category[]> {
    return this.httpClient.get<Category[]>(`https://localhost:8080/category/getAll`);
  }

  //add new product
  public addProduct(product: FormData) {
    return this.httpClient.post<Product>("https://localhost:8080/product/add", product);
  }

  //fetch all products associated with a company
  public getAllCompanyProducts(): Observable<Product[]> {
    return this.httpClient.get<Product[]>("https://localhost:8080/product/getByCompanyName");
  }

  //delete a product by Id
  public deleteProduct(productId: number) {
    return this.httpClient.delete(`https://localhost:8080/product/delete/${productId}`);
  }

  //fetch all products
  public getAllProducts(): Observable<Product[]> {
    return this.httpClient.get<Product[]>("https://localhost:8080/product/getAll");
  }

  //get a product by Id
  public getProductById(productId: number): Observable<Product> {
    return this.httpClient.get<Product>(`https://localhost:8080/product/getById/${productId}`);
  }

  //check if a discount is eligible for a discount
  public checkProductDiscount(product: Product): Observable<String> {
    return this.httpClient.post<String>("https://localhost:8080/product/checkDiscount", product);
  }

  //set a discount on a product
  public setDiscount(product: Product): Observable<String> {
    return this.httpClient.post<String>("https://localhost:8080/product/setDiscount", product);
  }

  //remove a discount from a product by Id
  public removeDiscount(productId: number) {
    return this.httpClient.delete(`https://localhost:8080/product/deleteDiscount/${productId}`);
  }
}
