import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-buyer',
  templateUrl: './buyer.component.html',
  styleUrls: ['./buyer.component.css']
})
export class BuyerComponent implements OnInit{

  message: string = '';

  constructor(private userService: UserService) { }
  
  ngOnInit(): void {
    console.log('BuyerComponent initialized');
    this.forBuyer();
  }

  //get the message from the buyer's page
  forBuyer(){
    console.log('Fetching data for buyer');
    this.userService.getDataForBuyer().subscribe({
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
