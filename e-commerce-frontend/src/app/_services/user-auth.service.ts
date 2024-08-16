import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class UserAuthService {
  constructor() { }

  //store user roles in local storage
  public setRoles(roles: string): void {
    localStorage.setItem('role', roles);
  }

  //get user roles from local storage
  public getRoles(): string {
    return localStorage.getItem('role') || '';
  }

  //store auth token in local storage
  public setToken(token: string): void {
    localStorage.setItem('token', token);
  }

  //get auth token from local storage
  public getToken(): string {
    return localStorage.getItem('token') || '';
  }

  //clear all data from local storage
  public clear(): void {
    localStorage.clear();
  }

  //check if a user is logged in
  public isLoggedIn(): boolean {
    return !!this.getToken();
  }

  //check if the logged-in user has a 'SELLER' role
  public isSeller(): boolean {
    if (this.getRoles().toString() === "SELLER") {
      return true;
    }
    return false;
  }
}
