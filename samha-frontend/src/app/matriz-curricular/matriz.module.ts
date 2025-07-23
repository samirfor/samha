import {NgModule} from '@angular/core';
import {MatrizRoutingModule} from './matriz-routing.module';
import {MaterialModule} from '../shared/material/material.module';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {MatrizFormComponent} from "./matriz-form/matriz-form.component";
import {SharedModule} from "../shared/shared.module";
import { MatrizCurricularComponent } from './matriz-curricular/matriz-curricular.component';
import {DxButtonModule} from "devextreme-angular";


@NgModule({
  declarations: [
    MatrizFormComponent,
    MatrizCurricularComponent
  ],
    imports: [
        MatrizRoutingModule,
        MaterialModule,
        CommonModule,
        ReactiveFormsModule,
        SharedModule,
        DxButtonModule
    ]
})
export class MatrizModule {}
