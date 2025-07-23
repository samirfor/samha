import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FormControl} from "@angular/forms";
import {Subscription} from "rxjs";
import {DataService} from "../../shared/service/data.service";
import {NotificationService} from "../../shared/service/notification.service";
import {HttpEvent, HttpEventType} from "@angular/common/http";
import {FunctionHelper} from "../../shared/function-helper";
import {RelatorioDto} from "../../meta-model/relatorio-professor";
import {AuthService} from "../../shared/service/auth.service";

@Component({
  selector: 'samha-relatorio-disciplina',
  templateUrl: './relatorio-disciplina.component.html'
})
export class RelatorioDisciplinaComponent implements OnInit, OnDestroy {
  @Input() public semestreControl: FormControl;
  @Input() public anoControl: FormControl;
  @Input() public authenticated: boolean;
  @Input() public enviarEmailControl: FormControl;
  @Input() public senhaControl: FormControl;
  public isGenerating: boolean = false;
  private gerarPdfSub: Subscription;
  public hide: boolean = true;
  constructor(private dataService: DataService,
              private notification: NotificationService,
              private authService: AuthService) { }

  ngOnInit(): void {
  }

  onGerarClick() {
    if (this.enviarEmailControl.value && !this.senhaControl.valid){
      this.senhaControl.markAsTouched();
      this.notification.error('A senha é obrigatória para o envio de e-mail.');
      return;
    }
    this.isGenerating = true;
    if (!this.authenticated) {
      this.gerarPdfSub = this.dataService.publicAsyncPost('gerar-relatorio-disciplina', this.getRelatorioDto())
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
      this.gerarPdfSub = this.dataService.asyncPost('relatorio/gerar-relatorio-disciplina', this.getRelatorioDto())
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

  ngOnDestroy() {
    this.gerarPdfSub?.unsubscribe();
  }

  private getRelatorioDto(): RelatorioDto {
    return {
      ano: this.anoControl.value,
      semestre: this.semestreControl.value,
      enviarEmail: this.enviarEmailControl.value,
      senha: this.senhaControl.value
    }
  }

  onAnoSemestreChange() {
    this.authService.loggedIn
  }
}
