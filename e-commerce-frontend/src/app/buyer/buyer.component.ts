import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-buyer',
  templateUrl: './buyer.component.html',
  styleUrl: './buyer.component.css'
})
export class BuyerComponent implements OnInit{

  message: string = '';
  constructor(private userService: UserService) { }
  ngOnInit(): void {
    this.forBuyer();
  }

  //get the message from the buyer's page
  forBuyer(){
    this.userService.forBuyer().subscribe({
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
