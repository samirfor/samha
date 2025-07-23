import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {FormResolver} from '../guards/form-resolver';
import {MatrizFormComponent} from "./matriz-form/matriz-form.component";
import {LogComponent} from '../shared/log/log.component';
import {MatrizCurricularComponent} from './matriz-curricular/matriz-curricular.component';


export const matrizRoutes: Routes = [
  {path: '', children: [
      {path: '', component: MatrizCurricularComponent},
      {path: 'new', component: MatrizFormComponent},
      {path: 'log', component: LogComponent},
      {path: ':entity', component: MatrizFormComponent, resolve: {matriz: FormResolver}}
    ]}
]


@NgModule({
  imports: [RouterModule.forChild(matrizRoutes)],
  exports: [RouterModule]
})
export class MatrizRoutingModule {}
