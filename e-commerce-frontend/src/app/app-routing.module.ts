import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin/admin.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SellerComponent } from './seller/seller.component';
import { RegisterSellerComponent } from './register-seller/register-seller.component';
import { RegisterBuyerComponent } from './register-buyer/register-buyer.component';
import { BuyerComponent } from './buyer/buyer.component';
import { AddNewProductComponent } from './add-new-product/add-new-product.component';
import { ShowProductDetailsComponent } from './show-product-details/show-product-details.component';
import { ProductResolveService } from './product-resolve.service';
import { DiscountsComponent } from './discounts/discounts.component';
import { ProductViewDetailsComponent } from './product-view-details/product-view-details.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'admin', component: AdminComponent, canActivate: ['authGuard'], data: { roles: ['ADMIN'] } },
  { path: 'seller', component: SellerComponent, canActivate: ['authGuard'], data: { roles: ['SELLER'] } },
  { path: 'buyer', component: BuyerComponent, canActivate: ['authGuard'], data: { roles: ['BUYER'] } },
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
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
