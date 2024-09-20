import { Component, OnInit } from '@angular/core';
import { ActivatedRoute,Router } from '@angular/router';
import { AuthService } from '../login/auth.service';
import { PasswordlessAuthenticationRequest } from '../models/passwordlessAuthenticationRequest.model';

@Component({
  selector: 'app-authorizing',
  templateUrl: './authorizing.component.html',
  styleUrls: ['./authorizing.component.css']
})
export class AuthorizingComponent implements OnInit {

  username:string;
  token:string;
  signature:string;
  passwordlessAuthRequest:PasswordlessAuthenticationRequest={
    username:"",
    token:"",
    signature:""

  };
  
  constructor(private route: ActivatedRoute,private authService:AuthService,private router:Router) { }
  ngOnInit() {
    this.route.queryParams.subscribe(params => {

      this.passwordlessAuthRequest.token=params['token'];
      this.passwordlessAuthRequest.signature=params['signature'];

      
      this.authService.passwordlessauth(this.passwordlessAuthRequest);
    });
  }
}
