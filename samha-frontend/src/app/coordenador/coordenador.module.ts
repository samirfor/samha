import {NgModule} from '@angular/core';
import {CoordenadorRoutingModule} from './coordenador-routing.module';
import { CoordenadorListComponent } from './coordenador-list/coordenador-list.component';
import {DxButtonModule} from "devextreme-angular";
import {MatToolbarModule} from "@angular/material/toolbar";
import {SharedModule} from "../shared/shared.module";


@NgModule({
  declarations: [
    CoordenadorListComponent
  ],
  imports: [CoordenadorRoutingModule, DxButtonModule, MatToolbarModule, SharedModule]
})
export class CoordenadorModule {}
