import {TableColumnModel} from './table-column-model';
import {FieldEnum} from '../shared/field-enum';
import {servidorColumns} from './servidor';

export const professorColumns: TableColumnModel[] = [
  {columnDef: 'id', header: 'id', visible: false},
  ...servidorColumns.filter(column => column.columnDef !== 'id'),
  {columnDef: 'cargaHoraria', header: 'CargaHoraria', visible: true},
  {columnDef: 'ativo', header: 'Ativo', visible: true, type: FieldEnum.BOOLEAN}
]

export const professorLogColumns: TableColumnModel[] = [
  {columnDef: 'cargaHoraria', header: 'CargaHoraria', visible: true},
  {columnDef: 'ativo', header: 'Ativo', visible: true, type: FieldEnum.BOOLEAN},
  {columnDef: 'pk.professor_id', header: 'ID', visible: true},
  {columnDef: 'pk.rev', header: 'Revisão', visible: true},
]
