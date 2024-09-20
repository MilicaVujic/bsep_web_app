import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { RegistrationRequestsOverviewComponent } from './registration-requests-overview/registration-requests-overview.component';
import { LoginComponent } from './login/login.component';
import { HomePageComponent } from './home-page/home-page.component';
import { EmployeeProfileComponent } from './profile/employee-profile/employee-profile.component';
import { ClientProfileComponent } from './profile/client-profile/client-profile.component';
import { AdminProfileComponent } from './profile/admin-profile/admin-profile.component';
import { CreateAdvertisementRequestComponent } from './advertisement/create-advertisement-request/create-advertisement-request.component';
import { AdvertisementRequestsOverviewComponent } from './advertisement/advertisement-requests-overview/advertisement-requests-overview.component';
import { CreateAdvertisementComponent } from './advertisement/create-advertisement/create-advertisement.component';
import { AdvertisementsOverviewComponent } from './advertisement/advertisements-overview/advertisements-overview.component';
import { AdminRegistrationComponent } from './registration/admin-registration/admin-registration.component';
import { EmployeeRegistrationComponent } from './registration/employee-registration/employee-registration.component';
import { EmployeesOverviewComponent } from './people-overview/employees-overview/employees-overview.component';
import { ClientsOverviewComponent } from './people-overview/clients-overview/clients-overview.component';
import { PasswordlessLoginComponent } from './passwordless-login/passwordless-login.component';
import { AuthorizingComponent } from './authorizing/authorizing.component';
import { ClientsBlockingOverviewComponent } from './clients-overview/clients-overview.component';
import { AuthGuard } from './login/auth.guard';
import { AdminGuard } from './login/admin.guard';
import { ClientGuard } from './login/cilent.guard';
import { EmployeeGuard } from './login/employee.guard';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { PermissionManageComponent } from './permission-manage/permission-manage.component';
import { ChangePasswordGuard } from './login/auth.service';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { AdvertisementDetailsComponent } from './advertisement/advertisement-details/advertisement-details.component';
import { RegisterAdminComponent } from './register-admin/register-admin.component';

const routes: Routes = [
  {
    path: 'registration',
    component: RegistrationComponent,
  },
  {
    path: 'registration-requests-overview',
    component: RegistrationRequestsOverviewComponent,
    canActivate:[AuthGuard,ChangePasswordGuard]
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'home_page',
    component: HomePageComponent,
    canActivate:[AuthGuard,ChangePasswordGuard]
  },
  {
    path: 'employee-profile',
    component: EmployeeProfileComponent,
    canActivate:[AuthGuard, ChangePasswordGuard]
  },
  {
    path: 'client-profile',
    component: ClientProfileComponent,
    canActivate:[AuthGuard,ChangePasswordGuard]
  },
  {
    path: 'admin-profile',
    component: AdminProfileComponent,
    canActivate:[AuthGuard,ChangePasswordGuard]
  },
  {
    path: 'create-advertisement-request',
    component: CreateAdvertisementRequestComponent,
    canActivate:[AuthGuard,ChangePasswordGuard]
  },
  {
    path: 'advertisement-request',
    component: AdvertisementRequestsOverviewComponent,
    canActivate:[AuthGuard,ChangePasswordGuard]
  },
  {
    path: 'create-advertisement/:id',
    component: CreateAdvertisementComponent,
    canActivate:[AuthGuard,ChangePasswordGuard]

  },
  {
    path: 'advertisements',
    component: AdvertisementsOverviewComponent,
    canActivate:[AuthGuard,ChangePasswordGuard]

  },
  {
    path: 'admin-registration',
    component: AdminRegistrationComponent,
    canActivate:[AuthGuard,ChangePasswordGuard]
  },
  {
    path: 'employee-registration',
    component: EmployeeRegistrationComponent,
    canActivate:[AuthGuard,ChangePasswordGuard]

  },
  {
    path: 'employees-overview',
    component: EmployeesOverviewComponent,
    canActivate:[AuthGuard,ChangePasswordGuard]

  },
  {
    path: 'clients-overview',
    component: ClientsOverviewComponent,
    canActivate:[AuthGuard,ChangePasswordGuard]

  },
  {
    path: 'passwordless_login',
    component: PasswordlessLoginComponent,
  },
  {
    path: 'authorizing',
    component: AuthorizingComponent,
  },
  {
    path: 'change-password',
    component: ChangePasswordComponent,
    canActivate:[AuthGuard]
  },
  {
    path: 'permission-manage',
    component: PermissionManageComponent,
    canActivate:[AdminGuard,ChangePasswordGuard]
  },
  {
    path: 'clients-blocking',
    component: ClientsBlockingOverviewComponent,
    canActivate:[AdminGuard,ChangePasswordGuard]
  },
  {
    path: 'reset-password',
    component: ResetPasswordComponent,
  },
  {
    path: 'advertisement-details/:id',
    component: AdvertisementDetailsComponent
  },
  {
    path: 'register-admin',
    component: RegisterAdminComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
