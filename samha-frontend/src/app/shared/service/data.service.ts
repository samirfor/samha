import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {LocalStorageService} from './local-storage.service';
import {QueryMirror} from '../query-mirror';
import {PagedList} from '../paged-list';
import {APIPREFIX} from '../../app.component';
import {NotificationService} from "./notification.service";

@Injectable({providedIn: 'root'})
export class DataService {

  showMenu: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(private http: HttpClient,
              private localStorage: LocalStorageService,
              private notification: NotificationService) {
  }

  public query(query: QueryMirror): Observable<PagedList | any> {
    return this.http.post<PagedList>(APIPREFIX + query.entityPath + '/query', query, this.getOptions());
  }

  public publicQuery(query: QueryMirror): Observable<PagedList | any> {
    return this.http.post<PagedList>(APIPREFIX + 'public/' + query.entityPath + '/query', query);
  }

  public publicPost(resource: string, body: any): Observable<any>{
    return this.http.post(APIPREFIX + 'public/' + resource, body);
  }

  public queryLog(query: QueryMirror): Observable<PagedList> {
    return this.http.post<PagedList>(APIPREFIX + query.entityPath + '/log', query, this.getOptions());
  }

  public get(resource: string, id: string): Observable<any>{
    return this.http.get(APIPREFIX + resource + '/' + id, this.getOptions());
  }

  public getAll(resource: string): Observable<any>{
    return this.http.get(APIPREFIX + resource + '/all', this.getOptions());
  }

  public publicGetAll(resource: string): Observable<any>{
    return this.http.get(APIPREFIX + 'public/' + resource + '/all');
  }

  public post(resource: string, body: any): Observable<any>{
    return this.http.post(APIPREFIX + resource, body, this.getOptions());
  }

  public asyncPost(resource: string, body: any): Observable<HttpEvent<Object>> {
    return this.http.post(APIPREFIX + resource, body, {
      headers: new HttpHeaders()
        .set('Authorization', 'Bearer ' + this.localStorage.get('access_token')),
      observe: 'events',
      reportProgress: true
    })
  }

  public publicAsyncPost(resource: string, body: any): Observable<HttpEvent<Object>> {
    return this.http.post(APIPREFIX + 'public/' + resource, body, {
      observe: 'events',
      reportProgress: true
    })
  }

  public save(resource: string, body: any): Observable<any>{
    return this.http.post(APIPREFIX + resource + '/insert', body, this.getOptions());
  }

  public update(resource: string, id: string,  body: any): Observable<any>{
    return this.http.patch(APIPREFIX + resource + '/' + id, body, this.getOptions())
  }

  private getOptions() {
    return {
      headers: new HttpHeaders()
        .set('Authorization', 'Bearer ' + this.localStorage.get('access_token'))
    }
  }

  public delete(resource: string, id): Observable<any> {
    return this.http.delete(APIPREFIX + resource + '/' + id, this.getOptions());
  }
}
