import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Product } from '../_model/product.model';
import { Observable } from 'rxjs';
import { Category } from '../_model/category.model';
import { ApiConfigService } from '../api-config.service';
import { OrderDetails } from '../_model/order-details.model';
import { MyOrderDetails } from '../_model/order.model';
import { Review } from '../_model/review.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private readonly PATH_OF_API: string;

  constructor(private httpClient: HttpClient, private apiConfigService: ApiConfigService) {
    this.PATH_OF_API = this.apiConfigService.API_BASE_URL;
  }

  //fetch all categories
  getCategories(): Observable<Category[]> {
    return this.httpClient.get<Category[]>(this.PATH_OF_API + "/category/getAll");
  }

  //add new product
  public addProduct(product: FormData) {
    return this.httpClient.post<Product>(this.PATH_OF_API + "/seller/product/add", product);
  }

  //fetch all products associated with a company
  public getPaginationAllCompanyProducts(pageNumber : number): Observable<Product[]> {
    return this.httpClient.get<Product[]>(this.PATH_OF_API + "/seller/product/getByCompanyName?pageNumber=" + pageNumber);
  }

  public getAllByCompanyProducts(): Observable<Product[]> {
    return this.httpClient.get<Product[]>(this.PATH_OF_API + "/seller/product/getAllByCompanyName");
  }

  //delete a product by Id
  public deleteProduct(productId: number) {
    return this.httpClient.delete(this.PATH_OF_API + "/seller/product/delete/" + productId);
  }

  //fetch all products
  public getAllProducts(pageNumber : number): Observable<Product[]> {
    return this.httpClient.get<Product[]>(this.PATH_OF_API + "/product/getAll?pageNumber=" + pageNumber);
  }

  //get a product by Id
  public getProductById(productId: number): Observable<Product> {
    return this.httpClient.get<Product>(this.PATH_OF_API + "/product/getById/" + productId);
  }

  //check if a discount is eligible for a discount
  public checkProductDiscount(product: Product): Observable<String> {
    return this.httpClient.post<String>(this.PATH_OF_API + "/seller/product/checkDiscount", product);
  }

  //set a discount on a product
  public setDiscount(product: Product): Observable<String> {
    return this.httpClient.post<String>(this.PATH_OF_API + "/seller/product/setDiscount", product);
  }

  //remove a discount from a product by Id
  public removeDiscount(productId: number) {
    return this.httpClient.delete(this.PATH_OF_API + "/seller/product/deleteDiscount/" + productId);
  }

  //get all products based on buyer's cart
  public getProductDetails(isSingleProductCheckout: any, productId: any): Observable<Product[]> {
    return this.httpClient.get<Product[]>(`${this.PATH_OF_API}/buyer/getProductDetails/${isSingleProductCheckout}/${productId}`);
  }

  //place an order
  public placeOrder(orderDetails : OrderDetails, isCartCheckout : any){
    return this.httpClient.post(this.PATH_OF_API+ "/buyer/placeOrder/" + isCartCheckout, orderDetails);
  }

  //add a product to cart
  public addToCart(productId: any){
    return this.httpClient.get(`${this.PATH_OF_API}/buyer/cart/addProduct/${productId}`)
  }

  //get cart details
  public getCart(){
    return this.httpClient.get(`${this.PATH_OF_API}/buyerr/cart/getDetails`);
  }

  //delete a product from a cart
  public deleteCart(cartId: any){
    return this.httpClient.delete(`${this.PATH_OF_API}/buyer/cart/deleteItem/${cartId}`);
  }

  public getMyOrders(): Observable<MyOrderDetails[]>{
    return this.httpClient.get<MyOrderDetails[]>(`${this.PATH_OF_API}/buyer/order/getAll`);
  }

  public getAllOrders(): Observable<MyOrderDetails[]>{
    return this.httpClient.get<MyOrderDetails[]>(`${this.PATH_OF_API}/seller/order/getAllSeller`);
  }

  public markDelivered(orderId : number){
    return this.httpClient.get(`${this.PATH_OF_API}/seller/order/delivered/${orderId}`);
  }

  public addReview(productId: number, review: Review){
    return this.httpClient.post(`${this.PATH_OF_API}/buyer/review/add/${productId}`, review);
  }

  public getReviews(productId: number){
    return this.httpClient.get(`${this.PATH_OF_API}/review/get/${productId}`);
  }

  public statusProduct(productId: number){
    return this.httpClient.post(`${this.PATH_OF_API}/seller/product/statusProduct/${productId}`,{});
  }
}
