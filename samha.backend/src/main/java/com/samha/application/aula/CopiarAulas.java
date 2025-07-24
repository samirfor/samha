package com.samha.application.aula;

import com.samha.commons.UseCase;
import com.samha.domain.Aula;
import com.samha.domain.Aula_;
import com.samha.domain.Oferta;
import com.samha.domain.Oferta_;
import com.samha.persistence.generics.IGenericRepository;

import javax.inject.Inject;
import java.util.List;

public class CopiarAulas extends UseCase<Aula> {
    
    private int turmaId;
    private int ano;
    private int semestre;
    private int ofertaIdDestino;

    @Inject
    private IGenericRepository genericRepository;

    @Inject
    public CopiarAulas(int turmaId, int ano, int ofertaIdDestino) {
        this.turmaId = turmaId;
        this.ano = ano;
        this.semestre = 1; // sempre copia as aulas do primeiro semestre
        this.ofertaIdDestino = ofertaIdDestino;
    }

    @Override
    protected Aula execute() throws Exception {
        excluiAulasSegundoSemestre();

        insereAulasSegundoSemestre();
        
        return null;
    }

    private void excluiAulasSegundoSemestre()throws Exception{
        List <Aula> aulas = genericRepository.find(Aula.class, 
            q -> q.where(
                q.equal(q.get(Aula_.oferta), ofertaIdDestino)
            )
        );
        
        if(aulas.isEmpty()){
            return;
        }

        aulas.forEach(aula ->{
            genericRepository.delete(aula);
        });
    }

    private void insereAulasSegundoSemestre()throws Exception{
        Oferta ofertaOrigem = genericRepository.findSingle(Oferta.class, 
            q -> q.where(
                q.equal(q.get( Oferta_.turma), turmaId),
                q.equal(q.get(Oferta_.semestre), semestre), 
                q.equal(q.get(Oferta_.ano), ano)
            )
        );

        Oferta ofertaDestinio = genericRepository.findSingle(Oferta.class, 
            q -> q.where(
                q.equal(q.get(Oferta_.id), ofertaIdDestino)
            )
        );

        if(ofertaOrigem == null){
            throw new Exception("Não foi possível identificar a oferta com as aulas a serem copiadas.[CopiaAulas.insereAulasSegundoSemestre]");
        }

        List <Aula> aulas = genericRepository.find(Aula.class, 
            q -> q.where(
                q.equal(q.get(Aula_.oferta), ofertaOrigem.getId())
            )
        );

        aulas.forEach(aula ->{
            insereAulaProximoSemestre(aula, ofertaDestinio);
        });
    }

    private void insereAulaProximoSemestre(Aula aulaSemAnterior, Oferta ofertaDestino) {
        Aula aula = new Aula();

        aula.setDia(aulaSemAnterior.getDia());
        aula.setNumero(aulaSemAnterior.getNumero());
        aula.setTurno(aulaSemAnterior.getTurno());
        aula.setAlocacao(aulaSemAnterior.getAlocacao());
        aula.setOferta(ofertaDestino);
        
        genericRepository.save(aula); 
    }
}