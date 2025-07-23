package com.samha.application.oferta;

import com.samha.commons.UseCase;
import com.samha.domain.Oferta;
import com.samha.persistence.IOfertaRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class MudarVisibilidadeOferta extends UseCase<Oferta> {

    private Long ofertaId;

    @Inject
    public MudarVisibilidadeOferta(Long ofertaId) {
        this.ofertaId = ofertaId;
    }

    @Inject
    private IOfertaRepository ofertaRepository;

    @Override
    protected Oferta execute() throws Exception {
        Oferta oferta = ofertaRepository.findById(ofertaId).get();
        List<Oferta> ofertas = ofertaRepository.findOfertasByAnoAndSemestre(oferta.getAno(), oferta.getSemestre()).stream().filter(o -> !o.getId().equals(oferta.getId())).collect(Collectors.toList());
        ofertas.forEach(o -> o.setPublica(!oferta.getPublica()));
        oferta.setPublica(!oferta.getPublica());
        ofertaRepository.saveAll(ofertas);
        return ofertaRepository.save(oferta);
    }
}
