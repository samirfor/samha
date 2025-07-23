package com.samha.service;

import com.samha.domain.Aula;
import com.samha.domain.Label;
import com.samha.domain.Label_;
import com.samha.persistence.generics.IGenericRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HorarioService {

    public static Integer ONZE_HORAS_EM_AULAS = 14;
    public static final String SEGUNDA = "SEGUNDA-FEIRA";
    public static final String TERCA = "TERÇA-FEIRA";
    public static final String QUARTA = "QUARTA-FEIRA";
    public static final String QUINTA = "QUINTA-FEIRA";
    public static final String SEXTA = "SEXTA-FEIRA";
    public static final int TEMPO_MAXIMO = 1;
    public static final int INTERVALO_MINIMO = 2;

    @Inject
    private IGenericRepository genericRepository;

    public Double getTotalHorasDisciplina(double horas) {
        Double horasDisciplina60horas = 3D;
        Double minutosDisciplina60horas = 20 / 60D;
        Double total = horasDisciplina60horas + minutosDisciplina60horas;
        return (horas * total) / 60;
    }

    public String getHorarioFinal(int numero) {
        return genericRepository.findSingle(Label.class, q -> q.where(
                q.equal(q.get(Label_.numero), numero)
        )).getFim().toString();
    }

    public String getHorarioInicial(int numero) {
        return genericRepository.findSingle(Label.class, q -> q.where(
                q.equal(q.get(Label_.numero), numero)
        )).getInicio().toString();
    }

    public double obterQuantidadeHoras(Aula primeira, Aula ultima, int flag) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

        LocalTime inicio = genericRepository.findSingle(Label.class, q -> q.where(
                q.equal(q.get(Label_.numero), primeira.getNumero()),
                q.equal(q.get(Label_.turno), deParaTurno(primeira.getTurno()))
        )).getInicio();
        LocalTime fim = genericRepository.findSingle(Label.class, q -> q.where(
                q.equal(q.get(Label_.numero), ultima.getNumero()),
                q.equal(q.get(Label_.turno), deParaTurno(ultima.getTurno()))
        )).getFim();

        double qtHoras = calcularDiferencaHoras(inicio, fim, formato);

        if (flag == HorarioService.INTERVALO_MINIMO)
            return modificarQuantidadeHorasIntervaloMinimo(fim, inicio, qtHoras);
        else
            return qtHoras;
    }

    private int deParaTurno(int turno) {
        switch (turno) {
            case 0: return 0;
            case 6: return 1;
            case 12: return 2;
            default: return -1;
        }
    }

    public double calcularDiferencaHoras(LocalTime inicio, LocalTime fim, DateTimeFormatter formato) {

        LocalTime diferenca = fim.minusHours(inicio.getHour()).minusMinutes(inicio.getMinute());
        String dif = diferenca.format(formato);
        String[] parts = dif.split(":");
        Integer hour = Integer.parseInt(parts[0]);
        Integer minutes = Integer.parseInt(parts[1]);
        //porcentagem de uma hora

        return hour + (minutes / 60D);
    }

    public double modificarQuantidadeHorasIntervaloMinimo(LocalTime fim, LocalTime inicio, double qtHoras) {

        if (fim.getHour() == inicio.getHour()) {

            if (fim.getMinute() < inicio.getMinute()) {
                return qtHoras;
            }

        } else if (fim.getHour() < inicio.getHour()) {
            return qtHoras;
        }

        return 24 - qtHoras;
    }

    public LocalTime getTimeFromDouble(double tempo) {
        Double tempoClass = tempo;
        Long hour = tempoClass.longValue();
        Long minutes =  Math.round((tempo - hour) * 60);
        return LocalTime.of(hour.intValue(), minutes.intValue());

    }

    public static String obterStringDia(int dia){

        switch(dia){

            case 0:
                return SEGUNDA;
            case 1:
                return TERCA;
            case 2:
                return QUARTA;
            case 3:
                return QUINTA;
            default:
                return SEXTA;
        }
    }

    public Map<String, String> getGenericReportLabels(String turno, int type) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

        List<Label> labels = genericRepository.findAll(Label.class).stream().sorted(Comparator.comparing(Label::getNumero)).collect(Collectors.toList());;
        Label labelNoturno = labels.stream().filter(l -> l.getNumero().equals(11) && l.getTurno().equals(2)).findFirst().get();

        Map<String, String> labelMap = new HashMap<>();

        for (var label : labels) {
            if (label.getTurno().equals(2) && label.getNumero().equals(11)) continue;
            if (label.getNumero().equals(11) && turno.equalsIgnoreCase("NOTURNO") && type == 1) {
                String horaInicioConcatenada = label.getInicio().format(formato) + "\n" + label.getFim().format(formato);
                String horaFimConcatenada = labelNoturno.getInicio().format(formato) + "\n" + labelNoturno.getFim().format(formato);
                labelMap.put("A" + label.getNumero(), horaInicioConcatenada + "\n --- \n" + horaFimConcatenada);
                labelMap.put("A" + label.getNumero() + "2", " ");
            } else if (label.getNumero().equals(11) && turno.equalsIgnoreCase("NOTURNO") && type == 2) {
                labelMap.put("A" + label.getNumero() + "2", labelNoturno.getInicio().format(formato) + " " + labelNoturno.getFim().format(formato));
                labelMap.put("A" + label.getNumero(), " ");
            } else if (label.getNumero().equals(11) && !turno.equalsIgnoreCase("NOTURNO")) {
                labelMap.put("A" + label.getNumero() + "2", label.getInicio().format(formato) + " " + label.getFim().format(formato));
                labelMap.put("A" + label.getNumero(), " ");
            }else {
                labelMap.put("A" + label.getNumero(), label.getInicio().format(formato) + " " + label.getFim().format(formato));
            }
        }

        return labelMap;
    }
}
