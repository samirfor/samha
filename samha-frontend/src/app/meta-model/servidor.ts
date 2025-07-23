import {TableColumnModel} from './table-column-model';
import {commonLogColumns} from './log';

export const servidorColumns: TableColumnModel[] = [
  {columnDef: 'id', header: 'id', visible: false},
  {columnDef: 'nome', header: 'Nome', visible: true},
  {columnDef: 'matricula', header: 'Matrícula', visible: true},
  {columnDef: 'email', header: 'E-mail', visible: true}
]

export const servidorLogColumns: TableColumnModel[] = [
  ...servidorColumns.filter(column => column.columnDef !== 'id'),
  ...commonLogColumns
]

export const coordenadorColumns: TableColumnModel[] = [
  ...servidorColumns,
  {columnDef: 'usuario.papel.nome', header: 'Papel', visible: true},
  {columnDef: 'usuario.login', header: 'Login', visible: true}
]
