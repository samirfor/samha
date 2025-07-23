import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {disciplinaColumns} from "../../meta-model/disciplina";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {Filter, QueryMirror} from "../../shared/query-mirror";
import {matrizColumns} from "../../meta-model/matriz-curricular";
import {DataService} from "../../shared/service/data.service";
import {MatOptionSelectionChange} from "@angular/material/core";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'samha-disciplina-list',
  templateUrl: './disciplina-list.component.html',
  styleUrls: ['./disciplina-list.component.css']
})
export class DisciplinaListComponent implements OnInit {
  @Output() selectedRow = new EventEmitter<number>();
  public showFiltro: boolean = false;
  disciplinaColumns = disciplinaColumns;
  matriz$: Observable<any>;
  form: FormGroup;
  cursoControl = new FormControl();
  filter: Filter;
  filterChange = new EventEmitter<Filter>();
  qtPeriodos: number = 1;
  showPeriodo: boolean = false;
  compareFunction(o1: string, o2: string) {
    return (o1 != null && o2 != null && o1.toUpperCase() == o2.toUpperCase());
  }


  constructor(private formBuilder: FormBuilder,
              private dataService: DataService,
              private router: Router,
              private route: ActivatedRoute) {
    this.form = this.formBuilder.group({
      matriz: [null],
      periodo: ['1', [Validators.maxLength(1)]]
    });
  }



  ngOnInit(): void {
    this.form.addControl('cursoControl', this.cursoControl);
  }


  onCursoChange($event) {
    this.form.get('periodo').setValue('1');
    this.qtPeriodos = this.cursoControl.value.qtPeriodos;
    this.showPeriodo = false;
    this.setMatriz(this.cursoControl.value.id);
  }

  private setMatriz(id: number) {
    let query = new QueryMirror('matrizCurricular');
    let projections = matrizColumns.map(column => column.columnDef);
    let filter: Filter;
    filter = {
      and: {
        'curso.id': {equals: id}
      }
    }
    query.selectList(projections);
    query.where(filter)

    this.matriz$ = this.dataService.query(query);
  }



  onFilterChange(clearFilter: boolean) {
    if(this.form.get('matriz').value && !clearFilter) {
      this.filterChange.emit({
        and: {
          'matriz.id': {equals: this.form.get('matriz').value.id},
          'periodo': {equals: this.form.get('periodo').value}
        }
      });
    }else {
      this.filterChange.emit({});
    }
  }

  onMatrizChanged($event: MatOptionSelectionChange) {
    this.form.get('matriz').setValue($event.source.value);
    this.showPeriodo = true;
    this.onFilterChange(false);
  }

  clearFilters() {
    this.cursoControl.setValue(null);
    this.form.get('matriz').setValue(null);
    this.showPeriodo = false;
    this.onFilterChange(true);
  }

  onSelectedRow($event: number) {
    this.selectedRow.emit($event);
  }

  goToLog() {
    this.router.navigate(['log'], {relativeTo: this.route});
  }
}
