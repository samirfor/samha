import {Component, OnDestroy, OnInit} from '@angular/core';
import {TableComponent} from "../../shared/table/table.component";
import {DataService} from "../../shared/service/data.service";
import {FormBuilder, FormControl} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {DomSanitizer} from "@angular/platform-browser";
import {Filter, Predicate, QueryMirror} from "../../shared/query-mirror";
import {Observable, of} from "rxjs";
import {Page, PagedList} from "../../shared/paged-list";
import {catchError, first} from "rxjs/operators";
import {NotificationService} from "../../shared/service/notification.service";
@Component({
  selector: 'samha-turma-list',
  templateUrl: './turma-list.component.html',
  styleUrls: ['../../shared/table/table.component.css']
})
export class TurmaListComponent extends TableComponent implements OnInit {
  cursoControl = new FormControl();
  ativasControl = new FormControl();
  cursoFilter: Filter = undefined;
  ativasFilter: Filter = {
    and: {
      'ativa': {equals: true}
    }
  };
  showFiltro: boolean = false;

  constructor(protected dataService: DataService,
              protected router: Router,
              protected route: ActivatedRoute,
              protected dialog: MatDialog,
              protected sanitizer: DomSanitizer,
              protected notification: NotificationService) {
    super(dataService, router, route, dialog, sanitizer, notification);
  }

  ngOnInit(): void {
    if(this.resource === undefined && this.columns === undefined) {
      this.setParametersByUrl();
    }
    this.ativasControl.setValue(true);
    this.defineDisplayedColumns();
    this.dataSource$ = this.loadTableData();
  }

  loadTableData(filter: Predicate[] = []): Observable<PagedList> {
    const query = new QueryMirror(this.resource);
    let projections: string[] = [];
    let page: Page = {
      size: this.maxRows,
      skip: this.calculateSkip()
    };
    let whereList: Filter[] = [];
    if(filter.length > 0){
      whereList.push({
        and: {
          or: filter
        }
      });
    }
    whereList.push(this.ativasFilter);
    if(this.cursoFilter !== undefined) whereList.push(this.cursoFilter);
    query.pageItem(page);
    this.columns.forEach(column => projections.push(column.columnDef));
    query.selectList(projections);
    if(whereList.length > 0) query.whereList(...whereList);
    if (this.orderBy !== '') {
      query.orderBy(this.orderBy);
    }
    return this.dataService.query(query).pipe(
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

  goToLog() {
    this.router.navigate(['log'], {relativeTo: this.route});
  }

  atualizarTurmasAtivas() {
    this.dataService.post('turma/atualizarTurmas', null).pipe(first()).subscribe(
      _ => {
        this.notification.success('Turmas atualizadas com sucesso!');
        this.onAtivasChange();
      },
       error => {
        this.notification.error('Não foi possível atualizar as turmas!');
        throw error;
       }
    );
  }

  onAtivasChange() {
    if(this.ativasControl.value){
      this.ativasFilter = {
        and: {
          'ativa': {equals: this.ativasControl.value}
        }
      }
    }else{
      this.ativasFilter = {
        and: {
          'ativa': {in: [true, false]}
        }
      }
    }
    this.dataSource$ = this.loadTableData();
  }

  onCursoChange() {
    this.cursoFilter = {
      and: {
        'matriz.curso.id': {equals: this.cursoControl.value.id}
      }
    }

    this.dataSource$ = this.loadTableData();
  }

  clearCursoFiltro() {
    this.cursoFilter = undefined;
    this.cursoControl.setValue('');

    this.dataSource$ = this.loadTableData();
  }
}
