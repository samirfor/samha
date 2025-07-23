import {
  Component, Input,
  OnDestroy,
  OnInit
} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {Observable, of, range, Subscription} from "rxjs";
import {PagedList} from "../../shared/paged-list";
import {DataService} from "../../shared/service/data.service";
import {NotificationService} from "../../shared/service/notification.service";
import {MatSelectChange} from "@angular/material/select";
import {QueryMirror} from "../../shared/query-mirror";
import {catchError, map, tap} from "rxjs/operators";
import {HttpEvent, HttpEventType} from "@angular/common/http";
import {RelatorioDto} from "../../meta-model/relatorio-professor";
import {FunctionHelper} from "../../shared/function-helper";

@Component({
  selector: 'samha-relatorio-professor',
  templateUrl: './relatorio-professor.component.html',
  styleUrls: ['../../oferta/oferta-grid/oferta-grid.component.css']
})
export class RelatorioProfessorComponent implements OnInit, OnDestroy {
  @Input() public semestreControl: FormControl
  @Input() public anoControl: FormControl;
  @Input() public authenticated: boolean;
  @Input() public enviarEmailControl: FormControl;
  @Input() public senhaControl: FormControl;
  public compareFunction = (o1: any, o2: any) => (o1 != null && o2 != null && o1.id == o2.id);
  public radioGroupControl = new FormControl();

  public professorSelection$: Observable<PagedList>;
  public professores$: Observable<any>;
  public eixos$: Observable<any>;
  public coord$: Observable<PagedList>;
  public formGroup: FormGroup;
  public professorControl = new FormControl();
  public professores: any[] = [];
  public isLoading = false;
  private gerarPdfSub: Subscription;
  public showPopup: boolean = false;
  public isGenerating: boolean = false;
  public hide: boolean = true;


  constructor(private dataService: DataService,
              private formBuilder: FormBuilder,
              private notification: NotificationService) {
    this.formGroup = formBuilder.group({
      coord: [null],
      eixo: [null]
    })

  }

  ngOnInit(): void {
    this.eixos$ = this.dataService.publicGetAll('eixo').pipe(
      map(items => {
        return items.sort((a, b) => a.nome.localeCompare(b.nome));
      })
    );
  }

  onEixoSelected($event: MatSelectChange) {
    this.coord$ = this.dataService.publicQuery(new QueryMirror('coordenadoria').selectList(['id', 'nome']).where({
      and: {
        'eixo.id': {equals: $event.value.id}
      }
    }).orderBy('nome'));
  }

  onCoordSelected($event: MatSelectChange) {
    this.professorSelection$ = this.dataService.publicQuery(new QueryMirror('professor').selectList(['id', 'nome']).where({
      and: {
        'ativo': {equals: true},
        'coordenadoria.id': {equals: $event.value.id}
      }
    }).orderBy('nome'));
    this.isLoading = true;
    this.getProfessores();
  }

  private getProfessores(): void {
    if (!this.authenticated) {
      this.professores$ = this.dataService.publicPost('obter-professores-relatorio', this.getRelatorioDto()).pipe(
        tap(next => {
            this.isLoading = false;
            this.professores = next;
          }
        ),
        catchError(err => {
          this.notification.handleError(err);
          return of(new Error(err));
        })
      )
    } else {
      this.professores$ = this.dataService.post('relatorio/obter-professores-relatorio', this.getRelatorioDto()).pipe(
        tap(next => {
            this.isLoading = false;
            this.professores = next;
          }
        ),
        catchError(err => {
          this.notification.handleError(err);
          return of(new Error(err));
        })
      )
    }
  }

  onProfessorSelected($event: MatSelectChange) {
    this.isLoading = true;
    this.getProfessores();
  }

  onAnoSemestreChange() {
    this.isLoading = true;
    if (this.formGroup.get('coord').value?.id) this.getProfessores();
  }

  generatePdf(button: any) {
    let coordId = this.formGroup.get('coord').value?.id;
    if (coordId == undefined) {
      this.showPopup = true;
    } else {
      this.gerarRelatorio();
    }

  }

  public gerarRelatorio(): void {
    if (this.enviarEmailControl.value && !this.senhaControl.valid){
      this.senhaControl.markAsTouched();
      this.notification.error('A senha é obrigatória para o envio de e-mail.');
      return;
    }
    this.isGenerating = true;
    if (!this.authenticated) {
      this.gerarPdfSub = this.dataService.publicAsyncPost('gerar-relatorio-professor', this.getRelatorioDto())
        .subscribe((event: HttpEvent<any>) => {
          if (event.type === HttpEventType.DownloadProgress) {
          } else if (event.type === HttpEventType.Response) {
            FunctionHelper.downloadFile(event.body.nomeArquivo, event.body.bytes);
            this.notification.success('Relatório gerado com sucesso!');
            this.isGenerating = false;
          }
        }, error => {
          this.isGenerating = false;
          this.notification.handleError(error)
        });
    } else {
      this.gerarPdfSub = this.dataService.asyncPost('relatorio/gerar-relatorio-professor', this.getRelatorioDto())
        .subscribe((event: HttpEvent<any>) => {
          if (event.type === HttpEventType.DownloadProgress) {
          } else if (event.type === HttpEventType.Response) {
            FunctionHelper.downloadFile(event.body.nomeArquivo, event.body.bytes);
            this.notification.success('Relatório gerado com sucesso!');
            this.isGenerating = false;
          }
        }, error => {
          this.isGenerating = false;
          this.notification.handleError(error)
        });
    }
  }

  getRelatorioDto(): RelatorioDto {
    return {
      ano: this.anoControl.value,
      semestre: this.semestreControl.value,
      professorId: this.professorControl.value?.id,
      coordenadoriaId: this.formGroup.get('coord').value?.id,
      eixoId: this.formGroup.get('eixo').value?.id,
      nomeRelatorio: 'relatorioGenerico',
      enviarEmail: this.enviarEmailControl.value,
      senha: this.senhaControl.value
    }
  }

  ngOnDestroy() {
    this.gerarPdfSub?.unsubscribe();
  }

  onRadioSelected() {
    const selected = this.radioGroupControl.value;
    switch (selected) {
      case '1':
        this.formGroup.get('eixo').setValue({});
        this.formGroup.get('coord').setValue({});
        this.professorControl.setValue({});
        this.professores = [];
        break;
      case '2':
        this.formGroup.get('coord').setValue({});
        this.professorControl.setValue({});
        this.professores = [];
        break;
      case '3':
        this.professorControl.setValue({});
        if (this.formGroup.get('coord').value?.id) this.onAnoSemestreChange();
        break;
      case '4':
        if (this.professorControl.value?.id) this.onAnoSemestreChange();
        break;
    }
  }

  onOptionChoosen(generate: boolean) {
    if(generate) this.gerarRelatorio();
    this.showPopup = false;
  }
}
