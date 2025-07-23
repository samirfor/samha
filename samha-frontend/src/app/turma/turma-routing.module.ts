import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TurmaListComponent} from './turma-list/turma-list.component';
import {TurmaFormComponent} from './turma-form/turma-form.component';
import {FormResolver} from '../guards/form-resolver';
import {LogComponent} from '../shared/log/log.component';

export const turmaRoutes: Routes = [
  {path: '', children: [
      {path: '', component: TurmaListComponent},
      {path: 'new', component: TurmaFormComponent},
      {path: 'log', component: LogComponent},
      {path: ':entity', component: TurmaFormComponent, resolve: {turma: FormResolver}}
    ]}
]

@NgModule({
  imports: [RouterModule.forChild(turmaRoutes)],
  exports: [RouterModule]
})
export class TurmaRoutingModule {}
