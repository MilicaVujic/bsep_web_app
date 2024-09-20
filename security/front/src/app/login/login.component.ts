import { Component, OnInit } from '@angular/core';
import { AuthService } from './auth.service';
import { JwtAuthenticationRequest } from '../models/jwtAuthenticationRequest.model';
import { Router } from '@angular/router';
import { ProfileService } from '../profile/profile.service';
import { VerificationRequestDto } from '../models/verificationRequest.model';
import { UserTokenState } from '../models/tokenState.model';
import { PasswordlessAuthenticationRequest } from '../models/passwordlessAuthenticationRequest.model';
import { PasswordlessLoginRequest } from '../models/changePasswordRequest.model';
import { PasswordChangeDto } from '../models/resetPassword.model';
import {NgxCaptchaModule, NgxCaptchaService} from "@binssoft/ngx-captcha";
import { Observable } from 'rxjs';
import { SanitizeService } from '../sanitize.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  captchaConfig:any = {
    type: 1, // 1 or 2 or 3 or 4
    length:6,
    cssClass:'custom',
    back: {
      stroke:"#2F9688",
      solid:"#f2efd2"
    } ,
    font:{
     color:"#000000",
     size:"35px"
    }
  };

  ngOnInit(): void {

    this.captchaService.captchStatus.subscribe((status)=>{
      this.captchaStatus = status;
      if (status == false) {
        //alert("Opps!\nCaptcha mismatch");
      } else  if (status == true) {
        //alert("Success!\nYou are right");
        this.isCaptchaVisible = false;
      }
    });
  }

  request:JwtAuthenticationRequest={
    email:"",
    password:""
  };

  verificationRequest:VerificationRequestDto={
    email:"",
    code:""
  };

  passwordChangeRequest:PasswordlessLoginRequest={
    email:""
  };

  qrcode:string;
  visibility:boolean=true;
  pvisibility:boolean=false;
  email:string;

  constructor(private sanitizeService:SanitizeService,private authService: AuthService,private router:Router,private profileService:ProfileService, private captchaService:NgxCaptchaService) {}

  login(): void {

        console.log(this.request)
        this.verificationRequest.email=this.request.email;
        this.request.email=this.sanitizeService.sanitizeInput(this.request.email)
        this.request.password=this.sanitizeService.sanitizeInput(this.request.password)
        this.authService.login(this.request).subscribe((data:UserTokenState)=>{
          console.log(data)
          this.qrcode=data.secretImageUri||"";
          this.visibility=false;
        /*  this.profileService.getUser(this.request.email).subscribe({
            next: (result:any) => {
              if(result.passwordChanged || result.roles[0].name=='ROLE_CLIENT'){
                this.router.navigate(['home_page'])
              }
              else
                this.router.navigate(['change-password'])
            },
          })*/
          if(data.employee){
            this.isCaptchaVisible = true;
          }
        },
        err => {
          if (err.status === 401) {
            alert("Bad credentials.");
          } 
    
        });

  }

  verify():void{
    
    this.authService.verify(this.verificationRequest).subscribe(data=>{
      console.log(data)
      this.profileService.getUser(this.request.email).subscribe({
        next: (result:any) => {
          if(result.passwordChanged || result.roles[0].name=='ROLE_CLIENT'){
            this.router.navigate(['home_page'])
          }
          else
            this.router.navigate(['change-password'])
        },
      })
      
    },
    err => {
      if (err.status === 401) {
        alert("Bad credentials.");
      } 

    });
  }

  onRegisterNewClient(){
    this.router.navigate(['registration'])
  }

  onPasswordlessLogin(){
    this.router.navigate(['passwordless_login'])
  }

  onResetPassword(){
    this.pvisibility=true;
  }

  change(){
    this.passwordChangeRequest.email=this.email
    this.authService.resetPassword(this.passwordChangeRequest).subscribe(data=>{
      alert("Check email")

    },err=>{
      if(err.status===400){
        alert("User not founded")
      }
    })
    this.pvisibility=false;
  }

  isCaptchaVisible: boolean = false;

  captchaStatus:any = false;



}
