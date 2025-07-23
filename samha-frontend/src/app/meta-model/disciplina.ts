import {TableColumnModel} from './table-column-model';
import {commonLogColumns} from './log';

export const disciplinaColumns: TableColumnModel[] = [
  {columnDef: 'id', header: 'Id', visible: false},
  {columnDef: 'nome', header: 'Nome', visible: true},
  {columnDef: 'sigla', header: 'Sigla', visible: true},
  {columnDef: 'matriz.curso.nome', header: 'Curso', visible: true},
  {columnDef: 'tipo', header: 'Tipo', visible: true},
  {columnDef: 'cargaHoraria', header: 'Carga Horária', visible: true},
  {columnDef: 'qtAulas', header: 'Quantidade de Aulas', visible: true},
  {columnDef: 'periodo', header: 'Período', visible: true}
]

export const disciplinaLogColumns: TableColumnModel[] = [
  ...commonLogColumns,
  ...disciplinaColumns.filter(column => column.columnDef !== 'id')
]
