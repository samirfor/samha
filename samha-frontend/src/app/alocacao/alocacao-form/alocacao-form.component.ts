import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Observable, of} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {DataService} from "../../shared/service/data.service";
import {ActivatedRoute} from "@angular/router";
import {ComponentType} from "@angular/cdk/overlay";
import {disciplinaColumns} from "../../meta-model/disciplina";
import {DialogData} from "../../meta-model/dialog-data";
import {DisciplinaDialogComponent} from "../../disciplina/disciplina-dialog/disciplina-dialog.component";

@Component({
  selector: 'samha-alocacao-new',
  templateUrl: './alocacao-form.component.html'
})
export class AlocacaoFormComponent implements OnInit {
  form: FormGroup;
  disciplina$: Observable<any>;
  disciplinaData = {columns: disciplinaColumns, resource: 'disciplina', toolbarHeader: 'Disciplinas'};
  professor$: Observable<any>;

  constructor(private formBuilder: FormBuilder,
              public dialog: MatDialog,
              private dataService: DataService,
              private route: ActivatedRoute) {
    this.form = this.formBuilder.group({
      disciplina_id: [null]
    })
  }

  ngOnInit(): void {
  }

  setDisciplinaValue(id) {
    this.form.get('disciplina_id').setValue(id);
    this.form.get('disciplina_id').disable({onlySelf: true});
    return id;
  }

  salvar() {

  }

  getDisciplinaComponent(): ComponentType<any> {
    return DisciplinaDialogComponent
  }

  openDialog(component: ComponentType<any>, data: DialogData) {
    const dialogRef = this.dialog.open(component, {
      panelClass: 'custom-dialog-container',
      data: data,
    });

    dialogRef.afterClosed().subscribe(entityId => {
      if (entityId) {
        if (data.resource == 'disciplina') {
          this.disciplina$ = this.dataService.get(data.resource, entityId);
        } else if (data.resource == 'professor') {
          this.professor$ = this.dataService.get(data.resource, entityId);
        }
      } else {
        this.professor$ = of({
          id: -1,
          nome: ''
        });
      }
    });
  }
}
