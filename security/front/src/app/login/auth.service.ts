import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { JwtAuthenticationRequest } from '../models/jwtAuthenticationRequest.model';
import { Observable, of, throwError } from 'rxjs';
import { UserTokenState } from '../models/tokenState.model';
import { environment } from 'src/env/environment';
import { HttpHeaders } from '@angular/common/http';
import { ApiService } from './api.service';
import { catchError, map } from 'rxjs/operators';
import { PasswordlessAuthenticationRequest } from '../models/passwordlessAuthenticationRequest.model';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { ChangePass } from '../models/change-password.model';
import { VerificationRequestDto } from '../models/verificationRequest.model';
import { getVpnMessageDto } from '../models/vpnresponse.model';
import { PasswordlessLoginRequest } from '../models/changePasswordRequest.model';
import { PasswordChangeDto } from '../models/resetPassword.model';
import { NotificationMessage } from '../models/notificationMessage.model';
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  isFirstLogin() {
    const jwtToken = localStorage.getItem('jwt');
  
      const headers = new HttpHeaders({
        Authorization: 'Bearer ' + jwtToken,
      });
  
      const options = {
        headers: headers,
      };
      return this.http.get<any>(environment.apiHost + 'user/firstLogin/'+this.getUser().sub,options);
    
  }
  constructor(
    private http: HttpClient,
    private apiService: ApiService,
    private router: Router
  ) {}

  getVpnMessage():Observable<getVpnMessageDto>{
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<getVpnMessageDto>(environment.apiHost+ 'vpn/get-message',options)
  }

  resetPassword(requestData: PasswordlessLoginRequest): Observable<any> {
    return this.http.post<any>(environment.apiHost + 'auth/resetPassword', requestData);
  }
  setNewPassword(requestData: PasswordChangeDto): Observable<any> {
    return this.http.post<any>(environment.apiHost + 'auth/setNewPassword', requestData);
  }

  login(requestData: JwtAuthenticationRequest): Observable<UserTokenState> {
    return this.http.post<UserTokenState>(environment.apiHost + 'auth/login', requestData);
  }


  verify(verificationRequest: VerificationRequestDto) {
    const loginHeaders = new HttpHeaders({
      Accept: 'application/json',
      'Content-Type': 'application/json',
    });
    const body = {
      email: verificationRequest.email,
      code: verificationRequest.code,
    };
    return this.apiService
      .post(
        environment.apiHost + 'auth/verify',
        JSON.stringify(body),
        loginHeaders
      )
      .pipe(
        map((res) => {
          console.log('Verification success');
          const userTokenState = res.body as UserTokenState;
          localStorage.setItem('jwt', userTokenState.accesstoken);
          localStorage.setItem('refreshToken', userTokenState.refreshtoken);
          console.log(localStorage.getItem('jwt'));
          console.log(this.getUser());
        })
      );
  }

/*
  login(jwtAuthenticationRequest: JwtAuthenticationRequest) {
    const loginHeaders = new HttpHeaders({
      Accept: 'application/json',
      'Content-Type': 'application/json',
    });
    const body = {
      email: jwtAuthenticationRequest.email,
      password: jwtAuthenticationRequest.password,
    };
    return this.apiService
      .post(
        environment.apiHost + 'auth/login',
        JSON.stringify(body),
        loginHeaders
      )
      .pipe(
        map((res) => {
          console.log('Login success');
          const userTokenState = res.body as UserTokenState;

          localStorage.setItem('jwt', userTokenState.accesstoken);
          localStorage.setItem('refreshToken', userTokenState.refreshtoken);
          console.log(localStorage.getItem('jwt'));
          console.log(this.getUser());
        })
      );
  }*/

  logout() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('refreshToken');
  }
  getAuthToken(): string | null {
    return localStorage.getItem('jwt');
  }
  getUser(): any {
    const token = this.getAuthToken();
    if (token) {
      const tokenPayload = this.parseJwt(token);
      return tokenPayload;
    } else {
      return null;
    }
  }

  private parseJwt(token: string): any {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        })
        .join('')
    );

    return JSON.parse(jsonPayload);
  }
  isAuthenticated(): boolean {
    const token = this.getAuthToken();
    return token !== null;
  }
  getUserRoles(): string[] | null {
    const token = this.getAuthToken();
    if (token) {
      const tokenPayload = this.parseJwt(token);
      return tokenPayload.role; // Pretpostavka da su uloge u JWT tokenu definisane kao niz "roles"
    } else {
      return null;
    }
  }

  getRolePermissions(): string[] | null {
    const token = this.getAuthToken();
    if (token) {
      const tokenPayload = this.parseJwt(token);
      console.log(token);
      console.log(tokenPayload);
      return tokenPayload.permission;
    } else {
      return null;
    }
  }

  checkAdminNotifications():Observable<NotificationMessage>{
    const jwtToken = localStorage.getItem('jwt');

    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + jwtToken,
    });

    const options = {
      headers: headers,
    };
    return this.http.get<NotificationMessage>(environment.apiHost+ 'user/criticalEvents',options)
  }

  passwordlesslogin(jwtAuthenticationRequest: JwtAuthenticationRequest) {
    const loginHeaders = new HttpHeaders({
      Accept: 'application/json',
      'Content-Type': 'application/json',
    });
    console.log("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa")
    const body = {
      email: jwtAuthenticationRequest.email,
    };
    return this.apiService
      .post(
        environment.apiHost + 'auth/passwordlesslogin',
        JSON.stringify(body),
        loginHeaders
      )
      .pipe(
        map((res) => {
          alert('Check email');
        })
      );
  }

  passwordlessauth(passwordlessauth: PasswordlessAuthenticationRequest) {
    this.http
      .post<any>(
        environment.apiHost + 'auth/passwordlessAuth',
        passwordlessauth,
        { observe: 'response' }
      )
      .subscribe((resp) => {
        console.log(resp);
        const accessToken = resp.headers.get('Accesstoken');
        const refreshToken = resp.headers.get('Refreshtoken');
        localStorage.setItem('jwt', accessToken || '');
        localStorage.setItem('refreshToken', refreshToken || '');
        console.log(accessToken);
        console.log(refreshToken);
        this.router.navigate(['home_page']);
      });
  }

  regenereateAccessToken(tokenState: UserTokenState) {
    this.http
      .post<any>(
        environment.apiHost + 'auth/regeneratingJwtToken',
        tokenState,
        { observe: 'response' }
      )
      .subscribe(
        (resp) => {
          console.log(resp);
          const accessToken = resp.headers.get('Accesstoken');
          localStorage.setItem('jwt', accessToken || '');
          console.log(accessToken);
          this.router.navigate(['home_page']);
        },
        (err) => {
          if (err.status === 400) {
            alert(
              "Access token can't be refreshed cause it isn't expired or it is invalid ."
            );
          }
        }
      );
  }
  changePassword(changePass: ChangePass): Observable<any> {
    const url = `${environment.apiHost}user/changePassword`;
    const options = { responseType: 'text' as 'json' }; // Postavljamo responseType na 'text'
    return this.http.put(url, changePass, options);
  }
}
@Injectable({
  providedIn: 'root',
})
export class ChangePasswordGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): Observable<boolean> {
    return this.authService.isFirstLogin().pipe(
      map(response => {
        if (response.isPasswordChanged) {
          return true; 
        } else {
          this.router.navigate(['change-password']); 
          return false;
        }
      }),
      catchError(error => {
        if (error.status === 400) {
          this.router.navigate(['change-password']); 
          return of(false);
        } else {
          return throwError(error);
        }
      })
    );
  }
  
}