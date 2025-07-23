import {TableColumnModel} from "./table-column-model";
import {commonLogColumns} from "./log";
import {FieldEnum} from "../shared/field-enum";

export const aulaColumns: TableColumnModel[] = [
  {columnDef: 'id', header: 'id', visible: false},
  {columnDef: 'numero', header: 'Numero', visible: true},
  {columnDef: 'dia', header: 'Dia', visible: true},
  {columnDef: 'turno', header: 'Turno', visible: true},
  {columnDef: 'alocacao.professor1.nome', header: 'Professor 1', visible: true},
  {columnDef: 'alocacao.professor2.nome', header: 'Professor 2', visible: true},
  {columnDef: 'alocacao.disciplina.sigla', header: 'Disciplina', visible: true},
  {columnDef: 'oferta.ano', header: 'Ano', visible: true},
  {columnDef: 'oferta.semestre', header: 'Semestre', visible: true},
  {columnDef: 'oferta.turma.nome', header: 'Turma', visible: true},
]

export const aulaLogColumns: TableColumnModel[] = [
  {columnDef: 'modifiedBy', header: 'Modificado por', visible: true},
  {columnDef: 'modifiedDate', header: 'Data de Alteração', visible: true, type: FieldEnum.DATE},
  {columnDef: 'revtype', header: 'Operação', visible: true, type: FieldEnum.OPERATION},
  {columnDef: 'oferta.turma.nome', header: 'Turma', visible: true},
  {columnDef: 'oferta.ano', header: 'Ano', visible: true},
  {columnDef: 'oferta.semestre', header: 'Semestre', visible: true},
  {columnDef: 'alocacao.professor1.nome', header: 'Professor 1', visible: true},
  {columnDef: 'alocacao.professor2.nome', header: 'Professor 2', visible: true},
  {columnDef: 'alocacao.disciplina.sigla', header: 'Disciplina', visible: true},
  {columnDef: 'numero', header: 'Numero', visible: true},
  {columnDef: 'dia', header: 'Dia', visible: true},
  {columnDef: 'turno', header: 'Turno', visible: true},
  {columnDef: 'pk.id', header: 'ID', visible: true},
  {columnDef: 'pk.rev', header: 'Revisão', visible: false},
]
