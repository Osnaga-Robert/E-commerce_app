import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-register-buyer',
  templateUrl: './register-buyer.component.html',
  styleUrls: ['./register-buyer.component.css']
})
export class RegisterBuyerComponent implements OnInit {
  passwordMismatch: boolean = false;
  repeatPasswordEmpty: boolean = false;
  errorMessage: string = '';

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
  }

  //handle buyer registration
  register(registerForm: NgForm) {
    const { userPassword, userRepeatPassword } = registerForm.value;

    this.repeatPasswordEmpty = false;
    this.passwordMismatch = false;
    this.errorMessage = '';

    if (userRepeatPassword === '') {
      this.repeatPasswordEmpty = true;
      return;
    }
    if (userPassword !== userRepeatPassword) {
      this.passwordMismatch = true;
      return;
    }

    this.userService.registerNewBuyer(registerForm.value).subscribe({
      next: (response: any) => {
        console.log("User registered successfully");
        console.log(response);
        this.router.navigate(['/login']);
      },
      error: (error: any) => {
        this.errorMessage = error.error.message;
      }
    });
  }
}
