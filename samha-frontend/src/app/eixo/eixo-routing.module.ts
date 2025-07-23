import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {FormResolver} from '../guards/form-resolver';
import {EixoFormComponent} from './eixo-form/eixo-form.component';
import {LogComponent} from '../shared/log/log.component';
import {EixoComponent} from './eixo/eixo.component';


export const eixoRoutes: Routes = [
  {path: '', children: [
      {path: '', component: EixoComponent},
      {path: 'new', component: EixoFormComponent},
      {path: 'log', component: LogComponent},
      {path: ':entity', component: EixoFormComponent, resolve: {eixo: FormResolver}}
    ]}
]


@NgModule({
  imports: [RouterModule.forChild(eixoRoutes)],
  exports: [RouterModule]
})
export class EixoRoutingModule {}
