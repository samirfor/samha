import {NgModule} from '@angular/core';
import {LabelRoutingModule} from './label-routing.module';
import {SharedModule} from "../shared/shared.module";
import {MaterialModule} from "../shared/material/material.module";
import {CommonModule} from "@angular/common";
import {MatDividerModule} from "@angular/material/divider";
import {MatRadioModule} from "@angular/material/radio";
import {DxButtonModule, DxDataGridModule, DxPopupModule, DxScrollViewModule} from "devextreme-angular";
import {LabelComponent} from "./label.component";


@NgModule({
  declarations: [
    LabelComponent
  ],
  imports: [LabelRoutingModule, SharedModule, MaterialModule, CommonModule, MatDividerModule, MatRadioModule, DxButtonModule, DxPopupModule, DxScrollViewModule, DxDataGridModule]
})
export class LabelModule {}
