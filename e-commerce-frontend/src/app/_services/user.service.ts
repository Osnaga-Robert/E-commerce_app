import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserAuthService } from './user-auth.service';
import { ApiConfigService } from '../api-config.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly PATH_OF_API: string;

  requestHeaders = new HttpHeaders(
    { 'No-Auth': 'True' }
  );

  
  constructor(private httpClient: HttpClient, private userAuthService: UserAuthService, private apiConfigService: ApiConfigService) {
    this.PATH_OF_API = this.apiConfigService.API_BASE_URL;
  }

  //send a login request with user credentials
  public login(loginData: any) {
    return this.httpClient.post(this.PATH_OF_API + "/authenticate", loginData, { headers: this.requestHeaders });
  }

  //get data for users with the 'SELLER' role
  public getDataForSeller() {
    return this.httpClient.get(this.PATH_OF_API + "/forSeller", { responseType: 'text' });
  }

  //get data for users with the 'BUYER' role
  public getDataForBuyer() {
    return this.httpClient.get(this.PATH_OF_API + "/forBuyer", { responseType: 'text' });
  }

  //get data for users with 'ADMIN' role
  public getDataForAdmin() {
    return this.httpClient.get(this.PATH_OF_API + "/forAdmin", { responseType: 'text' });
  }

  //register a new seller
  public registerNewSeller(registerData: any) {
    return this.httpClient.post(this.PATH_OF_API + "/registerNewSeller", registerData);
  }

  //register a new buyer
  public registerNewBuyer(registerData: any) {
    return this.httpClient.post(this.PATH_OF_API + "/registerNewBuyer", registerData);
  }

  //check if the user's role matches any of the allowed roles
  public roleMatch(allowedRoles: string[]): boolean {
    var isMatch = false;
    const userRoles: any = this.userAuthService.getRoles();
    if (userRoles != null && userRoles) {
      for (let j = 0; j < allowedRoles.length; j++) {
        if (userRoles === allowedRoles[j]) {
          isMatch = true;
          return isMatch;
        }
        else {
          return isMatch;
        }
      }
    }
    return isMatch;
  }
}
