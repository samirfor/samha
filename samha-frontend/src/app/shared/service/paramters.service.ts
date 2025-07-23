import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root' // garante que é singleton na aplicação inteira
})
export class ParamtersService {

  private cursoSelecionado: any = null;
  private anoSelecionado: any = null;
  private semSelecionado: any = null;

  setCursoSelecionado(curso: any) {
    this.cursoSelecionado = curso;
  }

  getCursoSelecionado() {
    return this.cursoSelecionado;
  }

  setAnoSelecionado(ano: any){
    this.anoSelecionado = ano;
  }

  getAnoSelecionado(){
    return this.anoSelecionado
  }

  setSemSelecionado(sem: any){
    this.semSelecionado = sem;
  }

  getSemestreSelecionado(){
    return this.semSelecionado;
  }

}
