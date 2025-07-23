import {NgModule} from '@angular/core';
import {ProfessorFormComponent} from './professor-form/professor-form.component';
import {ProfessorRoutingModule} from './professor-routing.module';
import {SharedModule} from '../shared/shared.module';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { RestricaoComponent } from './restricao/restricao.component';
import { RestricaoListComponent } from './restricao/restricao-list/restricao-list.component';
import {MaterialModule} from '../shared/material/material.module';
import { ProfessorComponent } from './professor/professor.component';
import { ProfessorLogComponent } from './professor-log/professor-log.component';
import {DxButtonModule} from "devextreme-angular";

@NgModule({
    declarations: [ProfessorFormComponent, RestricaoComponent, RestricaoListComponent, ProfessorComponent, ProfessorLogComponent],
    imports: [
        CommonModule,
        ProfessorRoutingModule,
        FormsModule,
        MaterialModule,
        ReactiveFormsModule,
        SharedModule,
        DxButtonModule
    ],
    exports: [
        ProfessorComponent
    ]
})
export class ProfessorModule{

}
