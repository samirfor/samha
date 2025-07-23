import {Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {DataService} from "../../shared/service/data.service";
import {Observable, range, Subscription} from "rxjs";
import {PagedList} from "../../shared/paged-list";
import {Filter, QueryMirror} from "../../shared/query-mirror";
import {first, map, tap, toArray} from "rxjs/operators";
import {FormControl} from "@angular/forms";
import {error} from "protractor";
import {NotificationService} from "../../shared/service/notification.service";
import {ILabel} from "../../label/label.component";

@Component({
  selector: 'samha-professor-grid',
  templateUrl: './professor-grid.component.html',
  styleUrls: ['../oferta-grid/oferta-grid.component.css']
})
export class ProfessorGridComponent implements OnInit, OnChanges, OnDestroy {
  @Input() public alocacao: any;
  @Input() public ano: string;
  @Input() public semestre: string;
  @Input() public oferta: any;
  public aulasConflitantes: any[] = [];
  public aulas$: Observable<PagedList>;
  public selectionControl: FormControl = new FormControl(1);
  public labels: ILabel[] = [];
  private aulasProfessor: PagedList;
  private selectionSub: Subscription;


  constructor(private dataService: DataService,
              private notificationService: NotificationService) {
    this.selectionSub = this.selectionControl.valueChanges.subscribe(next => {
      if(next == 1) {

      }
    })
  }

  ngOnInit() {
    this.dataService.getAll('label').pipe(first()).subscribe(
      (next: ILabel[]) => {
          this.labels = next.sort((a, b) => a.numero > b.numero ? 1 : -1);
      }, error => this.notificationService.handleError(error)

    )
  }

  getLabel(label: ILabel) {
    return label?.inicio.toString().substring(0, 5);
  }

  ngOnChanges(changes: SimpleChanges) {
    let orProfessor2Predicate: Filter;
    if(changes?.alocacao?.currentValue) {
      if (changes?.alocacao?.currentValue.disciplina.tipo === 'ESPECIAL' && changes.alocacao.currentValue.professor2?.id) {
        orProfessor2Predicate = {
          or: {
            'alocacao.professor1.id': {equals: this.alocacao?.professor2?.id},
            'alocacao.professor2.id': {equals: this.alocacao?.professor2?.id}
          }
        }
        this.selectionControl.enable();
      } else {
        this.selectionControl.disable();
      }
    }

    if(this.alocacao) {
      this.aulasConflitantes = [];
      this.aulas$ = this.dataService.query(new QueryMirror('aula')
        .selectList(['id', 'numero', 'dia', 'turno', 'oferta', 'alocacao.id', 'alocacao.disciplina', 'alocacao.professor1', 'alocacao.professor2', 'alocacao.ano', 'alocacao.semestre'])
        .where({
          or: {
            'alocacao.professor1.id': {equals: this.alocacao?.professor1?.id},
            'alocacao.professor2.id': {equals: this.alocacao?.professor1?.id},
            ...orProfessor2Predicate
          },
          and: {
            'oferta.semestre': {equals: this.semestre},
            'oferta.ano': {equals: this.ano},
            'oferta.turma.ativa': {equals: true}
          }
        }))
        .pipe(
          tap((next: PagedList) => {
            if (next.listMap.length > 0) {
              let request = {
                aulas: next.listMap,
                oferta: this.oferta
              }
              this.dataService.post('aula/obter-restricoes', request).pipe().subscribe(
                next => {
                  next.forEach(conflito => {
                    conflito.mensagens.forEach(mensagem => {
                      let aulas = mensagem.aulas.map(a => {
                        Object.assign(a, {tipo: mensagem.tipo});
                        Object.assign(a, {professorConflito: conflito.professor});
                        return a;
                      });
                      this.aulasConflitantes.push(...aulas);
                    })
                  }, error => {
                    this.notificationService.handleError(error);
                    }
                    );
                }
              )
            }
          })
        );
    }
  }

  getDia(i: number) {
    switch (i) {
      case 0: return 'Segunda';
      case 1: return 'Terça';
      case 2: return 'Quarta';
      case 3: return 'Quinta';
      case 4: return 'Sexta';
      default: return 'Inexistente';
    }
  }

  getMatrizAulasProfessor(aulasProfessor: PagedList) {
    this.aulasProfessor = aulasProfessor;
    let aulas = aulasProfessor.listMap;
    if(this.selectionControl.value == 1) {
      aulas = aulas.filter(a => (a.alocacao?.professor1?.id === this.alocacao?.professor1?.id) || (a.alocacao.professor2?.id === this.alocacao?.professor1?.id));
    } else {
      aulas = aulas.filter(a => (a.alocacao?.professor2?.id === this.alocacao?.professor2?.id) || (a.alocacao.professor1?.id === this.alocacao?.professor2?.id));
    }
    let matriz: any[][];
    const matriz$: Observable<any[][]> = range(0, 5).pipe(
      map(() => Array.from({ length: 16 }, () => '')),
      toArray()
    );

    matriz$.subscribe(m => matriz = m);
    aulas.forEach(a => matriz[a.dia][a.numero] = a);
    return matriz;
  }

  verificarAula(item: any): string {
    if (!(typeof item === 'string')) {
      let aula = this.aulasConflitantes.find(a => a.numero === item.numero &&
          a.dia === item.dia &&
          a.turno === item.turno &&
          (a.professorConflito.id === item.alocacao.professor1?.id || a.professorConflito.id === item.alocacao.professor2?.id));

      if (aula !== undefined) {
        switch (aula.tipo as number) {
          case 1: return 'background-red';
          case 2: return 'background-yellow';
          case 3: return 'background-blue';
        }
      }
    }
    return 'background-white';
  }

  ngOnDestroy() {
    this.selectionSub?.unsubscribe();
  }

  getNomeProfessor() {
    if (this.selectionControl.value == 1) {
      return this.alocacao?.professor1?.nome;
    } else {
      return this.alocacao?.professor2?.nome;
    }
  }
}
