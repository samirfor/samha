package com.samha.application.oferta;

import com.samha.commons.UseCase;
import com.samha.domain.Oferta;
import com.samha.persistence.IOfertaRepository;

import javax.inject.Inject;

public class InserirOferta extends UseCase<Oferta> {

    private Oferta oferta;

    public InserirOferta(Oferta oferta) {
        this.oferta = oferta;
    }

    public InserirOferta() {}

    @Inject
    private IOfertaRepository ofertaRepository;

    @Override
    protected Oferta execute() throws Exception {
        Oferta ofertaPublica = ofertaRepository.findFirstByAnoAndSemestreAndPublica(oferta.getAno(), oferta.getSemestre(), true);
        if (ofertaPublica != null) oferta.setPublica(true);
        return ofertaRepository.save(oferta);
    }
}
