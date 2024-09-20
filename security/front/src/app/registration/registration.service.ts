import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RegistrationRequest } from '../models/registrationRequest.model';
import { Observable } from 'rxjs';
import { environment } from 'src/env/environment';
import { HttpHeaders } from '@angular/common/http';
import { User } from '../models/user.model';
@Injectable({
  providedIn: 'root',
})
export class RegistrationService {
  constructor(private http: HttpClient) {}

  createRegistrationRequest(
    request: RegistrationRequest
  ): Observable<RegistrationRequest> {
    return this.http.post<RegistrationRequest>(
      environment.apiHost + 'registerRequest',
      request
    );
  }

  getAllRegistrationRequests(): Observable<RegistrationRequest[]> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<RegistrationRequest[]>(
      environment.apiHost + 'registerRequests',
      options
    );
  }

  acceptRegistrationRequests(
    request: RegistrationRequest
  ): Observable<RegistrationRequest[]> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.put<RegistrationRequest[]>(
      environment.apiHost + 'registrationRequest/accept',
      request,
      options
    );
  }

  rejectRegistrationRequests(
    request: RegistrationRequest,
    explanation: String
  ): Observable<RegistrationRequest[]> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.put<RegistrationRequest[]>(
      environment.apiHost + 'registrationRequest/reject/' + request.id,
      explanation,
      options
    );
  }

  createUser(user: User, role: String): Observable<User> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.post<User>(
      environment.apiHost + 'user/creatingUser/' + role,
      user,options
    );
  }

  createAdmin(user: User): Observable<User> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.post<User>(
      environment.apiHost + 'user/registerAdmin',
      user
    );
  }
}
