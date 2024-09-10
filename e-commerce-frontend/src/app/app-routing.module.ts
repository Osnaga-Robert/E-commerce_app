import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin/admin.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SellerComponent } from './seller/seller.component';
import { RegisterSellerComponent } from './register-seller/register-seller.component';
import { RegisterBuyerComponent } from './register-buyer/register-buyer.component';
import { AddNewProductComponent } from './add-new-product/add-new-product.component';
import { ShowProductDetailsComponent } from './show-product-details/show-product-details.component';
import { ProductResolveService } from './product-resolve.service';
import { DiscountsComponent } from './discounts/discounts.component';
import { ProductViewDetailsComponent } from './product-view-details/product-view-details.component';
import { BuyProductComponent } from './buy-product/buy-product.component';
import { BuyProductResolverService } from './buy-product-resolver.service';
import { OrderConfirmationComponent } from './order-confirmation/order-confirmation.component';
import { CartComponent } from './cart/cart.component';
import { MyOrdersComponent } from './my-orders/my-orders.component';
import { OrderDetailsComponent } from './order-details/order-details.component';
import { AddReviewComponent } from './add-review/add-review.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'admin', component: AdminComponent, canActivate: ['authGuard'], data: { roles: ['ADMIN'] } },
  { path: 'seller', component: SellerComponent, canActivate: ['authGuard'], data: { roles: ['SELLER'] } },
  { path: 'login', component: LoginComponent },
  { path: 'forbidden', component: ForbiddenComponent },
  { path: 'register-seller', component: RegisterSellerComponent },
  { path: 'register-buyer', component: RegisterBuyerComponent },
  {
    path: 'addNewProduct', component: AddNewProductComponent, canActivate: ['authGuard'], data: { roles: ['SELLER'] },
    resolve: {
      product: ProductResolveService
    }
  },
  { path: 'showProductsDetails', component: ShowProductDetailsComponent, canActivate: ['authGuard'], data: { roles: ['SELLER'] } },
  { path: 'discounts', component: DiscountsComponent, canActivate: ['authGuard'], data: { roles: ['SELLER'] } },
  {path: 'productViewDetails', component: ProductViewDetailsComponent, resolve: {product: ProductResolveService}},
  {path : 'buyProduct', component: BuyProductComponent, canActivate: ['authGuard'], data: { roles: ['BUYER']},
  resolve:{
    productDetails: BuyProductResolverService
  }
},
  {path: "orderConfirm", component: OrderConfirmationComponent, canActivate: ['authGuard'], data: { roles: ['BUYER']}},
  {path: "cart", component:CartComponent, canActivate: ['authGuard'], data: { roles: ['BUYER']}},
  {path: "myOrders", component:MyOrdersComponent, canActivate: ['authGuard'], data: { roles: ['BUYER']}},
  {path: "orderInfo", component:OrderDetailsComponent, canActivate: ['authGuard'], data: { roles: ['SELLER']}},
  {path: "addReview", component:AddReviewComponent, canActivate: ['authGuard'], data: { roles: ['BUYER']}}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
