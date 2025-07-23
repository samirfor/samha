import {EventEmitter, Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, CanDeactivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable, of} from 'rxjs';
import {LocalStorageService} from '../shared/service/local-storage.service';
import {catchError, map} from 'rxjs/operators';
import {AuthService} from '../shared/service/auth.service';
import {Location} from "@angular/common";

@Injectable()
export class AuthGuard implements CanActivate, CanDeactivate<any> {


  constructor(private localStorage: LocalStorageService,
              private router: Router,
              private authService: AuthService) {
  }

  canDeactivate(component: any, route: ActivatedRouteSnapshot, currentState: RouterStateSnapshot): Observable<boolean> | boolean {
    return this.canActivate(route, currentState);
  }



  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | boolean{
    if (route.routeConfig.path !== 'login') {
      return this.authService.isTokenValid().pipe(
        map(
          data => {
            return true;
          }
        ),
        catchError(_ => {
          return this.router.navigate(['login']);
        })
      )
    }
  }
}
