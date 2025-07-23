import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {APIPREFIX} from '../../app.component';
import {Observable, of} from 'rxjs';
import {LocalStorageService} from './local-storage.service';

@Injectable({providedIn: 'root'})
export class AuthService{

  public loggedIn: EventEmitter<boolean> = new EventEmitter<boolean>();
  public isLogado: boolean = false;

  constructor(private http: HttpClient,
              private localStorage: LocalStorageService) {
  }

  public login(body: any){
    return this.http.post(APIPREFIX + 'login', body, {
      headers: new HttpHeaders()
        .set('Content-Type', 'application/x-www-form-urlencoded')
    });
  }

  public isTokenValid(): Observable<any>{
    return this.http.post(APIPREFIX + 'auth/isTokenValid', null, this.getOptions());
  }

  private getOptions() {
    return {
      headers: new HttpHeaders()
        .set('Authorization', 'Bearer ' + this.localStorage.get('access_token'))
    }
  }

  public changePassword(body: { senha: any; login: any }): Observable<any> {
    return this.http.post(APIPREFIX + 'auth/change-password', body);
  }

  public refreshToken(): Observable<any> {
    return this.http.get(APIPREFIX + 'auth/refreshToken', {
      headers: new HttpHeaders()
        .set('Authorization', 'Refresh ' + this.localStorage.get('refresh_token'))
    });
  }

  public refreshSession(): Observable<any> {
    return this.http.post(APIPREFIX + 'auth/refreshSession', null, this.getOptions());
  }
}
