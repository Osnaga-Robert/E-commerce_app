import { Component, OnInit } from '@angular/core';
import { UserAuthService } from '../_services/user-auth.service';
import { Router } from '@angular/router';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private userAuthService: UserAuthService,
    private router: Router,
    private userService: UserService) { }

  ngOnInit(): void { }

  //check if the user is logged in
  public isLoggedIn() {
    return this.userAuthService.isLoggedIn();
  }

  //log out the user
  public logout() {
    this.userAuthService.clear();
    this.router.navigate(['/home']);
  }

  //check if the user's role matches with any of the allowed roles
  public roleMatch(allowedRoles: string[]): boolean {
    return this.userService.roleMatch(allowedRoles);
  }

  //get the page based on the account's role
  navigateToDashboard() {
    if (this.userService.roleMatch(['ADMIN'])) {
      this.router.navigate(['/admin']);
    }
    else if (this.userService.roleMatch(['SELLER'])) {
      this.router.navigate(['/seller']);
    }
    else {
      this.router.navigate(['/home']);
    }
  }

  //check if the use is a 'SELLER'
  public isSeller() {
    return this.userAuthService.isSeller();
  }
}
