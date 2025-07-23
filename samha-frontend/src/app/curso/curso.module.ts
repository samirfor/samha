import {NgModule} from '@angular/core';
import {CursoRoutingModule} from './curso-routing.module';
import { CursoFormComponent } from './curso-form/curso-form.component';
import {MaterialModule} from '../shared/material/material.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {SharedModule} from '../shared/shared.module';
import { CursoComponent } from './curso/curso.component';
import {DxButtonModule} from "devextreme-angular";

@NgModule({
    imports: [
        MaterialModule,
        ReactiveFormsModule,
        FormsModule,
        CursoRoutingModule,
        CommonModule,
        SharedModule,
        DxButtonModule,

    ],
  declarations: [
    CursoFormComponent,
    CursoComponent
  ]
})
export class CursoModule {}
