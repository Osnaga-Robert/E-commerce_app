import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS, provideHttpClient } from '@angular/common/http';  // Ensure HttpClientModule is imported

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { AdminComponent } from './admin/admin.component';
import { SellerComponent } from './seller/seller.component';
import { LoginComponent } from './login/login.component';
import { HeaderComponent } from './header/header.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { authGuard } from './_auth/auth.guard';
import { UserService } from './_services/user.service';
import { AuthInterceptor } from './_auth/auth.interceptor';
import { RegisterSellerComponent } from './register-seller/register-seller.component';
import { RegisterBuyerComponent } from './register-buyer/register-buyer.component';
import { BuyerComponent } from './buyer/buyer.component';
import { AddNewProductComponent } from './add-new-product/add-new-product.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input';
import { MatOptionModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatGridListModule } from '@angular/material/grid-list';
import { ShowProductDetailsComponent } from './show-product-details/show-product-details.component';
import { MatTableModule } from '@angular/material/table';
import { ProductResolveService } from './product-resolve.service';
import { DiscountsComponent } from './discounts/discounts.component';
import { ProductViewDetailsComponent } from './product-view-details/product-view-details.component';
import { BuyProductComponent } from './buy-product/buy-product.component';
import { OrderConfirmationComponent } from './order-confirmation/order-confirmation.component';
import { CartComponent } from './cart/cart.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AdminComponent,
    SellerComponent,
    LoginComponent,
    HeaderComponent,
    ForbiddenComponent,
    RegisterSellerComponent,
    RegisterBuyerComponent,
    BuyerComponent,
    AddNewProductComponent,
    ShowProductDetailsComponent,
    DiscountsComponent,
    ProductViewDetailsComponent,
    BuyProductComponent,
    OrderConfirmationComponent,
    CartComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,  // Add HttpClientModule here
    RouterModule,
    MatFormFieldModule,
    MatInputModule,
    MatOptionModule,
    MatSelectModule,
    MatGridListModule,
    MatTableModule,
    NgSelectModule,
    FormsModule
  ],
  providers: [
    { provide: 'authGuard', useValue: authGuard },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    UserService,
    provideHttpClient(),
    provideAnimationsAsync(),
    ProductResolveService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
