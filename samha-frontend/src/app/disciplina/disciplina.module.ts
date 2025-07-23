import {NgModule} from '@angular/core';
import {DisciplinaRoutingModule} from './disciplina-routing.module';
import {MaterialModule} from '../shared/material/material.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {SharedModule} from '../shared/shared.module';
import {DisciplinaFormComponent} from './disciplina-form/disciplina-form.component';
import { DisciplinaDialogComponent } from './disciplina-dialog/disciplina-dialog.component';
import { DisciplinaListComponent } from './disciplina-list/disciplina-list.component';
import {DxButtonModule} from "devextreme-angular";

@NgModule({
    imports: [
        MaterialModule,
        ReactiveFormsModule,
        FormsModule,
        DisciplinaRoutingModule,
        CommonModule,
        SharedModule,
        DxButtonModule,

    ],
  declarations: [
    DisciplinaFormComponent,
    DisciplinaDialogComponent,
    DisciplinaListComponent
  ]
})
export class DisciplinaModule {}
