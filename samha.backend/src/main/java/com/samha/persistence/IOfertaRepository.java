package com.samha.persistence;

import com.samha.domain.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOfertaRepository extends JpaRepository<Oferta, Long> {
    List<Oferta> findOfertasByAnoAndSemestre(Integer ano, Integer semestre);
    Oferta findFirstByAnoAndSemestreAndPublica(Integer ano, Integer Semestre, Boolean publica);
}
