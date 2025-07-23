import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DataService} from "../shared/service/data.service";
import {first} from "rxjs/operators";
import {NotificationService} from "../shared/service/notification.service";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'samha-label',
  templateUrl: './label.component.html',
  styleUrls: ['./label.component.css']
})
export class LabelComponent implements OnInit {
  public labels: ILabel[] = [];
  form: FormGroup;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private dataService: DataService,
              private notification: NotificationService,
              private formBuilder: FormBuilder) {
    this.form = formBuilder.group({
      duracao: [50]
    })
  }

  ngOnInit(): void {
    this.dataService.getAll('label').pipe(first()).subscribe(
      (data: ILabel[]) => {
        this.labels = data.sort((a, b) => a.numero > b.numero ? 1 : -1);
      }
    );
  }

  goToLog() {
    this.router.navigate(['log'], {relativeTo: this.route});
  }

  logEvent(editingStart: string) {
    console.log(editingStart)
  }

  format(row) {
    switch (row) {
      case 0: return 'Matutino'
      case 1: return 'Vespertino'
      case 2: return 'Noturno'
    }

  }

  save(row: any) {
    if (row.changes[0]){
      const data: ILabel = row.changes[0].data;

      this.dataService.update('label', data.id.toString(), data).pipe(first()).subscribe(
        next => {
          this.notification.success('A label foi alterada com sucesso!');
        }, error => {
          this.notification.handleError(error);
        }
      )
    }

  }
}


export interface ILabel {
  id: number,
  inicio: Date,
  fim: Date,
  turno: number,
  numero: number,
  especial: boolean
}
