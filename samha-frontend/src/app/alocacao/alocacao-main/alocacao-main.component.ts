import {Component, EventEmitter, OnInit} from '@angular/core';
import {disciplinaColumns} from "../../meta-model/disciplina";
import {Notification, Observable, of} from "rxjs";
import {DataService} from "../../shared/service/data.service";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {Filter, QueryMirror} from "../../shared/query-mirror";
import {matrizColumns} from "../../meta-model/matriz-curricular";
import {catchError, first, tap} from "rxjs/operators";
import {PagedList} from "../../shared/paged-list";
import {professorColumns} from "../../meta-model/professor";
import {MatRadioChange} from "@angular/material/radio";
import {ActivatedRoute, Router} from "@angular/router";
import {alocacaoColumns} from "../../meta-model/alocacao";
import {NotificationService} from "../../shared/service/notification.service";
import {error} from "protractor";
import notify from "devextreme/ui/notify";

@Component({
  selector: 'samha-alocacao-main',
  templateUrl: './alocacao-main.component.html',
  styleUrls: ['./alocacao-main.component.css']
})
export class AlocacaoMainComponent implements OnInit {

  //Bloco 1
  public disciplinaColumns = disciplinaColumns;
  public disciplinaDisplayedColumns = [];
  public selectedDisciplinaRowIndex: number;
  public disciplinaDataSource$: Observable<any>;
  public cursoControl = new FormControl();
  public disciplinaForm: FormGroup;
  public matriz$: Observable<any>;
  public showPeriodo: boolean = true;
  public qtPeriodos: number = 1;
  public matrizControl = new FormControl();
  public showPopup = false;
  public cargaHoraria$: Observable<any>;
  //Bloco 2
  public eixoControl = new FormControl();
  public professorForm: FormGroup;
  public professorColumns = professorColumns;
  public selectedProfessorRowIndex: number;
  public professor$: Observable<any>;
  public searchText: string;
  public selectedProfessorRowIndexes: string[] = [];

  //Bloco 3
  public alocacaoForm: FormGroup;
  public alocacao$: Observable<any>;
  public alocacaoColumns = alocacaoColumns;
  public alocacaoDisplayedColumns = [];
  public selectedAlocacaoRowIndex: number;
  private disciplinaSelecionada: any;
  isVisible: any;


  constructor(private dataService: DataService,
              private formBuilder: FormBuilder,
              private router: Router,
              private route: ActivatedRoute,
              private notificationService: NotificationService,
              private notification: NotificationService) {
    this.disciplinaForm = this.formBuilder.group({
      periodo: [1],
      matriz: [null]
    });

    this.professorForm = this.formBuilder.group({
      eixo: [2],
      search: ['']
    });

    this.alocacaoForm = this.formBuilder.group({
      ano: [new Date().getFullYear()],
      semestre: [1]
    })
  }

  ngOnInit(): void {
    this.disciplinaDisplayedColumns.push('nome');
    this.setAlocacaoDisplayedColumns();
  }

  findColumnValue = (row, column): string => <string>column.split('.').reduce((acc, cur) => acc[cur], row);

  private getAlocacao$(matrizId: number): Observable<any> {
    let query = new QueryMirror('alocacao');
    let projections = alocacaoColumns.map(column => column.columnDef);
    query.selectList(projections);
    let filter = {
      and: {
        'ano': {equals: this.alocacaoForm.get('ano').value},
        'semestre': {equals: this.alocacaoForm.get('semestre').value},
        'disciplina.matriz.curso.id': {equals: this.cursoControl.value.id},
        'disciplina.matriz.id': {equals: matrizId},
        'disciplina.periodo': {equals: this.disciplinaForm.get('periodo').value}
      }
    }
    query.where(filter);

    return this.dataService.query(query);
  }

  compareFunction(o1: any, o2: any) {
    return (o1 != null && o2 != null && o1.id == o2.id);
  }

  highlightDisciplina(row) {
    this.disciplinaSelecionada = row;
    this.selectedDisciplinaRowIndex = row.id;
  }

  onCursoChange($event: any) {
    this.disciplinaForm.get('periodo').setValue('1');
    this.qtPeriodos = this.cursoControl.value.qtPeriodos;
    this.loadMatrizes(this.cursoControl.value.id);
  }

  onMatrizChanged() {
    this.onFilterChange();
    this.alocacao$ = this.getAlocacao$(this.matrizControl.value.id);
  }

  onFilterChange() {
    let query = new QueryMirror('disciplina');
    query.projections = disciplinaColumns.map(column => column.columnDef);
    query.orderBy('nome asc');
    query.where({
      and: {
        'matriz.id': {equals: this.matrizControl.value.id},
        'periodo': {equals: this.disciplinaForm.get('periodo').value}
      }
    });

    this.disciplinaDataSource$ = this.dataService.query(query).pipe(
      tap(next => next.listMap.sort((a, b) => a.nome > b.nome ? 1 : -1))
    );
  }

  onLoaded($event: any[]) {
    this.cursoControl.setValue($event[0]);
    this.qtPeriodos = this.cursoControl.value.qtPeriodos;
    this.loadMatrizes(this.cursoControl.value.id);
  }

  private loadMatrizes(id: number) {
    let query = new QueryMirror('matrizCurricular');
    let projections = matrizColumns.map(column => column.columnDef);
    let filter: Filter;
    filter = {
      and: {
        'curso.id': {equals: id}
      }
    }
    query.selectList(projections);
    query.where(filter);
    query.orderBy('ano desc');

    this.matriz$ = this.dataService.query(query).pipe(
      tap((matrizes: PagedList) => {
        this.matrizControl.setValue(matrizes.listMap[0]);
        this.onMatrizChanged();
      })
    );
  }

