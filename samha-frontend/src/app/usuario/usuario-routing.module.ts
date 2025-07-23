import {RouterModule, Routes} from '@angular/router';
import {UsuarioFormComponent} from './usuario-form/usuario-form.component';
import {NgModule} from '@angular/core';
import {FormResolver} from '../guards/form-resolver';
import {LogComponent} from '../shared/log/log.component';
import {UsuarioListComponent} from "./usuario-list/usuario-list.component";


export const usuarioRoutes: Routes = [
  {path: '', children: [
      {path: '', component: UsuarioListComponent},
      {path: 'new', component: UsuarioFormComponent},
      {path: 'log', component: LogComponent},
      {path: ':entity', component: UsuarioFormComponent, resolve: {usuario: FormResolver}}
    ]}

]


@NgModule({
  imports: [RouterModule.forChild(usuarioRoutes)],
  exports: [RouterModule]
})
export class UsuarioRoutingModule {}
