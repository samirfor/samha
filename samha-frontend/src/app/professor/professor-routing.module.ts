import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ProfessorFormComponent} from './professor-form/professor-form.component';
import {CanDeactivateGuard} from '../guards/can-deactivate-guard';
import {FormResolver} from '../guards/form-resolver';
import {RestricaoComponent} from './restricao/restricao.component';
import {LogComponent} from '../shared/log/log.component';
import {ProfessorComponent} from './professor/professor.component';
import {ProfessorLogComponent} from './professor-log/professor-log.component';

export const professorRoutes: Routes = [
  { path: '', children: [
      {path: '', component: ProfessorComponent},
      {path: 'new', component: ProfessorFormComponent, canDeactivate: [CanDeactivateGuard]},
      {path: 'log', component: ProfessorLogComponent},
      {path: ':entity', children:[
          {path: '', component: ProfessorFormComponent, resolve: {professor: FormResolver}},
          {path :'restricaoProfessor', children: [
              // {path: '', pathMatch: 'full', },
              {path: 'new', component: RestricaoComponent},
              {path: 'log', component: LogComponent},
              {path: ':target', component: RestricaoComponent, resolve: {restricao: FormResolver}}
            ]}
        ]},
    ]}
]

@NgModule({
  imports: [RouterModule.forChild(professorRoutes)],
  exports: [RouterModule]
})
export class ProfessorRoutingModule{}
