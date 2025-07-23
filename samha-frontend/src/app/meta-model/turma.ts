import {TableColumnModel} from './table-column-model';
import {commonLogColumns} from './log';
import {FieldEnum} from "../shared/field-enum";

export const turmaColumns: TableColumnModel[] = [
  {columnDef: 'id', header: 'id', visible: false},
  {columnDef: 'nome', header: 'Nome', visible: true},
  {columnDef: 'turno', header: 'Turno', visible: true},
  {columnDef: 'ano', header: 'Ano', visible: true},
  {columnDef: 'semestre', header: 'Semestre', visible: true},
  {columnDef: 'matriz.nome', header: 'Matriz', visible: true},
  {columnDef: 'matriz.curso.nome', header: 'Curso', visible: true},
  {columnDef: 'ativa', header: 'Ativa', visible: true, type: FieldEnum.BOOLEAN}
];

export const turmaLogColumns: TableColumnModel[] = [
  ...commonLogColumns,
  ...turmaColumns.filter(column => column.columnDef !== 'id')
]
