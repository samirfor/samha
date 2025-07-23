import {TableColumnModel} from './table-column-model';
import {commonLogColumns} from './log';

export const matrizColumns: TableColumnModel[] = [
  {columnDef: 'id', header: 'id', visible: false},
  {columnDef: 'nome', header: 'Nome', visible: true},
  {columnDef: 'ano', header: 'Ano', visible: true},
  {columnDef: 'semestre', header: 'Semestre', visible: true},
  {columnDef: 'curso.nome', header: 'Curso', visible: true},
  {columnDef: 'curso.id', header: 'curso.id', visible: false},
  {columnDef: 'curso.qtPeriodos', header: 'curso.qtPeriodos', visible: false}
]

export const matrizLogColumns: TableColumnModel[] = [
  ...commonLogColumns,
  ...matrizColumns.filter(column => column.columnDef !== 'id')
]
