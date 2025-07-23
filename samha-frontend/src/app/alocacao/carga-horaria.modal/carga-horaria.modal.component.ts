import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable, of} from "rxjs";
import {DataService} from "../../shared/service/data.service";
import {QueryMirror} from "../../shared/query-mirror";
import {alocacaoColumns} from "../../meta-model/alocacao";
import {PagedList} from "../../shared/paged-list";
import {catchError, map, tap} from "rxjs/operators";
import {NotificationService} from "../../shared/service/notification.service";

@Component({
  selector: 'samha-carga-horaria-modal',
  templateUrl: './carga-horaria.modal.component.html',
  styleUrls: ['./carga-horaria.modal.component.css']
})
export class CargaHorariaModalComponent implements OnInit {
  @Input() showPopup: boolean;
  @Output() showPopupChange = new EventEmitter<boolean>();
  @Input() cargaHoraria$: Observable<any>;
  @Input() ano: number;
  @Input() semestre: number;
  public showAlocacaoPopup: boolean = false;
  public selectedIndex = -1;
  public selectedrow: any;
  public alocacoesProfessor$: Observable<any>;

  constructor(private dataService: DataService,
              private notification: NotificationService) { }

  ngOnInit(): void {
  }

  onRowClicked(professor: any) {
    this.selectedIndex = professor.id
    this.selectedrow = professor;
    const request = {
      profId: professor.id,
      ano: this.ano,
      semestre: this.semestre
    }
    this.alocacoesProfessor$ = this.dataService.post('alocacao/obter-alocacoes-professor', request).pipe(
      tap(next => next.sort((a, b) => {
          if (a.disciplina.nome < b.disciplina.nome) return 1;
          else if (a.disciplina.nome > b.disciplina.nome) return -1;
          else return 0;
        })),
      catchError(err => {
        this.notification.handleError(err)
        return of(new Error(err))
      })
    );
    this.showAlocacaoPopup = true;
  }
}
