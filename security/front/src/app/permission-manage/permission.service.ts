import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/env/environment';

@Injectable({
  providedIn: 'root'
})
export class PermissionService {

  constructor(private http: HttpClient) { }
  getExisting(role: string): Observable<any> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<any>(
      environment.apiHost + 'permission/existing/' + role,options
    );
  }

  getUnexisting(role: string): Observable<any> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<any>(
      environment.apiHost + 'permission/unexisting/' + role,options
    );
  }
  addPermission(name:string,role: string): Observable<any> {
    const jwtToken = localStorage.getItem('jwt');
    console.log("JwtToken"+jwtToken)

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    console.log("Options",options)
    return this.http.get<any>(
      environment.apiHost + 'permission/add/' + name+'/'+role,options
    );
  }
  removePermission(name:string,role: string): Observable<any> {
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<any>(
      environment.apiHost + 'permission/remove/' + name+'/'+role,options
    );
  }
}
