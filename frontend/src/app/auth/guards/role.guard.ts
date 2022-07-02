import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { UserTokenState } from 'src/app/core/models/response/user-token-state.model';
import { JwtService } from 'src/app/core/services/jwt.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(
    private router: Router,
    private jwtService: JwtService
  ) { }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const token: UserTokenState = this.jwtService.getToken();
    if (!token) {
      this.router.navigate(['/auth/sign-in']);
      return false;
    }
    return true;
  }

}
