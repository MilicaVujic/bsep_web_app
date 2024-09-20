import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/env/environment';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  constructor(private http: HttpClient) {}
  getEmployee(email: string): Observable<any> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<any>(
      environment.apiHost + 'user/employeeProfile/' + email,options
    );
  }

  updateEmployee(
    user: User,
    hashPassword: boolean,
    role: string,
    oldPassword: string
  ): Observable<User> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.put<any>(
      environment.apiHost + 'user/update/' + hashPassword + '/' + role + '/' + oldPassword,
      user,options
    );
  }
  updateClient(
    user: User,
    hashPassword: boolean,
    role: string,
    oldPassword: string
  ): Observable<User> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.put<any>(
      environment.apiHost + 'user/update/' + hashPassword + '/' + role + '/' + oldPassword,
      user,options
    );
  }
  getAdmin(email: string): Observable<any> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<any>(
      environment.apiHost + 'user/adminProfile/' + email,options
    );
  }

  updateAdmin(
    user: User,
    hashPassword: boolean,
    role: string,
    oldPassword: string
  ): Observable<User> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.put<any>(
      environment.apiHost + 'user/update/' + hashPassword + '/' + role + '/' + oldPassword,
      user,options
    );
  }
  getClient(email: string): Observable<any> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<any>(
      environment.apiHost + 'user/clientProfile/' + email,options
    );
  }

  getClientForType(email: string): Observable<any> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<any>(
      environment.apiHost + 'user/client/' + email,options
    );
  }

  getUser(email: string): Observable<any> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<any>(
      environment.apiHost + 'user/getAll/' + email,options
    );
  }

  getAll(): Observable<User[]> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<User[]>(
      environment.apiHost + 'user/all',options
    );
  }

  block(email:string):Observable<User[]>{
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<User[]>(
      environment.apiHost + 'user/block/'+email,options
    );
  }

  unblock(email:string):Observable<User[]>{
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<User[]>(
      environment.apiHost + 'user/unblock/'+email,options
    );
  }

  deleteUserData(id:number):Observable<void>{
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.delete<void>(
      environment.apiHost + 'user/delete/'+id,options
    );
  }
}
