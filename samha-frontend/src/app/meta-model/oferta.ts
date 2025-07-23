import {TableColumnModel} from "./table-column-model";
import {commonLogColumns} from "./log";
import {FieldEnum} from "../shared/field-enum";

export const ofertaColumns: TableColumnModel[] = [
  {columnDef: 'id', header: 'id', visible: false},
  {columnDef: 'publica', header: 'Pública', visible: true, type: FieldEnum.BOOLEAN},
  {columnDef: 'ano', header: 'Ano', visible: true},
  {columnDef: 'semestre', header: 'Semestre', visible: true},
  {columnDef: 'tempoMaximoTrabalho', header: 'Tempo Máximo Trabalho', visible: true},
  {columnDef: 'intervaloMinimo', header: 'Intervalo Mínimo', visible: true},
  {columnDef: 'turma.nome', header: 'Turma', visible: true}
]

export const ofertaLogColumns: TableColumnModel[] = [
  ...commonLogColumns,
  ...ofertaColumns.filter(f => f.columnDef !== 'id')
]
