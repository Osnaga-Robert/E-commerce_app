import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { UserAuthService } from '../_services/user-auth.service';
import { Inject, inject, Injectable } from '@angular/core';
import { UserService } from '../_services/user.service';

//auth guard to check user authentication and role-based access.
Injectable({ providedIn: 'root' })
export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const userAuthService: UserAuthService = inject(UserAuthService);
  const router: Router = inject(Router);
  const userService: UserService = inject(UserService);

  if (userAuthService.getToken() != null) {
    const role = route.data['roles'] as Array<string>;
    if (role[0]) {
      const match = userService.roleMatch(role);
      if (match) return true;
      else {
        router.navigate(['/forbidden']);
        return false;
      }
    }
  }

  router.navigate(['/login']);
  return false;
};
