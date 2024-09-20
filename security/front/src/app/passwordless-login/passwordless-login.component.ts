import { Component } from '@angular/core';
import { JwtAuthenticationRequest } from '../models/jwtAuthenticationRequest.model';
import { AuthService } from '../login/auth.service';

@Component({
  selector: 'app-passwordless-login',
  templateUrl: './passwordless-login.component.html',
  styleUrls: ['./passwordless-login.component.css']
})
export class PasswordlessLoginComponent {
  request:JwtAuthenticationRequest={
    email:"",
    password:""
  };

  constructor(private authService: AuthService) {}

  login(): void {
    console.log(this.request)
    this.authService.passwordlesslogin(this.request).subscribe(data=>{
      alert("Check email")
    }, err => {
      if (err.status === 400) {
        alert("Not found user.");
      } 
    });
  }
}
