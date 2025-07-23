import {ActivatedRouteSnapshot, CanDeactivate, RouterStateSnapshot} from '@angular/router';
import {IFormComponent} from '../meta-model/iform-component';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

@Injectable()
export class CanDeactivateGuard implements CanDeactivate<IFormComponent>{

  canDeactivate(
    component: IFormComponent,
    currentRoute: ActivatedRouteSnapshot,
    currentState: RouterStateSnapshot): Observable<boolean> | boolean  {
    return component.canDeactivateRoute();
  }

}
