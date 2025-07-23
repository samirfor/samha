import {TableColumnModel} from './table-column-model';
import {commonLogColumns} from './log';

export const coordenadoriaColumns: TableColumnModel[] = [
  {columnDef: 'id', header: 'id', visible: false},
  {columnDef: 'nome', header: 'Nome', visible: true},
  {columnDef: 'eixo.nome', header: 'Eixo', visible: true}
]

export const coordenadoriaLogColumns: TableColumnModel[] = [
  ...commonLogColumns,
  ...coordenadoriaColumns.filter(column => column.columnDef !== 'id')
]
