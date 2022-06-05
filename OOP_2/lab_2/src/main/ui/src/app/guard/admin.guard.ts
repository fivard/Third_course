import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { Observable } from "rxjs";
import { filter, switchMap, first, map } from "rxjs/operators";
import { AuthService } from "../service";

@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {
  constructor(private router: Router, private authService: AuthService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.authService.isDoneLoading$.pipe(
      filter(isDone => isDone),
      switchMap(() => this.authService.isAdminAuthenticated$.pipe(
        first(),
        map((isAuthenticated) => {
          if (!isAuthenticated) {
            this.router.navigateByUrl('/login');
          }
          return isAuthenticated;
        })
      )),
    );
  }
}
