import { Component } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {aulaLogColumns} from "../../meta-model/aula";

@Component({
  selector: 'samha-aula-log',
  templateUrl: './aula-log.component.html'
})
export class AulaLogComponent{
  public readonly aulaLogColumns = aulaLogColumns;
  public showLegenda: boolean = false;
  constructor(private router: Router,
              private route: ActivatedRoute) { }
  goBack = () => this.router.navigate(['../'], {relativeTo: this.route});


}
