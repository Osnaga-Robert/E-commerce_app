import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-register-seller',
  templateUrl: './register-seller.component.html',
  styleUrls: ['./register-seller.component.css']  // Corrected styleUrls property name
})
export class RegisterSellerComponent implements OnInit {

  passwordMismatch: boolean = false;
  repeatPasswordEmpty: boolean = false;
  errorMessage: string = '';

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {}

  //handle seller registration
  register(registerForm: NgForm) {
    const { userPassword, userRepeatPassword } = registerForm.value;

    this.passwordMismatch = false;
    this.repeatPasswordEmpty = false;
    this.errorMessage = '';

    if (!userRepeatPassword) {
      this.repeatPasswordEmpty = true;
      return;
    }

    if (userPassword !== userRepeatPassword) {
      this.passwordMismatch = true;
      return;
    }

    this.userService.registerNewSeller(registerForm.value).subscribe({
      next: (response: any) => {
        console.log("User registered successfully");
        this.router.navigate(['/login']);
      },
      error: (error: any) => {
        this.errorMessage = error.error.message;
      }
    });
  }
}
