import {TableColumnModel} from './table-column-model';
import {FieldEnum} from "../shared/field-enum";

export const restricaoProfessorColumns: TableColumnModel[] = [
  {columnDef: 'id', header: 'id', visible: false},
  {columnDef: 'nome', header: 'Nome', visible: true},
  {columnDef: 'descricao', header: 'Descrição', visible: true},
  {columnDef: 'dia', header: 'Dia', visible: true},
  {columnDef: 'turno', header: 'Turno', visible: true},
  {columnDef: 'aula1', header: 'Aula 1', visible: true, type: FieldEnum.BOOLEAN},
  {columnDef: 'aula2', header: 'Aula 2', visible: true, type: FieldEnum.BOOLEAN},
  {columnDef: 'aula3', header: 'Aula 3', visible: true, type: FieldEnum.BOOLEAN},
  {columnDef: 'aula4', header: 'Aula 4', visible: true, type: FieldEnum.BOOLEAN},
  {columnDef: 'aula5', header: 'Aula 5', visible: true, type: FieldEnum.BOOLEAN},
  {columnDef: 'aula6', header: 'Aula 6', visible: true, type: FieldEnum.BOOLEAN},
  {columnDef: 'prioridade', header: 'Prioridade', visible: true},
]
