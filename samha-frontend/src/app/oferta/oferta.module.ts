import {NgModule} from "@angular/core";
import {MaterialModule} from "../shared/material/material.module";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../shared/shared.module";
import {OfertaRoutingModule} from "./oferta-routing.module";
import {OfertaComponent} from "./oferta-main/oferta.component";
import {DragDropModule} from "@angular/cdk/drag-drop";
import {
    DxButtonModule,
    DxDraggableModule, DxPopupModule,
    DxProgressBarModule,
    DxSchedulerModule,
    DxScrollViewModule
} from "devextreme-angular";
import { OfertaGridComponent } from './oferta-grid/oferta-grid.component';
import { ProfessorGridComponent } from './professor-grid/professor-grid.component';
import {CdkAccordionModule} from "@angular/cdk/accordion";
import { OfertaLogComponent } from './oferta-log/oferta-log.component';
import { AulaLogComponent } from './aula-log/aula-log.component';
@NgModule({
  declarations: [
    OfertaComponent,
    OfertaGridComponent,
    ProfessorGridComponent,
    OfertaLogComponent,
    AulaLogComponent
  ],
    imports: [
        OfertaRoutingModule,
        MaterialModule,
        CommonModule,
        SharedModule,
        DragDropModule,
        DxSchedulerModule,
        DxScrollViewModule,
        DxDraggableModule,
        DxButtonModule,
        CdkAccordionModule,
        DxProgressBarModule,
        DxPopupModule
    ]
})
export class OfertaModule {}
