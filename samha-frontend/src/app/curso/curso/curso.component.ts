import { Component, OnInit } from '@angular/core';
import {cursoColumns} from '../../meta-model/curso';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'samha-curso',
  templateUrl: './curso.component.html',
  styleUrls: ['../../shared/table/table.component.css']
})
export class CursoComponent implements OnInit {
  cursoColumns = cursoColumns;

  constructor(private router: Router,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
  }

  goToLog() {
    this.router.navigate(['log'], {relativeTo: this.route});
  }
}
