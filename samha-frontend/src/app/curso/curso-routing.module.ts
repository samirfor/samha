import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {CursoFormComponent} from './curso-form/curso-form.component';
import {FormResolver} from '../guards/form-resolver';
import {CursoComponent} from './curso/curso.component';
import {LogComponent} from '../shared/log/log.component';

export const cursoRoutes: Routes = [
  {path: '', children: [
      {path: '', component: CursoComponent},
      {path: 'new', component: CursoFormComponent},
      {path: 'log', component: LogComponent},
      {path: ':entity', component: CursoFormComponent, resolve: {curso: FormResolver}},

    ]}
]

@NgModule({
  imports: [RouterModule.forChild(cursoRoutes)],
  exports: [RouterModule]
})
export class CursoRoutingModule {}
