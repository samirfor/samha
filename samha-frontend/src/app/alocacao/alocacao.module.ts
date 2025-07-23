import {NgModule} from '@angular/core';
import {AlocacaoRoutingModule} from './alocacao-routing.module';
import {SharedModule} from "../shared/shared.module";
import { AlocacaoFormComponent } from './alocacao-form/alocacao-form.component';
import {MaterialModule} from "../shared/material/material.module";
import {CommonModule} from "@angular/common";
import { AlocacaoMainComponent } from './alocacao-main/alocacao-main.component';
import {MatDividerModule} from "@angular/material/divider";
import {MatRadioModule} from "@angular/material/radio";
import {DxButtonModule, DxPopupModule, DxScrollViewModule} from "devextreme-angular";
import {CargaHorariaModalComponent} from "./carga-horaria.modal/carga-horaria.modal.component";


@NgModule({
  declarations: [
    AlocacaoFormComponent,
    AlocacaoMainComponent,
    CargaHorariaModalComponent
  ],
  imports: [AlocacaoRoutingModule, SharedModule, MaterialModule, CommonModule, MatDividerModule, MatRadioModule, DxButtonModule, DxPopupModule, DxScrollViewModule]
})
export class AlocacaoModule {}
