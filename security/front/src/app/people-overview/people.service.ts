import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/env/environment';

@Injectable({
  providedIn: 'root',
})
export class PeopleService {
  constructor(private http: HttpClient) {}

  getEmployees(): Observable<any> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<any>(environment.apiHost + 'user/employees/',options);
  }
  getClients(): Observable<any> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<any>(environment.apiHost + 'user/clients/',options);
  }
}
