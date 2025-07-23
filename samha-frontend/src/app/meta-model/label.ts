import {TableColumnModel} from "./table-column-model";
import {commonLogColumns} from "./log";
import {FieldEnum} from "../shared/field-enum";

export const labelColumns: TableColumnModel[] = [
  {columnDef: 'id', header: 'Id', visible: false},
  {columnDef: 'nome', header: 'Título', visible: true},
  {columnDef: 'turno', header: 'Turno', visible: true},
  {columnDef: 'numero', header: 'Aula', visible: true},
  {columnDef: 'inicio', header: 'Início', visible: true},
  {columnDef: 'fim', header: 'Fim', visible: true}
]


export const labelLogColumns: TableColumnModel[] = [
  ...commonLogColumns,
  ...labelColumns.filter(f => f.columnDef !== 'id')
]
