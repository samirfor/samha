import {RouterModule, Routes} from '@angular/router';
import {CoordenadoriaFormComponent} from './coordenadoria-form/coordenadoria-form.component';
import {FormResolver} from '../guards/form-resolver';
import {NgModule} from '@angular/core';
import {LogComponent} from '../shared/log/log.component';
import {CoordenadoriaComponent} from './coordenadoria/coordenadoria.component';

export const cooordenadoriaRoutes: Routes = [
  {path: '', children: [
      {path: '', component: CoordenadoriaComponent},
      {path: 'new', component: CoordenadoriaFormComponent},
      {path: 'log', component: LogComponent},
      {path: ':entity', component: CoordenadoriaFormComponent, resolve: {coord: FormResolver}}
    ]}
]

@NgModule({
  imports: [RouterModule.forChild(cooordenadoriaRoutes)],
  exports: [RouterModule]
})
export class CoordenadoriaRoutingModule {}
