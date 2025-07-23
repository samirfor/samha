import { Component, OnInit } from '@angular/core';
import {coordenadoriaColumns} from '../../meta-model/coordenadoria';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'samha-coordenadoria',
  templateUrl: './coordenadoria.component.html',
  styleUrls: ['../../shared/table/table.component.css']
})
export class CoordenadoriaComponent implements OnInit {
  coordenadoriaColumns = coordenadoriaColumns;

  constructor(private router: Router,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
  }

  goToLog() {
    this.router.navigate(['log'], {relativeTo: this.route});
  }
}
