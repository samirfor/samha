import { Component, OnInit } from '@angular/core';
import {usuarioColumns} from "../../meta-model/usuario";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'samha-usuario-list',
  templateUrl: './usuario-list.component.html'
})
export class UsuarioListComponent {
  public readonly usuarioColumns = usuarioColumns;

  constructor(private router: Router,
              private route: ActivatedRoute) {
  }
  goToLog() {
    this.router.navigate(['log'], {relativeTo: this.route});
  }
}
