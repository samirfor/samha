import {Component, Input, OnInit} from '@angular/core';
import {Observable, range} from "rxjs";
import {first, map, toArray} from "rxjs/operators";
import {ILabel} from "../../label/label.component";
import {DataService} from "../../shared/service/data.service";
import {NotificationService} from "../../shared/service/notification.service";

@Component({
  selector: 'samha-report-aula-grid',
  templateUrl: './report-aula-grid.component.html',
  styleUrls: ['./report-aula-grid.component.css', '../../oferta/oferta-grid/oferta-grid.component.css']
})
export class ReportAulaGridComponent implements OnInit {
  @Input() entityData: any[];
  @Input() ano: number;
  @Input() semestre: number;
  public disciplinas: any[] = [];
  public labels: ILabel[] = [];

  constructor(private dataService: DataService,
              private notification: NotificationService) {
  }

  ngOnInit() {
    this.dataService.publicGetAll('label').pipe(first()).subscribe(next => {
      this.labels = next.sort((a,b) => a.numero > b.numero ? 1 : -1);
    }, error => this.notification.handleError(error))
  }

  getLabel(label: ILabel) {
    return label?.inicio.toString().substring(0, 5);
  }

  getMatrizAulasProfessor(aulasProfessor: any[]) {
    this.disciplinas = [];
    let matriz: any[][];
    const matriz$: Observable<any[][]> = range(0, 5).pipe(
      map(() => Array.from({ length: 16 }, () => '')),
      toArray()
    );
    matriz$.subscribe(m => matriz = m);
    aulasProfessor.forEach(a => {
      matriz[a.dia][a.numero] = a;
      let disciplina = {
        nome: a.nomeDisciplina,
        sigla: a.siglaDisciplina
      }
      let jaInseriu = this.disciplinas.find(d => d.nome == disciplina.nome);
      if (!jaInseriu) this.disciplinas.push(disciplina);
    });
    return matriz;
  }

  getDia(i: number) {
    switch (i) {
      case 0:return 'Segunda';
      case 1: return 'Terça';
      case 2: return 'Quarta';
      case 3: return 'Quinta';
      case 4: return 'Sexta';
      default: return 'Inexistente';
    }
  }

}
