import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {professorColumns} from '../../meta-model/professor';

@Component({
  selector: 'samha-professor',
  templateUrl: './professor.component.html',
  styleUrls: ['../../shared/table/table.component.css']
})
export class ProfessorComponent implements OnInit {
  professorColumns = professorColumns;

  constructor(private router: Router,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
  }

  goToLog() {
    this.router.navigate(['log'], {relativeTo: this.route});
  }
}
