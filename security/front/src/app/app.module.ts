import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormGroup, FormsModule, NgForm } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './registration/registration.component';
import { HttpClientModule } from '@angular/common/http';
import { RegistrationRequestsOverviewComponent } from './registration-requests-overview/registration-requests-overview.component';
import { LoginComponent } from './login/login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HomePageComponent } from './home-page/home-page.component';
import { EmployeeProfileComponent } from './profile/employee-profile/employee-profile.component';
import { ClientProfileComponent } from './profile/client-profile/client-profile.component';
import { AdminProfileComponent } from './profile/admin-profile/admin-profile.component'
import { CreateAdvertisementRequestComponent } from './advertisement/create-advertisement-request/create-advertisement-request.component';
import { CreateAdvertisementComponent } from './advertisement/create-advertisement/create-advertisement.component';
import { AdvertisementRequestsOverviewComponent } from './advertisement/advertisement-requests-overview/advertisement-requests-overview.component';
import { AdvertisementsOverviewComponent } from './advertisement/advertisements-overview/advertisements-overview.component';
import { DatePipe } from '@angular/common';
import { AdminRegistrationComponent } from './registration/admin-registration/admin-registration.component';
import { EmployeeRegistrationComponent } from './registration/employee-registration/employee-registration.component';
import { EmployeesOverviewComponent } from './people-overview/employees-overview/employees-overview.component';
import { ClientsOverviewComponent } from './people-overview/clients-overview/clients-overview.component';
import { PasswordlessLoginComponent } from './passwordless-login/passwordless-login.component';
import { AuthorizingComponent } from './authorizing/authorizing.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { PermissionManageComponent } from './permission-manage/permission-manage.component';
import { DialogComponent } from './dialog/dialog.component';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ClientsBlockingOverviewComponent } from './clients-overview/clients-overview.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { AdvertisementDetailsComponent } from './advertisement/advertisement-details/advertisement-details.component';
import {NgxCaptchaModule} from "@binssoft/ngx-captcha";
import { RegisterAdminComponent } from './register-admin/register-admin.component';


@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    RegistrationRequestsOverviewComponent,
    LoginComponent,
    HomePageComponent,
    EmployeeProfileComponent,
    ClientProfileComponent,
    AdminProfileComponent,
    CreateAdvertisementRequestComponent,
    CreateAdvertisementComponent,
    AdvertisementRequestsOverviewComponent,
    AdvertisementsOverviewComponent,
    AdminRegistrationComponent,
    EmployeeRegistrationComponent,
    EmployeesOverviewComponent,
    ClientsOverviewComponent,
    PasswordlessLoginComponent,
    PasswordlessLoginComponent,
    AuthorizingComponent,
    ChangePasswordComponent,
    PermissionManageComponent,
    DialogComponent,
    ClientsBlockingOverviewComponent,
    ResetPasswordComponent,
    AdvertisementDetailsComponent,
    RegisterAdminComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatDialogModule,
    NgxCaptchaModule, 
  ],
  providers: [
    DatePipe, // Dodajte DatePipe kao provider
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
