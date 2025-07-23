import {Component, OnInit, ViewChild, ElementRef} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {DataService} from '../shared/service/data.service';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {NotificationService} from "../shared/service/notification.service";
import {AutocompleteFieldComponent} from "../shared/fields/autocomplete-field/autocomplete-field.component";
import { Console } from 'console';

@Component({
  selector: 'samha-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit{
  @ViewChild('anoInput', {static: false}) anoInput: ElementRef;
  @ViewChild('semestreInput', {static: false}) semestreInput: ElementRef;
  public formGroup: FormGroup;

  loading$: Observable<any>;

  columns: any[] = [];

  constructor(private dataService: DataService,
              private notification: NotificationService,
              private formBuilder: FormBuilder) {
    this.formGroup = formBuilder.group({
        // ,      periodo: [1]
    });
  }
  ngOnInit(): void {
    this.loading$ = this.dataService.post('menu/list', null).pipe(
      catchError(
        err => {
          this.notification.handleError(err)
          return of(new Error(err));
        }
      )
    )
    console.log(new Date().getUTCFullYear())
    // this.formGroup.get('ano').setValue(new Date().getUTCFullYear());
    // this.formGroup.get('semestre').setValue(new Date().getMonth() < 6 ? 1 : 2);
  }
  
}
