export interface RelatorioDto {
  eixoId?: number,
  coordenadoriaId?: number,
  professorId?: number,
  ano: number,
  semestre: number,
  nomeRelatorio?: string,
  turmaId?: string,
  cursoId?: string,
  enviarEmail: boolean,
  senha?: string
}
