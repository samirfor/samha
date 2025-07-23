package com.samha.persistence;

import com.samha.domain.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface IAulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> getAulasByOferta_Id(Long ofertaId);


    @Query(value = """
        select	au.*
        from	oferta o
        join	aula au
                on	o.id		= au.oferta_id
                and	au.dia          = :pDia
                and	au.numero       = :pNumero
                and	au.oferta_id	<> :pOfertaId
        join	alocacao a
                on  au.alocacao_id	= a.id
                and o.ano		= :pAno
                and o.semestre		= :pSemestre
                and	(  a.professor1_id	= :pProfessorId 
                        or a.professor2_id	= :pProfessorId
                        )
        join	turma t
		on	o.turma_id	= t.id
                and	t.ativa         = 1
        where	o.ano			= :pAno
        and	o.semestre		= :pSemestre
    """, nativeQuery = true)
    List<Aula> buscaAulaMesmoHorario(@Param("pOfertaId") Long pOfertaId, 
                                    @Param("pDia") int pDia, 
                                    @Param("pNumero") int pNumero, 
                                    @Param("pAno") int pAno, 
                                    @Param("pSemestre") int pSemestre, 
                                    @Param("pProfessorId") Long pProfessorId);
}
