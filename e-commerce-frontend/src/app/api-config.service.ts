import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiConfigService {

  readonly API_BASE_URL = 'https://localhost:8080';

  constructor() { }

}
