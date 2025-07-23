import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {AlocacaoFormComponent} from "./alocacao-form/alocacao-form.component";
import {AlocacaoMainComponent} from "./alocacao-main/alocacao-main.component";
import {LogComponent} from "../shared/log/log.component";


export const alocacaoRoutes: Routes = [
  {path: '', children: [
      {path: '', component: AlocacaoMainComponent},
      {path: 'new', component: AlocacaoFormComponent},
      {path: 'log', component: LogComponent}
    ]
  }
]


@NgModule({
  imports: [RouterModule.forChild(alocacaoRoutes)],
  exports: [RouterModule]
})
export class AlocacaoRoutingModule {}
