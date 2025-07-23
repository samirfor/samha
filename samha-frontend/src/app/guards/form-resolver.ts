import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {DataService} from '../shared/service/data.service';
import {Observable} from 'rxjs';

@Injectable()
export class FormResolver implements Resolve<Observable<any>>{

  constructor(private dataService: DataService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
    let parts = state.url.split('/');
    let id = parts[parts.length-1];
    let resource = parts[parts.length-2];
    return this.dataService.get(resource, id);
  }



}