  defineDataSource(dataSource: PagedList) {
    return dataSource.listMap;
  }

  onEixoChange($event: any) {
    this.loadProfessores(this.eixoControl?.value);
  }

  onEixoLoaded($event: any) {
    this.eixoControl.setValue($event[0]);
    this.loadProfessores($event[0]);
  }

  private loadProfessores($eventElement: any) {
    let query = new QueryMirror('professor');
    query.projections = professorColumns.map(column => column.columnDef);
    query.orderBy('nome asc');
    let and = {
      'ativo': {equals: true}
    };
    if (this.professorForm.get('eixo').value == 1 && $eventElement?.id) {
      Object.assign(and, {'coordenadoria.eixo.id': {equals: $eventElement.id}});
    }

    if (this.searchText) {
      Object.assign(and, {'nome': {contains: this.searchText}});
    }

    if (Object.entries(and).length !== 0) {
      query.where({
        and: and
      });
    }


    this.professor$ = this.dataService.query(query);
  }

  highlightProfessor(row) {
    let index = this.selectedProfessorRowIndexes.indexOf(this.selectedProfessorRowIndexes.find(i => i == row.id));

    if (index == -1) {
      this.selectedProfessorRowIndexes.push(row.id);
    } else {
      this.selectedProfessorRowIndexes.splice(index, 1);
    }
  }

  highlightAlocacao(row) {
    this.selectedAlocacaoRowIndex = row.id;
  }

  onRadioChange($event: MatRadioChange) {
    this.onEixoLoaded([this.eixoControl.value]);
  }

  onSearchChange() {
    this.searchText = this.professorForm.get('search').value;
    this.loadProfessores(this.eixoControl?.value,);
  }

  new() {
    if (!this.verificarDados()) return;

    let alocacao = {
      "ano": this.alocacaoForm.get("ano").value,
      "semestre": this.alocacaoForm.get("semestre").value,
      "disciplina": {
        "id": this.selectedDisciplinaRowIndex
      },
      "professor1": {
        "id": this.selectedProfessorRowIndexes[0]
      }
    }

    if (this.selectedProfessorRowIndexes.length > 1) {
      Object.assign(alocacao, {
        "professor2": {
          "id": this.selectedProfessorRowIndexes[1]
        }
      })
    }

    this.dataService.save('alocacao', alocacao).subscribe(
      _ => {
        this.alocacao$ = this.getAlocacao$(this.matrizControl.value.id);
        this.notification.success('Alocação incluída com sucesso!');
        this.professorForm.get('search').setValue('');
        this.onSearchChange();
        this.selectedProfessorRowIndexes = [];
        this.selectedDisciplinaRowIndex = undefined;
      },
      (err) => this.notification.handleError(err));
  }

  delete() {
    if (this.selectedAlocacaoRowIndex != null) {
      this.dataService.delete("alocacao", this.selectedAlocacaoRowIndex).pipe(first())
        .subscribe(
          _ => {
            this.alocacao$ = this.getAlocacao$(this.matrizControl.value.id);
            this.selectedAlocacaoRowIndex = null;
            this.notification.success('Alocação excluída com sucesso!');
          },
          err => {
            this.notification.handleError(err);
          });
    } else {
      this.notification.error('Selecione uma alocação para ser excluída.');
    }
  }

  goToLog() {
    this.router.navigate(['log'], {relativeTo: this.route});
  }

  private setAlocacaoDisplayedColumns() {
    this.alocacaoColumns.forEach(column => {
      if (column.visible && column.columnDef !== 'professor1.nome' && column.columnDef !== 'professor2.nome') {
        this.alocacaoDisplayedColumns.push(column.columnDef);
      }
    });
    this.alocacaoDisplayedColumns.push('encurtadoProfessor1');
    this.alocacaoDisplayedColumns.push('encurtadoProfessor2');
    this.alocacaoDisplayedColumns.push('completa');
  }

  public onPeriodoChange() {
    this.onFilterChange();
    this.alocacao$ = this.getAlocacao$(this.matrizControl.value.id);
  }

  onFilterAlocacaoChange() {
    this.alocacao$ = this.getAlocacao$(this.matrizControl.value.id);
  }

  public highlightProfessorRow(row): boolean {
    return this.selectedProfessorRowIndexes.find(i => i == row.id) ? true : false;
  }

  private verificarDados() {
    if (this.selectedDisciplinaRowIndex == null || this.selectedDisciplinaRowIndex == undefined
      || this.selectedProfessorRowIndexes.length == 0) {
      this.notification.error('Selecione uma disciplina e pelo menos 1 (Um) professor!');
      return false;
    }

    if (!(this.disciplinaSelecionada.tipo == 'ESPECIAL') && this.selectedProfessorRowIndexes.length > 1) {
      this.notification.error('Não é possível alocar mais de um professor para uma disciplina não-especial!');
      return false;
    }

    if (this.selectedProfessorRowIndexes.length > 2) {
      this.notification.error('Selecione no mínimo 1 (Um) e no máximo 2 (Dois) professores!');
      return false;
    }

    return true;
  }

  onCargaHorariaClicked() {
    if (!this.eixoControl.value?.id) {
      this.notification.error('Selecione um eixo!')
      return;
    }
    this.cargaHoraria$ = this.dataService.post('alocacao/obter-carga-horaria', {
      ano: this.alocacaoForm.get('ano').value,
      semestre: this.alocacaoForm.get('semestre').value,
      eixoId: this.eixoControl.value.id
    })
    this.showPopup = true
  }

  onClearButtonClicked() {
    this.professorForm.get('search').setValue('');
    this.onSearchChange();
  }
}
