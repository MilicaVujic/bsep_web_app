import { Component, OnInit } from '@angular/core';
import { RegistrationRequest } from '../models/registrationRequest.model';
import { RegistrationService } from './registration.service';
import { FormGroup,FormControl,Validators } from '@angular/forms';
import { AbstractControl ,ValidatorFn} from '@angular/forms';
import { Router } from '@angular/router';
import { SanitizeService } from '../sanitize.service';



@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})

export class RegistrationComponent{
  constructor(
    private registrationService: RegistrationService,
    private router:Router,
    private sanitizeService: SanitizeService
  ) {}

  registrationForm= new FormGroup({
    email: new FormControl('',[Validators.required,Validators.email]),
    password: new FormControl('',[Validators.required, Validators.minLength(6),this.passwordValidator]),
    repeatedPassword: new FormControl('',[Validators.required]),
    name: new FormControl('',[Validators.required]),
    surname: new FormControl(''),
    city: new FormControl('',[Validators.required]),
    country: new FormControl('',[Validators.required]),
    street: new FormControl('',[Validators.required]),
    streetNumber: new FormControl('',[Validators.required]),
    phone: new FormControl('',[Validators.required,Validators.minLength(9)]),
    type:new FormControl("FIZICKO",[Validators.required]),
    packageType:new FormControl("BASIC",[Validators.required]),
    pib:new FormControl('',[this.pibValidator])
  })

  registrationRequest: RegistrationRequest = {
    id:0,
    name: '',
    surname: '',
    username: '',
    password: '',
    city: '',
    country: '',
    street: '',
    streetNumber: '',
    phone: '',
    pib: '',
    packageType: '',
    type: '',
    email:'',
    requestStatus:'NEW'
  };

  repeatedPassword:string;

  onSubmit() {
    this.sanitizeUserInput()
    if (this.areFieldsValid()) {
      this.registrationRequest.username = this.registrationRequest.email;
      this.registrationService.createRegistrationRequest(this.registrationRequest).subscribe(
        res => {
          if (res == null) {
            alert("Email already exists");
          } else {
            this.emptyFileds();
            this.registrationForm.reset();
            alert("Your request is received. We will send you a response to your email.");
            this.router.navigate(["/login"]);
          }
        },
        err => {
          if (err.status === 400) {
            alert("There is already unhandled request with this email.");
          } 
          if (err.status === 405) {
            alert("Request with this email is recently rejected. Try after ban period is over.");
          } 
          if (err.status === 401) {
              alert(
                "Unauthorized. Maybe you should refresh your access token. "
              );
          }
        }
      );
    } else {
      alert("Please fill the form correctly.");
    }
  }
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
  pibValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const pib: string = control.value;
    if (!pib) {
      return null;
    }
  
    const hasNumber = /\d{9}/.test(pib);
  
  
    const valid = hasNumber ;
    
    return valid ? null : { invalidPib: true };
  }

  areFieldsValid(){
    return this.registrationRequest.email.length>0 && this.registrationRequest.password.length>0
      && this.registrationRequest.name.length>0 && this.registrationRequest.packageType.length>0 && this.registrationRequest.type.length>0 && this.registrationForm.valid;  
  }

  emptyFileds(){
    this.registrationRequest.name='',
    this.registrationRequest.surname= '',
    this.registrationRequest.username= '',
    this.registrationRequest.password= '',
    this.repeatedPassword=''
    this.registrationRequest.city= '',
    this.registrationRequest.country= '',
    this.registrationRequest.street= '',
    this.registrationRequest.streetNumber='',
    this.registrationRequest.phone= '',
    this.registrationRequest.pib= '',
    this.registrationRequest.packageType= '',
    this.registrationRequest.type= '',
    this.registrationRequest.email=''
  }

  sanitizeUserInput(): void {
    this.registrationRequest.name = this.sanitizeService.sanitizeInput(this.registrationRequest.name);
    this.registrationRequest.surname=this.sanitizeService.sanitizeInput(this.registrationRequest.surname);
    this.registrationRequest.street=this.sanitizeService.sanitizeInput(this.registrationRequest.street);
    this.registrationRequest.city=this.sanitizeService.sanitizeInput(this.registrationRequest.city);
    this.registrationRequest.country=this.sanitizeService.sanitizeInput(this.registrationRequest.country);
    this.registrationRequest.streetNumber=this.sanitizeService.sanitizeInput(this.registrationRequest.streetNumber);
    this.registrationRequest.password=this.sanitizeService.sanitizeInput(this.registrationRequest.password);
    this.registrationRequest.email=this.sanitizeService.sanitizeInput(this.registrationRequest.email);
    this.registrationRequest.phone=this.sanitizeService.sanitizeInput(this.registrationRequest.phone);
    this.registrationRequest.pib=this.sanitizeService.sanitizeInput(this.registrationRequest.pib);
  }
}
