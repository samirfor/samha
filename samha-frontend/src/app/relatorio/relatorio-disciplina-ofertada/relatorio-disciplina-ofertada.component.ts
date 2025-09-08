import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Observable, Subscription} from "rxjs";
import {DataService} from "../../shared/service/data.service";
import {NotificationService} from "../../shared/service/notification.service";
import {HttpEvent, HttpEventType} from "@angular/common/http";
import {FunctionHelper} from "../../shared/function-helper";
import {RelatorioDto} from "../../meta-model/relatorio-professor";
import {AuthService} from "../../shared/service/auth.service";
import {PagedList} from "../../shared/paged-list";
import {MatSelectChange} from "@angular/material/select";
import {QueryMirror} from "../../shared/query-mirror";
import {map} from "rxjs/operators";

@Component({
  selector: 'samha-relatorio-disciplina-ofertada',
  templateUrl: './relatorio-disciplina-ofertada.component.html'
})
export class RelatorioDisciplinaOfertadaComponent implements OnInit, OnDestroy {
  // @Input() public cursoControl: FormControl;
  // @Input() public semestreControl: FormControl;
  // @Input() public anoControl: FormControl;
  // @Input() public enviarEmailControl: FormControl;
  @Input() public authenticated: boolean;
  @Input() public senhaControl: FormControl;

  public form: FormGroup;
  public compareFunction = (o1: any, o2: any) => (o1 != null && o2 != null && o1.id == o2.id);
  public eixos$: Observable<any>;
  public eixoControl = new FormControl({});
  public curso$: Observable<PagedList>;

  public isGenerating: boolean = false;
  private gerarPdfSub: Subscription;
  public hide: boolean = true;

  constructor(private dataService: DataService,
              private notification: NotificationService,
              private authService: AuthService,
              private fb: FormBuilder) { 
    const hoje = new Date();
    const anoAtual = hoje.getFullYear();
    const semestreAtual = hoje.getMonth() < 6 ? 1 : 2; // Jan-Jun = 1, Jul-Dez = 2
    this.form = this.fb.group({
        eixo: [null, Validators.required],
        curso: [null, Validators.required],
        ano: [anoAtual, [Validators.required, Validators.min(2020)]],
        semestre: [semestreAtual, [Validators.required, Validators.min(1), Validators.max(2)]]
    });
  }

  ngOnInit(): void {
     this.eixos$ = this.dataService.publicGetAll('eixo').pipe(
          map(items => {
            return items.sort((a, b) => a.nome.localeCompare(b.nome));
          })
        );
  }

  onGerarClick() {
  }

  ngOnDestroy() {
    this.gerarPdfSub?.unsubscribe();
  }

  private getRelatorioDto(): RelatorioDto {
    return {
      ano: this.form.controls['ano'].value,
      semestre: this.form.controls['semestre'].value,
      cursoId: this.form.controls['curso'].value?.id,
      enviarEmail: false,
      senha: this.senhaControl.value
    }
  }

  onEixoSelected(eixo: MatSelectChange) {
    this.curso$ = this.dataService.publicQuery(new QueryMirror('curso').selectList(['id', 'nome']).where({
      and: {
        'coordenadoria.eixo.id': {equals: eixo.value.id}
      }
    }).orderBy('nome'))
  }

  gerarRelatorio() {
    if (!this.authenticated) {
      this.gerarPdfSub = this.dataService.publicAsyncPost('gerar-relatorio-disciplina-ofertada', this.getRelatorioDto())
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
      this.gerarPdfSub = this.dataService.asyncPost('relatorio/gerar-relatorio-disciplina-ofertada', this.getRelatorioDto())
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
  
}