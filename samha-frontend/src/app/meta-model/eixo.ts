import {TableColumnModel} from './table-column-model';
import {commonLogColumns} from './log';

export const eixoColumns: TableColumnModel[] = [
  {columnDef: 'id', header: 'id', visible: false},
  {columnDef: 'nome', header: 'Nome', visible: true}
]

export const eixoLogColumns: TableColumnModel[] = [
  ...commonLogColumns,
  {columnDef: 'nome', header: 'Nome', visible: true}
]
