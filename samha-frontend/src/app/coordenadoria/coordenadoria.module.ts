import {NgModule} from '@angular/core';
import {CoordenadoriaRoutingModule} from './coordenadoria-routing.module';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SharedModule} from '../shared/shared.module';
import {MaterialModule} from '../shared/material/material.module';
import {CoordenadoriaFormComponent} from './coordenadoria-form/coordenadoria-form.component';
import { CoordenadoriaComponent } from './coordenadoria/coordenadoria.component';
import {DxButtonModule} from "devextreme-angular";

@NgModule({
  declarations:[
    CoordenadoriaFormComponent,
    CoordenadoriaComponent
  ],
    imports: [
        CoordenadoriaRoutingModule,
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        SharedModule,
        MaterialModule,
        DxButtonModule
    ]
})
export class CoordenadoriaModule {}
