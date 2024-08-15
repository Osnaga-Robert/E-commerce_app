import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent implements OnInit {
  message: string = '';
  constructor(private userService: UserService) { }
  ngOnInit(): void {
    this.forAdmin();
  }

  //get the message for the admin's page
  forAdmin() {
    this.userService.forAdmin().subscribe({
      next: (response: any) => {
        console.log(response);
        this.message = response;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }
}
