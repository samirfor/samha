import {Component, Input, OnInit} from '@angular/core';
import {TableComponent} from '../table/table.component';
import {DataService} from '../service/data.service';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {cursoLogColumns} from '../../meta-model/curso';
import {Filter, Predicate, QueryMirror} from '../query-mirror';
import {Page, PagedList} from '../paged-list';
import {catchError} from 'rxjs/operators';
import {Observable, of} from 'rxjs';
import {DomSanitizer} from '@angular/platform-browser';
import {professorColumns, professorLogColumns} from '../../meta-model/professor';
import {disciplinaLogColumns} from '../../meta-model/disciplina';
import {turmaLogColumns} from '../../meta-model/turma';
import {usuarioColumns, usuarioLogColumns} from '../../meta-model/usuario';
import {coordenadoriaLogColumns} from '../../meta-model/coordenadoria';
import {eixoLogColumns} from '../../meta-model/eixo';
import {matrizLogColumns} from '../../meta-model/matriz-curricular';
import {alocacaoLogColumns} from "../../meta-model/alocacao";
import {NotificationService} from "../service/notification.service";
import {not} from "rxjs/internal-compatibility";
import {ofertaLogColumns} from "../../meta-model/oferta";
import {labelLogColumns} from "../../meta-model/label";

@Component({
  selector: 'samha-log',
  templateUrl: './log.component.html',
  styleUrls: ['../table/table.component.css']
})
export class LogComponent extends TableComponent implements OnInit {
  @Input() ativarToolbar = true;

  constructor(dataService: DataService,
              router: Router,
              route: ActivatedRoute,
              dialog: MatDialog,
              sanitizer: DomSanitizer,
              notification: NotificationService) {
    super(dataService, router, route, dialog, sanitizer, notification);
  }

  ngOnInit(): void {
    if(this.resource === undefined && this.columns === undefined) {
      this.setParametersByUrl();
    }
    this.defineDisplayedColumns();
    this.displayedColumns.pop(); //dropa coluna actions para nao precisar sobrescrever o método
    this.orderBy = 'modifiedDate desc';
    this.dataSource$ = this.loadTableData();
  }


  loadTableData(filter: Predicate[] = []): Observable<PagedList> {
    const query = new QueryMirror(this.resource);
    let projections: string[] = [];
    let page: Page = {
      size: this.maxRows,
      skip: this.calculateSkip()
    };
    let orFilter: Filter = {
      and: {
        or: filter
      }
    };
    query.pageItem(page);
    this.columns.forEach(column => projections.push(column.columnDef));
    query.selectList(projections);
    if (filter.length > 0) {
      query.where(orFilter);
    }
    if (this.orderBy !== '') {
      query.orderBy(this.orderBy);
    }
    return this.dataService.queryLog(query).pipe(
      catchError(err => {
        let empty = {
          listMap: [],
          page: {
            size: 0,
            skip: 0,
            totalItems: 0
          }
        };
        this.notification.handleError(err)
        return of(new PagedList(empty));
      })
    );
  }

  setParametersByUrl() {
    this.resource = this.router.url.split('/')[1];
    this.defineColumns();
  }

  defineColumns() {
    switch (this.resource) {
      case 'professor':
        this.columns = professorLogColumns;
        break;
      case 'coordenador':
        this.columns = professorColumns;
        break;
      case 'disciplina':
        this.toolbarHeader = 'Disciplinas';
        this.columns = disciplinaLogColumns;
        break;
      case 'curso':
        this.toolbarHeader = 'Cursos';
        this.columns = cursoLogColumns;
        break;
      case 'oferta':
        this.columns = ofertaLogColumns;
        break;
      case 'turma':
        this.toolbarHeader = 'Turmas';
        this.columns = turmaLogColumns;
        break;
      case 'usuario':
        this.toolbarHeader = 'Usuários';
        this.columns = usuarioLogColumns;
        break;
      case 'coordenadoria':
        this.toolbarHeader = 'Coordenadorias';
        this.columns = coordenadoriaLogColumns;
        break;
      case 'eixo':
        this.toolbarHeader = 'Eixos';
        this.columns = eixoLogColumns;
        break;
      case 'matrizCurricular':
        this.toolbarHeader = 'Matrizes Curriculares';
        this.columns = matrizLogColumns;
        break;
      case 'alocacao':
        this.toolbarHeader = 'Alocações';
        this.columns = alocacaoLogColumns;
        break;
      case 'label':
        this.toolbarHeader = 'Label';
        this.columns = labelLogColumns;
    }
  }

  goBack() {
    this.router.navigate(['../'], {relativeTo: this.route});
  }
}
