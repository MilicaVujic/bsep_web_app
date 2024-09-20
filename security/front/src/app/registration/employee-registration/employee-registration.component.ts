import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { User } from 'src/app/models/user.model';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { AuthService } from 'src/app/login/auth.service';
import { Address } from 'src/app/models/address.model';
import { RegistrationService } from '../registration.service';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { SanitizeService } from 'src/app/sanitize.service';

@Component({
  selector: 'app-employee-registration',
  templateUrl: './employee-registration.component.html',
  styleUrls: ['./employee-registration.component.css'],
})
export class EmployeeRegistrationComponent implements OnInit {
  constructor(
    private service: RegistrationService,
    private router: Router,
    private appComponent: AppComponent,
    private authService: AuthService,
    private sanitizeService:SanitizeService  ) {}
  email: string = '';

  ngOnInit(): void {}

  address: Address = {
    city: '',
    country: '',
    street: '',
    streetNumber: '',
  };
  user: User = {
    id: 0,
    email: '',
    password: '',
    firstName: '',
    username: '',
    lastName: '',
    address: this.address,
    phone: '',
    blocked:false
  };

  addressForm = new FormGroup({
    city: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[A-Z][A-Za-z ]*$/),
    ]),
    country: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[A-Z][A-Za-z ]*$/),
    ]),
    street: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[A-Z][A-Za-z ]*$/),
    ]),
    streetNumber: new FormControl('', []),
  });

  userForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
      this.passwordValidator,
    ]),
    confirmPassword: new FormControl('', [Validators.required]),
    firstName: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[A-Z][A-Za-z ]*$/),
    ]),
    lastName: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[A-Z][A-Za-z ]*$/),
    ]),

    phone: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[0-9]{9,10}$/),
    ]),
  });

  createEmployee(): void {
    var hasPermission = false;
    var permissions = this.authService.getRolePermissions();
    console.log(permissions)
    permissions?.forEach((p) => {
      if (p == 'REG_EMPLOYEE_ADMIN') {
        hasPermission = true;
      }
    });
    if (hasPermission) {
      if (this.isInputValid) {
        var oldUsername = this.user.username;
        const address: Address = {
          city: this.addressForm.value.city || '',
          country: this.addressForm.value.country || '',
          street: this.addressForm.value.street || '',
          streetNumber: this.addressForm.value.streetNumber || '',
        };
        const user: User = {
          id: this.user!.id,
          email: this.userForm.value.email || '',
          password: this.userForm.value.password || '',
          firstName: this.userForm.value.firstName || '',
          username: this.userForm.value.email || '',
          lastName: this.userForm.value.lastName || '',
          address: address,
          phone: this.userForm.value.phone || '',
          blocked:false
        };
        this.sanitizeUserInput()
        console.log(user)
        this.service.createUser(user, 'ROLE_EMPLOYEE').subscribe({
          next: (result: User) => {
            if (result == null)
              alert(
                'Please enter valid data. (email must be unique and all fields filled)'
              );
            else {
              this.user = result;
              alert('Employee registered.');
              this.router.navigate(['home_page']);
            }
          },
          error:(error) => {
            if (error.status === 401) {
              alert(
                "Unauthorized. Maybe you should refresh your access token. "
              );
            }
          }
        });
      } else alert('Please enter valid data.');
    } else alert('No permission.');
  }

  get isUsernameInvalid(): boolean {
    const usernameControl = this.userForm.get('username');
    return (
      !!usernameControl &&
      usernameControl.invalid &&
      (usernameControl.dirty || usernameControl.touched)
    );
  }

  get isNameInvalid(): boolean {
    const nameControl = this.userForm.get('firstName');
    return (
      !!nameControl &&
      nameControl.invalid &&
      (nameControl.dirty || nameControl.touched)
    );
  }

  get isSurnameInvalid(): boolean {
    const surnameControl = this.userForm.get('lastName');
    return (
      !!surnameControl &&
      surnameControl.invalid &&
      (surnameControl.dirty || surnameControl.touched)
    );
  }

  get isCityInvalid(): boolean {
    const cityControl = this.userForm.get('city');
    return (
      !!cityControl &&
      cityControl.invalid &&
      (cityControl.dirty || cityControl.touched)
    );
  }

  get isCountryInvalid(): boolean {
    const countryControl = this.userForm.get('country');
    return (
      !!countryControl &&
      countryControl.invalid &&
      (countryControl.dirty || countryControl.touched)
    );
  }

  get isStreetInvalid(): boolean {
    const streetControl = this.userForm.get('street');
    return (
      !!streetControl &&
      streetControl.invalid &&
      (streetControl.dirty || streetControl.touched)
    );
  }

  get isNumberInvalid(): boolean {
    const numberControl = this.userForm.get('phone');
    return (
      !!numberControl &&
      numberControl.invalid &&
      (numberControl.dirty || numberControl.touched)
    );
  }

  get isPasswordInvalid(): boolean {
    const passwordControl = this.userForm.get('password');
    return (
      !!passwordControl &&
      passwordControl.invalid &&
      (passwordControl.dirty || passwordControl.touched)
    );
  }

  get isConfirmPasswordInvalid(): boolean {
    const passwordControl = this.userForm.get('password');
    const confirmPasswordControl = this.userForm.get('confirmPassword');
    return (
      !!passwordControl &&
      !!confirmPasswordControl &&
      passwordControl.value !== confirmPasswordControl.value
    );
  }

  get isInputValid(): boolean {
    return (
      !this.isNameInvalid &&
      !this.isSurnameInvalid &&
      !this.isUsernameInvalid &&
      !this.isNumberInvalid &&
      !this.isCityInvalid &&
      !this.isCountryInvalid &&
      !this.isStreetInvalid &&
      !this.isPasswordInvalid &&
      !this.isConfirmPasswordInvalid &&
      this.areFieldsValid()
    );
  }
  passwordValidator(
    control: AbstractControl
  ): { [key: string]: boolean } | null {
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
  areFieldsValid() {
    return this.userForm.valid;
  }

  sanitizeUserInput(): void {
    this.user.email=this.sanitizeService.sanitizeInput(this.user.email)
    this.user.password=this.sanitizeService.sanitizeInput(this.user.password)
    this.user.lastName=this.sanitizeService.sanitizeInput(this.user.lastName)
    this.user.firstName=this.sanitizeService.sanitizeInput(this.user.firstName)
    this.user.phone=this.sanitizeService.sanitizeInput(this.user.phone)
    this.address.city=this.sanitizeService.sanitizeInput(this.address.city)
    this.address.country=this.sanitizeService.sanitizeInput(this.address.country)
    this.address.street=this.sanitizeService.sanitizeInput(this.address.street)
    this.address.streetNumber=this.sanitizeService.sanitizeInput(this.address.streetNumber)


  }
}
