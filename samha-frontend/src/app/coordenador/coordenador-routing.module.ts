import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {ProfessorFormComponent} from '../professor/professor-form/professor-form.component';
import {FormResolver} from '../guards/form-resolver';
import {LogComponent} from '../shared/log/log.component';
import {CoordenadorListComponent} from "./coordenador-list/coordenador-list.component";


export const coordenadorRoutes: Routes = [
  {path: '', children: [
      {path: '', component: CoordenadorListComponent},
      {path: 'log', component: LogComponent},
      {path: ':entity', component: ProfessorFormComponent, resolve: {professor: FormResolver}}
    ]}
]


@NgModule({
  imports: [RouterModule.forChild(coordenadorRoutes)],
  exports: [RouterModule]
})
export class CoordenadorRoutingModule {}
