import {NgModule} from '@angular/core';
import {TurmaRoutingModule} from './turma-routing.module';
import {SharedModule} from '../shared/shared.module';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MaterialModule} from '../shared/material/material.module';
import { TurmaListComponent } from './turma-list/turma-list.component';
import { TurmaFormComponent } from './turma-form/turma-form.component';
import {DxButtonModule, DxTextBoxModule} from "devextreme-angular";

@NgModule({
  declarations: [
    TurmaListComponent,
    TurmaFormComponent
  ],
    imports: [
        CommonModule,
        TurmaRoutingModule,
        FormsModule,
        MaterialModule,
        ReactiveFormsModule,
        SharedModule,
        DxTextBoxModule,
        DxButtonModule
    ],
})
export class TurmaModule {}
