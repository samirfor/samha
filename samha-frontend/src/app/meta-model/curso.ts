import {TableColumnModel} from './table-column-model';
import {commonLogColumns} from './log';
import {FieldEnum} from "../shared/field-enum";

export const cursoColumns: TableColumnModel[] = [
  {columnDef: 'id', header: 'id', visible: false},
  {columnDef: 'nome', header: 'Nome', visible: true},
  {columnDef: 'qtPeriodos', header: 'Períodos', visible: true},
  {columnDef: 'nivel', header: 'Nível', visible: true},
  {columnDef: 'semestral', header: 'Semestral', visible: true, type: FieldEnum.BOOLEAN},
  {columnDef: 'coordenadoria.nome', header: 'Coordenadoria', visible: true},
  {columnDef: 'professor.nome', header: 'Coordenador', visible: true}
]

export const cursoLogColumns: TableColumnModel[] = [
  ...commonLogColumns,
  ...cursoColumns.filter(column => column.columnDef !== 'id')
]
