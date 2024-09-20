import { Component, OnInit } from '@angular/core';
import { FormGroup,FormControl,Validators } from '@angular/forms';
import { AbstractControl ,ValidatorFn} from '@angular/forms';
import { JwtAuthenticationRequest } from '../models/jwtAuthenticationRequest.model';
import { PasswordChangeDto } from '../models/resetPassword.model';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../login/auth.service';
import { SanitizeService } from '../sanitize.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit{
  repeatedPassword:string;
  ngOnInit(): void {
    this.route.queryParams.subscribe(params=>{
      this.registrationRequest.email=params["email"]
      this.registrationRequest.token=params["token"]
    })

  }
  constructor(private route: ActivatedRoute,private authService:AuthService,private router:Router,private sanitizeService:SanitizeService) { }

  onSubmit(){
    this.registrationRequest.password=this.sanitizeService.sanitizeInput(this.registrationRequest.password)
    this.registrationForm.value.repeatedPassword=this.sanitizeService.sanitizeInput(this.registrationForm.value.repeatedPassword||"")
    this.registrationRequest.password=this.registrationForm.value.repeatedPassword||"";
    console.log(this.registrationRequest)
    this.authService.setNewPassword(this.registrationRequest).subscribe(data=>{

    },
  err=>{
    if(err.code==404){
      alert("Not found user")
    }
  });
    alert("Changed password")
    this.router.navigate(['login']);
  }

  registrationForm= new FormGroup({
    password: new FormControl('',[Validators.required, Validators.minLength(6),this.passwordValidator]),
    repeatedPassword: new FormControl('',[Validators.required]),
  })


  passwordValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const password: string = control.value;
    if (!password) {
      return null;
    }
  
    const hasNumber = /\d/.test(password);
    const hasUppercase = /[A-Z]/.test(password);
    const hasLowercase = /[a-z]/.test(password);
    const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password);
  
    const valid = hasNumber && hasUppercase && hasLowercase && hasSpecial;
    
    return valid ? null : { invalidPassword: true };
  }

  isRepatPasswordIsEmpty() {
    let repeatPsswordIsEmpty: boolean = true;
    if (this.registrationForm.value.repeatedPassword?.length||1 > 0) repeatPsswordIsEmpty = false;
    return repeatPsswordIsEmpty;
  }

  isPasswordCorrect() {
    let isCorrectPassword: boolean = false;
    if (this.registrationForm.value.repeatedPassword === this.registrationForm.value.password)
      isCorrectPassword = true;

    return isCorrectPassword;
  }

  registrationRequest: PasswordChangeDto = {
    password: '',
    email:'',
    token:''
  };
}
