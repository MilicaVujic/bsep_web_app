import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../login/auth.service';
import { ChangePass } from '../models/change-password.model';
import { ProfileService } from '../profile/profile.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css'],
})
export class ChangePasswordComponent implements OnInit {
  currentPassword: string = '';
  newPassword: string = '';
  confirmPassword: string = '';
  email: string = '';
  changePass: ChangePass = {
    email: '',
    password: '',
  };
  constructor(
    private authService: AuthService,
    private router: Router,
    private profileService: ProfileService
  ) {}
  ngOnInit(): void {
    var result = this.authService.getUser();
    this.email = result.sub;
    console.log(this.email);
  }

  changePassword() {
    if (this.newPassword !== this.confirmPassword) {
      alert('New password and confirm password must be equal.');
      return;
    }
    this.changePass.email = this.email;
    this.changePass.password = this.newPassword;
    console.log(this.changePass);
    this.authService.changePassword(this.changePass).subscribe(
      (res) => {
        alert('Changed.');
        this.router.navigate(['/home_page']);
      },
      (err) => {
        if (err.status === 400) {
          alert('Invalid password.');
        }
      }
    );
  }

  isPasswordCorrect() {
    let isCorrectPassword: boolean = false;
    if (this.newPassword === this.confirmPassword)
      isCorrectPassword = true;

    return isCorrectPassword;
  }

  isPasswordEmpty() {
    let passwordIsEmpty: boolean = true;
    if (this.newPassword.length||1 > 0) passwordIsEmpty = false;
    return passwordIsEmpty;
  }
}
