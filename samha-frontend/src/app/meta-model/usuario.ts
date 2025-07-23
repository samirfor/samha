import {TableColumnModel} from './table-column-model';
import {commonLogColumns} from "./log";

export const usuarioColumns: TableColumnModel[] = [
  {columnDef: 'id', header: 'Id', visible: true},
  {columnDef: 'login', header: 'Login', visible: true},
  {columnDef: 'papel.nome', header: 'Papel', visible: true},
]

export const usuarioLogColumns: TableColumnModel[] = [
  ...commonLogColumns,
  {columnDef: 'login', header: 'Login', visible: true},
  {columnDef: 'papel.nome', header: 'Papel', visible: true},
]
