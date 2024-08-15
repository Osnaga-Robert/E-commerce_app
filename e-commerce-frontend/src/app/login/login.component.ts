import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserService } from '../_services/user.service';
import { UserAuthService } from '../_services/user-auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  errorMessage: string = '';

  constructor(private userService: UserService,
    private userAuthService: UserAuthService,
    private router: Router
  ) {

  }
  ngOnInit(): void { }

  //handle login request
  login(loginForm: NgForm) {
    this.errorMessage = '';
    this.userService.login(loginForm.value).subscribe({
      next: (response: any) => {
        console.log("Login Success");
        this.userAuthService.setRoles(response.user.role.toString());
        this.userAuthService.setToken(response.jwtToken);
        console.log(this.userAuthService.getRoles());
        console.log(this.userAuthService.getToken());
        const role = response.user.role.toString();
        if (role === 'ADMIN') {
          console.log("Admin");
          this.router.navigate(['/admin']);
        } else if (role === 'SELLER') {
          this.router.navigate(['/seller']);
        }
        else {
          this.router.navigate(['/buyer']);
        }
      },
      error: (error: any) => {
        this.errorMessage = error.error.message;
      }
    });
  }

  //go to the seller's page
  registerSeller() {
    this.router.navigate(['/register-seller']);
  }

  //go to the buyer's page
  registerBuyer() {
    this.router.navigate(['/register-buyer']);
  }
}
