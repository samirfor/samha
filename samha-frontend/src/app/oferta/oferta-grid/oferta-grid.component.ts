import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ILabel} from "../../label/label.component";
import {DataService} from "../../shared/service/data.service";
import {NotificationService} from "../../shared/service/notification.service";
import {first} from "rxjs/operators";

@Component({
  selector: 'samha-oferta-grid',
  templateUrl: './oferta-grid.component.html',
  styleUrls: ['./oferta-grid.component.css']
})
export class OfertaGridComponent implements OnInit {
  @Input() matriz: any[][] = [[]];
  @Input() novaAula: any;
  @Input() turno: string;
  @Input() turma: any;
  @Input() aulasConflitantes: any[] = [];
  @Output() public onNovaAulaCreated = new EventEmitter<any>();
  @Output() public onAulaIndexChange = new EventEmitter<any>();
  @Output() public onAulaDeleted = new EventEmitter<any>();
  public labelsMatutino: ILabel[] = [];
  public labelsVespertino: ILabel[] = [];
  public labelsNoturno: ILabel[] = [];
  highlightedRowIndex: number = -1;
  highlightedColIndex: number = -1;

  constructor(private dataService: DataService,
              private notification: NotificationService) {
  }

  ngOnInit() {
    this.dataService.getAll('label').pipe(first()).subscribe(
      (result: ILabel[]) => {
        result = result.sort((a, b) => a.numero > b.numero ? 1 : -1);
        this.labelsMatutino = result.filter(r => r.turno === 0);
        this.labelsVespertino = result.filter(r => r.turno === 1);
        this.labelsNoturno = result.filter(r => r.turno === 2);
      },
      error => this.notification.handleError(error)
    );
  }

  onDragOver(event: DragEvent, rowIndex: number, colIndex: number) {
    event.preventDefault();
    this.highlightedRowIndex = rowIndex;
    this.highlightedColIndex = colIndex;
  }

  onDragLeave(event: DragEvent, rowIndex: number, colIndex: number) {
    event.preventDefault();
    if (this.highlightedRowIndex === rowIndex && this.highlightedColIndex === colIndex) {
      this.highlightedRowIndex = -1;
      this.highlightedColIndex = -1;
    }
  }
  onDragStart(event: DragEvent, rowIndex: number, colIndex: number) {
    const target = event.target as HTMLElement;
    target.classList.add('dragging');
    event.dataTransfer?.setData('text/plain', JSON.stringify({ rowIndex, colIndex }));
  }

  onDragEnd(event: DragEvent) {
    event.preventDefault();
    const target = event.target as HTMLElement;
    target.classList.remove('dragging');
  }

  onItemDrop(event: DragEvent, rowIndex: number, colIndex: number) {
    if(this.novaAula !== undefined) {
      if(!this.checkDraggableArea(this.novaAula, colIndex)) return;
      let aula = {
        alocacao: this.novaAula.alocacao,
        id: null,
        dia: rowIndex,
        numero: colIndex + this.novaAula.turno,
        turno: this.novaAula.turno,
        oferta: this.novaAula.oferta,
        turma: this.novaAula.turma
      }
      this.onNovaAulaCreated.emit(aula);
    } else {
      const data = JSON.parse(event.dataTransfer?.getData('text/plain') || '');
      const prevRowIndex = data.rowIndex;
      const prevColIndex = data.colIndex;
      if (prevRowIndex === rowIndex && prevColIndex === colIndex) {
        return; // O elemento não mudou de posição
      }
      const prevItem = this.matriz[prevRowIndex][prevColIndex];
      const currItem = this.matriz[rowIndex][colIndex];
      if(currItem !== undefined && !(typeof currItem === 'string')) {
        if(!this.checkDraggableArea(currItem, colIndex)) return;
        currItem.dia = prevRowIndex;
        currItem.numero = prevColIndex + currItem.turno;
      }
      if(prevItem !== undefined && !(typeof prevItem === 'string')) {
        if(!this.checkDraggableArea(prevItem, colIndex)) return;
        prevItem.dia = rowIndex;
        prevItem.numero = colIndex + prevItem.turno;
      }

      this.onAulaIndexChange.emit({
        prevItem: {
          prevRowIndex: prevRowIndex,
          prevColIndex: prevColIndex,
          item: prevItem
        },
        currItem: {
          rowIndex: rowIndex,
          colIndex: colIndex,
          item: currItem
        }
      })
    }

    this.highlightedRowIndex = -1;
    this.highlightedColIndex = -1;
  }
  onClick(event: MouseEvent, i: number, j: number) {
    event.preventDefault();
    if(this.highlightedRowIndex == i && this.highlightedColIndex == j) {
      this.highlightedColIndex = -1;
      this.highlightedRowIndex = -1;
    } else {
      this.highlightedColIndex = j;
      this.highlightedRowIndex = i;
    }
  }

  getNomeEncurtadoProfessor(nome: string) {
    if(nome != null) {
      let nomes = nome.split(' ');
      let siglas = nomes.map(n => n.substring(0, 1)).splice(1).join('');
      let nomeEncutado = nomes[0] + ' ' + siglas;
      return nomeEncutado;
    }

    return '';
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

  verificarAula(item: any): string {
    if (!(typeof item === 'string')) {
      let aula = undefined;

      if(item.alocacao.disciplina.tipo === 'ESPECIAL') {
        aula = this.aulasConflitantes.find(a => a.numero === item.numero &&
          a.dia === item.dia &&
          a.turno === item.turno &&
          a.alocacao.professor1?.id === item.alocacao.professor1.id &&
          a.alocacao.professor2?.id === item.alocacao.professor2?.id);
      } else {
        aula = this.aulasConflitantes.find(a => a.numero === item.numero &&
          a.dia === item.dia &&
          a.turno === item.turno &&
          a.alocacao.professor1?.id === item.alocacao.professor1.id);
      }
      if (aula !== undefined) {
        switch (aula.tipo as number) {
          case 1: return 'background-red';
          case 2:
            const aulaVermelha = this.aulasConflitantes.find(a => a.dia == aula.dia && a.numero == aula.numero && a.tipo == 1);
            if (aulaVermelha != undefined) return 'background-red';
            return 'background-yellow';
          case 3:
            const aulaVermelha2 = this.aulasConflitantes.find(a => a.dia == aula.dia && a.numero == aula.numero && a.tipo == 1);
            const aulaLaranja = this.aulasConflitantes.find(a => a.dia == aula.dia && a.numero == aula.numero && a.tipo == 2);
            if (aulaVermelha2 != undefined) return 'background-red';
            else if (aulaLaranja != undefined) return 'background-yellow';
            else return 'background-blue';
        }
      }
    }
    return 'background-white';
  }

  private checkDraggableArea(item: any, colIndex: number) {
    if(item.turno == 12 && colIndex > 3) {
      return false;
    }

    return true;
  }

  getColspan() {
    return this.turno?.toUpperCase() === 'NOTURNO' ? 4 : 6
  }

  deleteAula(item: any) {
    this.onAulaDeleted.emit(item);
    this.highlightedRowIndex = -1;
    this.highlightedColIndex = -1;
  }

  getLabel(label: ILabel) {
    return label?.inicio.toString().substring(0, 5) + ' a ' + label?.fim.toString().substring(0, 5);
  }
}
