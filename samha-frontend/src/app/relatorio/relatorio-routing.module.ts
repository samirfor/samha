import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RelatorioComponent} from "./relatorio/relatorio.component";
export const relatorioRoutes: Routes = [
  { path: '', component: RelatorioComponent}
]

@NgModule({
  imports: [RouterModule.forChild(relatorioRoutes)],
  exports: [RouterModule]
})
export class RelatorioRoutingModule {}
