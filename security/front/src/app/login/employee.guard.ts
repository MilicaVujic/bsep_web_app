import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class EmployeeGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (this.isEmployee()) {
      return true; // Korisnik je autentifikovan, dozvoli pristup
    } else {
      this.router.navigate(['/login']); // Preusmeri na stranicu za prijavu
      return false; // Ne dozvoli pristup
    }
  }

  isEmployee(): boolean {
    const userRoles = this.authService.getUserRoles();
    return userRoles !== null && userRoles.includes('ROLE_EMPLOYEE');
  }
}