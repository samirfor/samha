import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DataService} from "../../shared/service/data.service";
import {NotificationService} from "../../shared/service/notification.service";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {Observable, of, range, Subscription} from "rxjs";
import {catchError, delay, first, map, startWith, tap, toArray} from "rxjs/operators";
import {QueryMirror} from "../../shared/query-mirror";
import {alocacaoColumns} from "../../meta-model/alocacao";
import {MatDialog} from "@angular/material/dialog";
import {AlteracaoDialogComponent} from "../../shared/alteracao-dialog/alteracao-dialog.component";
import {PagedList} from "../../shared/paged-list";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {HttpEvent, HttpEventType} from "@angular/common/http";
import {error} from "protractor";
import {LocalStorageService} from "../../shared/service/local-storage.service";


@Component({
  selector: 'samha-oferta',
  templateUrl: './oferta.component.html',
  styleUrls: ['./oferta.component.css', '../oferta-grid/oferta-grid.component.css'],
  animations: [
    trigger('pushInOut', [
      state('void', style({transform: 'translateX(100%)'})),
      state('*', style({transform: 'translateX(0)'})),
      transition(':enter', animate('200ms linear')),
      transition(':leave', animate('200ms linear'))
    ]),
    trigger('verticalInOut', [
      state('void', style({transform: 'translateY(-100%)'})),
      state('*', style({transform: 'translateY(0)'})),
      transition(':leave', animate('200ms ease-out')),
      transition(':enter', animate('200ms ease-in'))
    ])
  ]
})
export class OfertaComponent implements OnInit, OnDestroy {
  @ViewChild('anoInput', {static: false}) anoInput: ElementRef;
  @ViewChild('semestreInput', {static: false}) semestreInput: ElementRef;
  @ViewChild('periodoInput', {static: false}) periodoInput: ElementRef;
  private validarTurmasSub: Subscription;
  public cursoControl = new FormControl();
  public turmaControl = new FormControl();
  public showPopupMudarVisiblidade: boolean = false;
  public formGroup: FormGroup;
  public formPeriodo: FormGroup;
  public filteredOptions: Observable<any[]>;
  public alocacao$: Observable<any>;
  public alocacaoColumns = alocacaoColumns;
  public alocacaoDisplayedColumns = alocacaoColumns.filter(c => c.visible).map(c => c.columnDef);
  public qtPeriodos = 1;
  public tempoMaximo = '11';
  public intervaloMinimo = '11';
  public ofertaChanged: boolean = false;
  public alocacoes = [];
  public alocacaoSelecionada: any;
  public matriz: any[][] = [[]];
  public oferta: any;
  public notificacoes: any[] = [];
  public aulasConflitantes: any[] = [];
  public filterOpened: boolean = true;
  public notificacoesOpened: boolean = false;
  public notificacaoTurma: boolean = false;
  public showPopupOfertaPublica: boolean = false;z
  public permissaoMudarVisibilidade: boolean = false;
  public novaAula: any;
  private list: any[];
  private aulasMatutinas: any[] = [];
  private aulasVespertinas: any[] = [];
  private aulasNoturnas: any[] = [];
  private turmaCurrentValue: any;
  private cursoCurrentValue: any;
  private anoCurrentValue: any;
  public semestreCurrentValue: any;
  private periodoCurrentValue: any;
  private progresso: number = 0;
  private length: number;
  private disciplinasWarns: any[] = [];
  private restricoesSubscription: Subscription;


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private dataService: DataService,
    private notification: NotificationService,
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private localStorageService: LocalStorageService
  ) {
    this.formGroup = formBuilder.group({
      curso: [''],
      ano: [],
      semestre: [],
      turma: [''],
      turno: ['MATUTINO']
    });
    this.formPeriodo = formBuilder.group({
     periodo: [1]
    });

  }

  ngOnInit(): void {
    this.dataService.post('oferta/permissaoMudarVisibilidade', null).pipe(first()).subscribe(
      (resultado: boolean) => {
        this.permissaoMudarVisibilidade = resultado;
      }, error => this.notification.handleError(error)
    )
    const cursoSelecionadoRaw = localStorage.getItem('curso');
    const anoSelecionado = localStorage.getItem('ano');
    const semestreSelecionado = localStorage.getItem('semestre');
    
    if (cursoSelecionadoRaw && cursoSelecionadoRaw.trim() !== '') {
      const cursoSelecionado = JSON.parse(cursoSelecionadoRaw);
      this.cursoControl.setValue(cursoSelecionado);
      this.onCursoChange(cursoSelecionado); // dispara a atualização das turmas
    }

    if (anoSelecionado && anoSelecionado !== 'undefined') {
      this.formGroup.get('ano').setValue(anoSelecionado);
    }
    else {
      this.formGroup.get('ano').setValue(new Date().getUTCFullYear());
      localStorage.setItem('ano', this.formGroup.get('ano').value);
    }

    if (semestreSelecionado && semestreSelecionado !== 'undefined') {
      this.formGroup.get('semestre').setValue(semestreSelecionado);
    }
    else {
      this.formGroup.get('semestre').setValue(new Date().getMonth() < 6 ? 1 : 2);
      localStorage.setItem('semestre', this.formGroup.get('semestre').value);
    }

    this.anoCurrentValue = this.formGroup.get('ano').value;
    this.semestreCurrentValue = this.formGroup.get('semestre').value;
  }

  goToLogOferta() {
    this.router.navigate(['logOferta'], {relativeTo: this.route});
  }

  goToLogAulas() {
    this.router.navigate(['logAulas'], {relativeTo: this.route});
  }

  onCursoChange(_) {
    this.qtPeriodos = this.cursoControl.value.qtPeriodos;
    localStorage.setItem('curso', JSON.stringify(this.cursoControl.value));
    this.loadTurmas();
  }

  onCursoLoaded($event: any[]) {
    const cursoSelecionadoRaw = localStorage.getItem('curso');
    if (cursoSelecionadoRaw && cursoSelecionadoRaw.trim() !== '') {
      const cursoSelecionado = JSON.parse(cursoSelecionadoRaw);
      this.cursoControl.setValue(cursoSelecionado);
    }
    else{
      if ($event.length > 0) {
        this.cursoControl.setValue($event[0]);
        this.qtPeriodos = this.cursoControl.value.qtPeriodos;
        this.loadTurmas();
      } else {
        this.notification.error('Não há cursos cadastrados!');
      }
    }
  }

  compareFunction = (o1: string, o2: string) => (o1 != null && o2 != null && o1.toUpperCase() == o2.toUpperCase());
  displayFn = (entity: any): string => entity && entity.nome ? entity.nome : '';

  private _filter(nome: string): any[] {
    const filterValue = nome.toLowerCase();
    return this.list.filter(entity => entity.nome.toLowerCase().includes(filterValue));
  }

  private loadTurmas() {
    if (this.ofertaChanged) {
      const dialogRef = this.openDialog();
      dialogRef.afterClosed().pipe(first()).subscribe(
        (result: string) => {
          if (result === 'salvar') {
            this.onSalvarClicked();
          } else if (result === 'descartar') {
            this.executeTurmaQuery();
          } else {
            this.cursoControl.setValue(this.cursoCurrentValue);
            this.turmaControl.setValue(this.turmaCurrentValue);
            this.formGroup.get('turno').setValue(this.turmaCurrentValue?.turno)
          }
        }
      )
    } else {
      this.executeTurmaQuery();
    }

  }

  onTurnoChange() {
    this.buildMatriz();
  }

  executeTurmaQuery() {
    this.alocacoes = [];
    this.oferta = undefined;
    this.ofertaChanged = false;
    this.dataService.query(
      new QueryMirror('turma')
        .selectList(['id', 'nome', 'matriz.id', 'ano','matriz.curso.id', 'matriz.curso.semestral', 'turno'])
        .where({
            and: {
              'matriz.curso.id': {equals: this.cursoControl.value.id},
              'ativa': {equals: true}
            }
          }
        ).orderBy('nome asc')
    ).pipe(
      first(),
      tap(
        (next: PagedList) => {
          next.listMap.sort((a, b) => {
            if (a.nome > b.nome) return 1;
            else if (a.nome < b.nome) return -1;
            else return 0;
          })
        }
      )
    ).subscribe(
      data => {
        this.list = data.listMap;       
        if (this.list.length > 0 && (!this.turmaControl.value?.id || (this.turmaControl.value?.matriz.curso.id !== this.cursoControl.value?.id))) {
          this.turmaControl.setValue(this.list[0]);
          this.formGroup.get('turno').setValue(this.turmaControl.value?.turno)
          this.onTurmaChange();
          const contador = {};
          // Primeiro: contar quantos nomes iguais existem
          this.list.forEach(item => {
            const nomeBase = item.nome.trim();
            contador[nomeBase] = (contador[nomeBase] || 0) + 1;
          });

          // Segundo: aplicar nomes com sufixo apenas aos que se repetem
          const contadorOcorrencia = {};
          this.list = this.list.map(item => {
            const nomeBase = item.nome.trim();

            // Só modifica se houver mais de uma ocorrência
            if (contador[nomeBase] > 1) {
              contadorOcorrencia[nomeBase] = (contadorOcorrencia[nomeBase] || 0) + 1;
              return {
                ...item,
                nome: `${nomeBase} (${item.ano})`
              };
            }

            // Se não repetir, mantém original
            return item;
          });
        } else {
          this.turmaControl.setValue(undefined);
          this.notification.warning('Não há turmas para este curso.');
          this.buildMatriz();
        }
        this.filteredOptions = this.turmaControl.valueChanges.pipe(
          startWith(''),
          map(value => (typeof value === 'string' ? value : value?.name)),
          map(name => (name ? this._filter(name) : this.list.slice())),
        );
      }
    )
  }

  onAnoChange(value: any) {
    localStorage.setItem('ano', value);
    if (this.ofertaChanged) {
      const dialogRef = this.openDialog();
      dialogRef.afterClosed().pipe(first()).subscribe(
        (result: string) => {
          if (result === 'salvar') {
            this.onSalvarClicked()
          } else if (result === 'descartar') {
            this.anoCurrentValue = value;
            this.executeAnoQuery();
          } else {
            this.formGroup.get('ano').setValue(this.anoCurrentValue);
          }
        }
      )
    } else {
      this.anoCurrentValue = value;
      this.executeAnoQuery();
    }
  }

  private executeAnoQuery() {
    this.ofertaChanged = false;
    this.dataService.query(new QueryMirror('alocacao')
      .selectList(['id', 'disciplina.sigla', 'disciplina.id', 'disciplina.tipo', 'professor1', 'professor2', 'ano', 'semestre'])
      .where({
        and: {
          'disciplina.periodo': {equals: this.formPeriodo.get('periodo').value},
          'ano': {equals: this.formGroup.get('ano').value},
          'semestre': {equals: this.formGroup.get('semestre').value},
          'disciplina.matriz.id': {equals: this.turmaControl.value.matriz.id}
        }
      })
    ).pipe(first())
      .subscribe(
        data => {
          this.alocacoes = data.listMap;
          this.executeOfertaQuery();
        }
      );
  }


  private executeOfertaQuery() {
    this.notificacoes = [];
    this.disciplinasWarns = [];
    this.notificacaoTurma = false;
    this.dataService.query(new QueryMirror('oferta')
      .selectList(['id', 'ano', 'semestre', 'tempoMaximoTrabalho', 'intervaloMinimo', 'turma.id', 'turma.nome', 'publica'])
      .where({
        and: {
          'ano': {equals: this.formGroup.get('ano').value},
          'semestre': {equals: this.formGroup.get('semestre').value},
          'turma.id': {equals: this.turmaControl.value.id},
          'turma.ativa': {equals: true},
          'tempoMaximoTrabalho': {equals: this.tempoMaximo},
          'intervaloMinimo': {equals: this.intervaloMinimo}
        }
      })
    ).pipe(first()).subscribe(
      next => {
        if (next.listMap.length > 0) {
          this.oferta = next.listMap[0];
          this.dataService.query(new QueryMirror('aula')
            .selectList(['id', 'numero', 'dia', 'turno', 'oferta', 'alocacao.id', 'alocacao.disciplina', 'alocacao.professor1', 'alocacao.professor2', 'alocacao.ano', 'alocacao.semestre'])
            .where({
              and: {
                'oferta.id': {equals: next.listMap[0].id}
              }
            })).pipe(first()).subscribe(
            next => {
              let list = next.listMap as any[];
              if (next.listMap.length > 0) this.executeAulasRestricaoQuery(next.listMap);
              this.aulasMatutinas = list.filter(a => a.numero <= 5);
              this.aulasVespertinas = list.filter(a => a.numero > 5 && a.numero <= 11);
              this.aulasNoturnas = list.filter(a => a.numero > 11);
              this.buildMatriz();
            }
          )
        } else {
          this.oferta = {
            id: undefined,
            ano: this.formGroup.get('ano').value,
            semestre: this.formGroup.get('semestre').value,
            turma: {
              id: this.turmaControl.value.id,
            },
            tempoMaximoTrabalho: this.tempoMaximo,
            intervaloMinimo: this.intervaloMinimo,
            publica: false
          }
          this.aulasVespertinas = [];
          this.aulasMatutinas = [];
          this.aulasNoturnas = [];
          this.buildMatriz();
        }
      }
    )
  }

  onTurmaChange() {
    if (this.ofertaChanged) {
      const dialogRef = this.openDialog();
      dialogRef.afterClosed().pipe(first()).subscribe(
        (result: string) => {
          if (result === 'salvar') {
            this.onSalvarClicked()
          } else if (result === 'descartar') {
            this.executePeriodoAtualQuery();
          } else {
            this.turmaControl.setValue(this.turmaCurrentValue);
            this.formGroup.get('turno').setValue(this.turmaCurrentValue?.turno)
          }
        }
      )
    } else {
      this.executePeriodoAtualQuery();
    }
  }

  private executePeriodoAtualQuery() {
    this.ofertaChanged = false;
    this.dataService.get('turma/getPeriodoAtual', this.turmaControl.value?.id)
      .pipe(first())
      .subscribe(data => {
        let periodo = data > 0 ? data : 1;
        this.formPeriodo.get('periodo').setValue(periodo);
        this.periodoCurrentValue = periodo;
        this.onAnoChange(this.anoCurrentValue);
      });
  }

  getNomeEncurtadoProfessor(nome: string) {
    if (nome != null) {
      let nomes = nome.split(' ');
      let siglas = nomes.map(n => n.substring(0, 1)).splice(1).filter(l => l !== 'd').join('');
      let nomeEncutado = nomes[0] + ' ' + siglas;
      return nomeEncutado;
    }

    return '';
  }

  onDragEnd(event: DragEvent) {
    event.preventDefault();
    const target = event.target as HTMLElement;
    target.classList.remove('dragging');
    this.novaAula = undefined;
  }

  onListDraggerStart(alocacao: any) {
    let novaAula = {
      alocacao: alocacao,
      dia: null,
      numero: null,
      turno: this.getValorTurno(),
      oferta: this.oferta
    }
    this.novaAula = novaAula;
  }

  private buildMatriz() {
    this.matriz = [[]];
    const turno = (this.formGroup.get('turno').value as string).toUpperCase();
    this.length = this.getValorTurno() == 12 ? 4 : 6;

    const matriz$: Observable<any[][]> = range(0, 5).pipe(
      map(() => Array.from({length: this.length}, () => '')),
      toArray()
    );

    matriz$.subscribe(m => {
      this.matriz = m;

      switch (turno) {
        case 'MATUTINO':
          this.aulasMatutinas.forEach(aula => this.matriz[aula.dia][aula.numero] = aula);
          break;
        case 'VESPERTINO':
          this.aulasVespertinas.forEach(aula => this.matriz[aula.dia][aula.numero % 6] = aula);
          break;
        default:
          this.aulasNoturnas.forEach(aula => this.matriz[aula.dia][aula.numero % 6] = aula);
          break;
      }
    });

    return this.matriz;
  }

  onAulaChanged(event: any) {
    this.ofertaChanged = true;
    switch (event.turno) {
      case 0:
        this.aulasMatutinas.push(event);
        break;
      case 6:
        this.aulasVespertinas.push(event);
        break;
      case 12:
        this.aulasNoturnas.push(event);
        break;
    }
    this.buildMatriz();
  }

  private getValorTurno() {
    switch (this.formGroup.get('turno').value.toUpperCase()) {
      case 'MATUTINO':
        return 0;
      case 'VESPERTINO':
        return 6;
      case 'NOTURNO':
        return 12;
    }
  }

  onAulaIndexChanged(event: any) {
    if (event.prevItem.item !== undefined && !(event.prevItem.item instanceof String)) this.onAulaChanged(event.prevItem)
    else this.matriz[event.prevItem.prevRowIndex][event.prevItem.prevColIndex] = event.item;
    if (event.currItem.item !== undefined && !(event.currItem.item instanceof String)) this.onAulaChanged(event.currItem)
    else this.matriz[event.currItem.rowIndex][event.currItem.colIndex] = event.item;
  }

  onAulaDeleted(event: any) {
    this.ofertaChanged = true;
    if (event.numero < 6) {
      this.aulasMatutinas = this.aulasMatutinas.filter(a => !(a.dia == event.dia && a.numero == event.numero));
    } else if (event.numero < 12 && event.numero >= 6) {
      this.aulasVespertinas = this.aulasVespertinas.filter(a => !(a.dia == event.dia && a.numero == event.numero));
    } else if (event.numero < 16 && event.numero >= 12) {
      this.aulasNoturnas = this.aulasNoturnas.filter(a => !(a.dia == event.dia && a.numero == event.numero));
    }
    this.buildMatriz();
  }

  onSemestreChange(value: any) {
    localStorage.setItem('semestre', value);
    if (this.ofertaChanged) {
      const dialogRef = this.openDialog();
      dialogRef.afterClosed().pipe(first()).subscribe(
        (result: string) => {
          if (result === 'salvar') {
            this.onSalvarClicked();
          } else if (result === 'descartar') {
            this.semestreCurrentValue = value;
            this.executeAnoQuery();
          } else {
            this.formGroup.get('semestre').setValue(this.semestreCurrentValue);
          }
        }
      )
    } else {
      this.semestreCurrentValue = value;
      this.executeAnoQuery();
    }
  }

  onPeriodoChange(value: any) {
    if (this.ofertaChanged) {
      const dialogRef = this.openDialog();
      dialogRef.afterClosed().pipe(first()).subscribe(
        (result: string) => {
          if (result === 'salvar') {
            this.onSalvarClicked();
          } else if (result === 'descartar') {
            this.periodoCurrentValue = value;
            this.executeAnoQuery();
          } else {
            this.formPeriodo.get('periodo').setValue(this.periodoCurrentValue);
          }
        }
      )
    } else {
      this.periodoCurrentValue = value;
      this.executeAnoQuery();
    }
  }

  selectAlocacao = (alocacao: any) => {
    if (alocacao.id === this.alocacaoSelecionada?.id) this.alocacaoSelecionada = undefined;
    else this.alocacaoSelecionada = alocacao;
  }
  getAulas = () => [...this.aulasMatutinas, ...this.aulasVespertinas, ...this.aulasNoturnas];
  openDialog = () => this.dialog.open(AlteracaoDialogComponent);
  
  onTurmaSelectOpened = () => {
    this.turmaCurrentValue = this.turmaControl.value;
  }
  
  onCursoSelectionOpened = () => {
    this.cursoCurrentValue = this.cursoControl.value;
    localStorage.setItem('curso', JSON.stringify(this.cursoControl.value));
  }

  private executeAulasRestricaoQuery(aulas: any[]) {
    let request = {
      aulas: aulas,
      oferta: this.oferta
    }
    this.aulasConflitantes = [];
    this.restricoesSubscription = this.dataService.post('aula/obter-restricoes', request).pipe(first()).subscribe(
      next => {
        this.notificacoes = next;
        next.forEach(conflito => {
          conflito.mensagens.forEach(mensagem => {
            let aulas = mensagem.aulas.map(a => Object.assign(a, {tipo: mensagem.tipo}));
            this.aulasConflitantes.push(...aulas);
          })
        });
      }, error => this.notification.handleError(error)
    )
  }

  getQuantidadeNotificacao(item: any, tipo: number) {
    let mensagens = item.mensagens as any[];
    return mensagens.filter(m => m.tipo == tipo).length;
  }

  getQuantidadeNotificacaoTurma(itemTurma: any, tipo: number) {
    let qtd = 0;
    itemTurma.conflitos.forEach(item => {
      qtd += this.getQuantidadeNotificacao(item, tipo);
    })
    return qtd;
  }

  onValidarAulasClick = () => {
    this.notificacaoTurma = false;
    this.notificacoes = [];
    this.aulasConflitantes = [];
    this.disciplinasWarns = [];
    this.executeAulasRestricaoQuery([...this.aulasVespertinas, ...this.aulasMatutinas, ...this.aulasNoturnas]);
  }

  onControleQuantidadeDisciplinasClick() {
    this.notificacaoTurma = false;
    this.disciplinasWarns = [];
    let aulas = [
      ...this.aulasMatutinas,
      ...this.aulasVespertinas,
      ...this.aulasNoturnas
    ];
    let request = {
      ano: this.formGroup.get('ano').value,
      semestre: this.formGroup.get('semestre').value,
      periodo: this.formPeriodo.get('periodo').value,
      cursoId: this.cursoControl.value.id,
      aulasCriadas: aulas
    }
    this.dataService.post('aula/controle-qtd-disciplina', request).pipe(first())
      .subscribe(next => {
        if (next.mensagens.length > 0) {
          this.notificacoes = [next];
          next.mensagens.forEach(m => this.disciplinasWarns.push(m.disciplina));
        }
      }, error => this.notification.handleError(error))
  }

  onSalvarClicked() {
    this.ofertaChanged = false;
    const aulas = [...this.aulasMatutinas, ...this.aulasVespertinas, ...this.aulasNoturnas];
    const ofertaDto = {
      ofertaId: this.oferta.id,
      aulas: aulas
    }
    this.dataService.post('aula/salvar-aulas', ofertaDto).pipe(first()).subscribe(
      next => {
        this.executeOfertaQuery();
        this.notification.success('As aulas foram salvas com sucesso!');
      }, error => {
        this.notification.handleError(error);
        this.ofertaChanged = false;
      }
    )
  }

  public createOferta () {
    this.dataService.query(new QueryMirror('oferta').select('id').where({
     and: {
       publica: {equals: true},
       ano: {equals: this.anoCurrentValue},
       semestre: {equals: this.semestreCurrentValue}
     }
    })).pipe(first()).subscribe(
      (result: PagedList) => {
        if (result.listMap.length > 0) {
          this.showPopupOfertaPublica = true;
        } else {
          this.saveOferta();
        }
      }, error => this.notification.handleError(error)
    )
  }

  public saveOferta(): void {
    this.showPopupOfertaPublica = false;
    this.dataService.save('oferta', this.oferta).pipe(first()).subscribe(
      next => {
        this.oferta = next;
        this.notification.success('Oferta criada com sucesso!');
      }, error => this.notification.handleError(error)
    );
  }

  onDesfazerAlteracoesClicked() {
    this.ofertaChanged = false;
    this.executeOfertaQuery();
  }

  onValidarTurmasClick() {
    this.notificacoes = [];
    this.disciplinasWarns = [];
    this.progresso = 0;
    if (this.ofertaChanged) {
      this.notification.error('Você possui alterações que precisam ser salvas antes de realizar esta ação!');
      return;
    }
    this.notificacaoTurma = true;
    this.notification.warning('Este processo pode demorar um pouco! Você pode conferir seu progresso na barra de carregamento abaixo!');
    const timer = setInterval(() => {
      this.progresso += 1; // Incrementa o progresso em 5%

      if (this.progresso >= 95) {
        clearInterval(timer); // Interrompe o timer quando o progresso atingir 95%
      }
    }, 200);
    this.validarTurmasSub = this.dataService.asyncPost('aula/validar-turmas/' + this.formGroup.get('ano').value + '/' + this.formGroup.get('semestre').value, null)
      .subscribe((event: HttpEvent<any>) => {
        if (event.type === HttpEventType.DownloadProgress) {
        } else if (event.type === HttpEventType.Response) {
          this.progresso = 100;
          clearInterval(timer);
          this.notificacoes = event.body;
        }
      }, error => {
        this.progresso = 0;
        clearInterval(timer);
        this.notification.handleError(error);
      });
  }

  /* Este método copia as aulas do semestre anterior da turama para o semestre corrente, dentro do mesmo ano. Ou seja, este método não está adaptado
   para copiar as aulas do segundo semestre do ano anterior para o primeiro semestre do ano corrente.
  */
  onCopiarAulasClick(){
    if(this.formGroup.get('semestre').value == 1){
      this.notification.warning('Não é possível realizar a cópia das aulas do ano anterior. Certifique-se que esteja no segundo semestre!');
      return;
    }

    const confirma = window.confirm('Confirma a cópia das aulas do semestre anterior? \nObs.: As aulas do semestre corrente NÃO serão preservadas!');

    if(confirma){
      this.dataService.post('aula/copiar-aulas/' + this.turmaControl.value?.id + '/' + this.formGroup.get('ano').value + '/' + this.oferta.id , null)
        .subscribe((event: HttpEvent<any>) => {   }, 
          error => {
          this.notification.handleError(error);
        });
        this.executeAnoQuery();
    }
  }

  format = (ratio) => `Progresso: ${parseInt((ratio * 100).toString())}%`;

  ngOnDestroy() {
    this.validarTurmasSub?.unsubscribe();
    this.restricoesSubscription?.unsubscribe();
  }

  onNovaAulaCreated(item: any) {
    switch (item.turno) {
      case 0:
        this.aulasMatutinas = this.aulasMatutinas.filter(a => !(a.dia == item.dia && a.numero == item.numero));
        break;
      case 6:
        this.aulasVespertinas = this.aulasVespertinas.filter(a => !(a.dia == item.dia && a.numero == item.numero));
        break;
      case 12:
        this.aulasNoturnas = this.aulasNoturnas.filter(a => !(a.dia == item.dia && a.numero == item.numero));
        break;
    }
    this.onAulaChanged(item)
  }

  mudarVisiblidadeOferta() {
    this.showPopupMudarVisiblidade = false;
    if (this.oferta?.id)
      this.dataService.post('oferta/mudar-visiblidade', this.oferta?.id).pipe(first()).subscribe(
        next => {
          this.oferta = next;
          this.notification.success('Visibilidade do horário alterada com sucesso!');
        }, error => this.notification.handleError(error)
      );
  }

  onConfirmarClick() {

  }

  isDisciplinaAlocacaoInvalida(alocacao: any) {
    let disciplina = this.disciplinasWarns.find(d => d.id === alocacao.disciplina.id);
    if (disciplina) return true;
    return false;
  }

  getCorTexto(cor: any) {
    switch (cor) {
      case '#C7312C': return '#fff';
      case '#FF9966':
      case '#00A4CA': return '#000';
      default: return '#000';
    }
  }
}
