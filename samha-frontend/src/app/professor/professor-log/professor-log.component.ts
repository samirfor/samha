import { Component, OnInit } from '@angular/core';
import {professorLogColumns} from '../../meta-model/professor';
import {servidorLogColumns} from '../../meta-model/servidor';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'samha-professor-log',
  templateUrl: './professor-log.component.html'
})
export class ProfessorLogComponent implements OnInit {
  professorLogColumns = professorLogColumns;
  servidorLogColumns = servidorLogColumns;

  constructor(private router: Router,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
  }

  goBack() {
    this.router.navigate(['../'], {relativeTo: this.route});
  }
}
