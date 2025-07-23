import {NgModule} from '@angular/core';
import {UsuarioFormComponent} from './usuario-form/usuario-form.component';
import {UsuarioRoutingModule} from './usuario-routing.module';
import {MaterialModule} from '../shared/material/material.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import { UsuarioListComponent } from './usuario-list/usuario-list.component';
import {DxButtonModule} from "devextreme-angular";
import {SharedModule} from "../shared/shared.module";


@NgModule({
  declarations: [UsuarioFormComponent, UsuarioListComponent],
  imports: [
    UsuarioRoutingModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule,
    CommonModule,
    DxButtonModule,
    SharedModule
  ],
  exports: []
})
export class UsuarioModule {}
