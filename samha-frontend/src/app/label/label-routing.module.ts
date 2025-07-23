import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {LogComponent} from "../shared/log/log.component";
import {LabelComponent} from "./label.component";


export const labelRoutes: Routes = [
  {path: '', children: [
      {path: '', component: LabelComponent},
      {path: 'log', component: LogComponent}
    ]
  }
]


@NgModule({
  imports: [RouterModule.forChild(labelRoutes)],
  exports: [RouterModule]
})
export class LabelRoutingModule {}
