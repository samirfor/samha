import {NgModule} from '@angular/core';
import {EixoRoutingModule} from './eixo-routing.module';
import { EixoFormComponent } from './eixo-form/eixo-form.component';
import {MaterialModule} from '../shared/material/material.module';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import { EixoComponent } from './eixo/eixo.component';
import {SharedModule} from '../shared/shared.module';
import {DxButtonModule} from "devextreme-angular";


@NgModule({
  declarations: [
    EixoFormComponent,
    EixoComponent
  ],
    imports: [
        EixoRoutingModule,
        MaterialModule,
        CommonModule,
        ReactiveFormsModule,
        SharedModule,
        DxButtonModule
    ]
})
export class EixoModule {}
