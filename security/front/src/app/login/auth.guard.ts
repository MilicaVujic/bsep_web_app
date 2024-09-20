import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (this.authService.isAuthenticated()) {
      return true; // Korisnik je autentifikovan, dozvoli pristup
    } else {
      this.router.navigate(['/login']); // Preusmeri na stranicu za prijavu
      return false; // Ne dozvoli pristup
    }
  }
}