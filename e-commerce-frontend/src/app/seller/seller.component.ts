import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-seller',
  templateUrl: './seller.component.html',
  styleUrl: './seller.component.css'
})
export class SellerComponent implements OnInit {

  message: string = '';

  constructor(private userService: UserService) { }
  
  ngOnInit(): void {
    this.forSeller();
  }

  //get the message from the seller's page
  forSeller() {
    this.userService.getDataForSeller().subscribe({
      next: (response: any) => {
        console.log("Message: " + response);
        this.message = response;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }
}
