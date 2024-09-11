import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { response } from 'express';
import { error } from 'console';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  
  //table's columns from HTML
  displayedColumns = ['accounts', 'actions'];
  accounts: Object[] = [];

  constructor(private userService: UserService) { }
  
  ngOnInit(): void {
    console.log('AdminComponent initialized');
    this.forAdmin();
  }

  // Get the message for the admin's page
  forAdmin() {
    console.log('Fetching data for admin');
    this.userService.getNonActivatedAccounts().subscribe({
      next: (response: any) => {
        console.log('Received response:', response);
        this.accounts = [];
        this.accounts = response;
      },
      error: (error: any) => {
        console.log('Error occurred:', error);
      }
    });
  }

  //accept a seller
  accept(email: any){
    this.userService.activateAccount(email).subscribe({
      next: (response: any) => {
        console.log(response);
        console.log(email + " was accepted");
        this.forAdmin();
      },
      error: (error: any) => {
        console.log("Error: " + error);
        this.forAdmin();
      }
    });
  }

  //decline a seller
  decline(email: any){
    this.userService.declineAccount(email).subscribe({
      next: (response: any) => {
        console.log("Deleted");
        this.forAdmin();
      },
      error: (error: any) => {
        console.log("Error: " + error);
        this.forAdmin();
      }
    });
  }
}
