import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {OfertaComponent} from "./oferta-main/oferta.component";
import {OfertaGridComponent} from "./oferta-grid/oferta-grid.component";
import {OfertaLogComponent} from "./oferta-log/oferta-log.component";
import {AulaLogComponent} from "./aula-log/aula-log.component";

export const ofertaRoutes: Routes = [
  {path: '', children: [
      {path: '', component: OfertaComponent},
      {path: 'logOferta', component: OfertaLogComponent},
      {path: 'logAulas', component: AulaLogComponent},
      {path: 'grid', component: OfertaGridComponent}
    ]}
]


@NgModule({
  imports: [RouterModule.forChild(ofertaRoutes)],
  exports: [RouterModule]
})
export class OfertaRoutingModule {}
