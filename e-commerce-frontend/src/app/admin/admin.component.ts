import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  
  message: string = '';

  constructor(private userService: UserService) { }
  
  ngOnInit(): void {
    console.log('AdminComponent initialized');
    this.forAdmin();
  }

  //get the message for the admin's page
  forAdmin() {
    console.log('Fetching data for admin');
    this.userService.getDataForAdmin().subscribe({
      next: (response: any) => {
        console.log('Received response:', response);
        this.message = response;
      },
      error: (error: any) => {
        console.log('Error occurred:', error);
      }
    });
  }
}
