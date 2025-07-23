import { Component, OnInit } from '@angular/core';
import {eixoColumns} from '../../meta-model/eixo';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'samha-eixo',
  templateUrl: './eixo.component.html',
  styleUrls: ['../../shared/table/table.component.css']
})
export class EixoComponent implements OnInit {
  eixoColumns = eixoColumns;

  constructor(private router: Router,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
  }

  goToLog() {
    this.router.navigate(['log'], {relativeTo: this.route});
  }
}
