package com.samha.application.aula;

import com.samha.commons.UseCase;
import com.samha.domain.Aula;
import com.samha.domain.Aula_;
import com.samha.domain.Oferta;
import com.samha.domain.Oferta_;
import com.samha.domain.dto.OfertaDto;
import com.samha.persistence.IAulaRepository;
import com.samha.persistence.generics.IGenericRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SalvarAulas extends UseCase<List<Aula>> {

    private OfertaDto ofertaDto;
    private Oferta oferta;

    @Inject
    public SalvarAulas(OfertaDto ofertaDto) {
        this.ofertaDto = ofertaDto;
    }

    @Inject
    private IGenericRepository genericRepository;

    @Inject
    private IAulaRepository aulaRepository;

    @Override
    protected List<Aula> execute() throws Exception {
        this.oferta = genericRepository.get(Oferta.class, ofertaDto.getOfertaId());

        List<Aula> aulasBanco = new ArrayList<>();

        if (oferta != null) {
            aulasBanco = genericRepository.find(Aula.class, q -> q.where(
                    q.equal(q.get(Aula_.oferta).get(Oferta_.id), oferta.getId())));
        }

        aulasBanco.stream().forEach(a -> {
            Optional<Aula> aula = ofertaDto.getAulas().stream().filter(a2 -> a2.getId() != null).filter(a1 -> a1.getId().equals(a.getId())).findFirst();
            if (aula.isPresent()) {
                Aula aulaEncontrada = aula.get();
                boolean aulaMudou = this.verificarMudancaAula(a, aulaEncontrada);
                if (aulaMudou) aulaRepository.save(aulaEncontrada);
            } else {
                genericRepository.delete(a);
            }
        });

        List<Aula> novasAulas = ofertaDto.getAulas().stream().filter(a -> a.getId() == null).collect(Collectors.toList());

        aulaRepository.saveAll(novasAulas);

        return aulaRepository.getAulasByOferta_Id(oferta.getId());
    }

    private boolean verificarMudancaAula(Aula a1, Aula a2) {
        return a2.getDia() != a1.getDia() || a2.getNumero() != a1.getNumero();
    }
    
}
