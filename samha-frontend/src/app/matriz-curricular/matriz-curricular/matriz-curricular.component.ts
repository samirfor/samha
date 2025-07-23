import { Component, OnInit } from '@angular/core';
import {matrizColumns} from '../../meta-model/matriz-curricular';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'samha-matriz-curricular',
  templateUrl: './matriz-curricular.component.html',
  styleUrls: ['../../shared/table/table.component.css']
})
export class MatrizCurricularComponent implements OnInit {
  matrizColumns = matrizColumns;

  constructor(private router: Router,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
  }

  goToLog() {
    this.router.navigate(['log'], {relativeTo: this.route});
  }
}
