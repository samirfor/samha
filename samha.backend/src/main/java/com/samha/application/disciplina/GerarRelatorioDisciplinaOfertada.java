package com.samha.application.disciplina;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;

import com.samha.commons.UseCase;
import com.samha.domain.dto.RelatorioDto;

@Async
public class GerarRelatorioDisciplinaOfertada extends UseCase<Map<String, Object>> {
 
    private RelatorioDto relatorioDto;

    @Inject
    public GerarRelatorioDisciplinaOfertada(RelatorioDto relatorioDto) {
        this.relatorioDto = relatorioDto;
    }

    @Override
    protected Map<String, Object> execute() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
    /*
select	distinct
		d.periodo
,		t.nome		as nome_turma
,		d.nome
,		c.nome 		as nome_curso
,		mc.nome		as nome_matriz
,		d.qt_vagas
,		a.turno
,		d.tipo
,		s.nome		as nome_professor1
,		(
		select	s.nome
		from	professor p2
		join	servidor s
				on	p2.professor_id		= s.id
		where	al.professor2_id		= p2.professor_id
		)			as nome_professor2
,		a.dia
,		case 
			when a.dia = 0 then 'segunda'
            when a.dia = 1 then 'terça'
            when a.dia = 2 then 'quarta'
            when a.dia = 3 then 'quinta'
            when a.dia = 4 then 'sexta'
		end			as	dia_semada
,		a.numero
,		case
			when a.numero = 0 then '07:00 - 07:50'
            when a.numero = 1 then '07:50 - 08:40'
            when a.numero = 2 then '08:40 - 09:30'
            when a.numero = 3 then '09:50 - 10:40'
            when a.numero = 4 then '10:40 - 11:30'
            when a.numero = 5 then '11:30 - 12:20'
            when a.numero = 6 then '13:00 - 13:50'
            when a.numero = 7 then '13:50 - 14:40'
            when a.numero = 8 then '14:40 - 15:30'
            when a.numero = 9 then '15:50 - 16:40'
            when a.numero = 10 then '16:40 - 17:30'
            when a.numero = 11 then '17:30 - 18:20'
            when a.numero = 12 then '18:50 - 19:35'
            when a.numero = 13 then '19:35 - 20:20'
            when a.numero = 14 then '20:30 - 21:15'
            when a.numero = 15 then '21:15 - 22:00'
		end			as horario
from	curso c
join	matriz_curricular mc
		on	c.id			= mc.curso_id
join	turma t
		on	mc.id			= t.matriz_curricular_id
join	oferta o
		on	t.id			= o.turma_id
join	aula a
		on	o.id			= a.oferta_id
join	alocacao al
		on	a.alocacao_id	= al.id
join	disciplina d
		on	al.disciplina_id	= d.id
join	professor p1
		on	al.professor1_id	= p1.professor_id
join	servidor s
		on	p1.professor_id		= s.id
where 	c.id				= 2
and		o.ano				= 2025
and		o.semestre			= 2

order by	d.periodo
,			t.nome
,			mc.nome
,			d.nome
,			a.dia
,			a.numero
;
 
    */
}
