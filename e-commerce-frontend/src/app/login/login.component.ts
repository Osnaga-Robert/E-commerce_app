import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserService } from '../_services/user.service';
import { UserAuthService } from '../_services/user-auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  errorMessage: string = '';

  constructor(
    private userService: UserService,
    private userAuthService: UserAuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    console.log('LoginComponent initialized');
  }

  //handle login request
  login(loginForm: NgForm) {
    console.log('Login attempt with form value:', loginForm.value);
    this.errorMessage = '';
    this.userService.login(loginForm.value).subscribe({
      next: (response: any) => {
        console.log('Login Success:', response);
        this.userAuthService.setRoles(response.user.role.toString());
        this.userAuthService.setToken(response.jwtToken);
        console.log('User roles:', this.userAuthService.getRoles());
        console.log('Token:', this.userAuthService.getToken());
        
        const role = response.user.role.toString();
        if (role === 'ADMIN') {
          console.log('Redirecting to Admin page');
          this.router.navigate(['/admin']);
        } else if (role === 'SELLER') {
          console.log('Redirecting to Seller page');
          this.router.navigate(['/seller']);
        } else {
          console.log('Redirecting to Buyer page');
          this.router.navigate(['/home']);
        }
      },
      error: (error: any) => {
        console.log('Login Error:', error);
        this.errorMessage = error.error.message;
      }
    });
  }

  //go to the seller's page
  registerSeller() {
    console.log('Navigating to Register Seller page');
    this.router.navigate(['/register-seller']);
  }

  //go to the buyer's page
  registerBuyer() {
    console.log('Navigating to Register Buyer page');
    this.router.navigate(['/register-buyer']);
  }
}
