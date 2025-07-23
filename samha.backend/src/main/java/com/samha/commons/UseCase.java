package com.samha.commons;

import lombok.Data;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class UseCase<ENTITY> implements Serializable {

    private final List<UseCase> executeBefore = new ArrayList<>();
    private final List<UseCase> executeAfter = new ArrayList<>();

    protected abstract ENTITY execute() throws Exception;

    protected void addBefore(UseCase useCase){
        this.executeBefore.add(useCase);
    }

    protected void addAfter(UseCase useCase){
        this.executeAfter.add(useCase);
    }

    public static Map<String, Object> convertTupleToMap(Tuple tuple) {
        Map<String, Object> mapa = new HashMap<>();
        for(TupleElement element : tuple.getElements()){
            String alias = element.getAlias();
            if(alias.indexOf(".") >= 0){
                String[] aliasPath = alias.split("\\.");
                Map<String, Object> subMapa = new HashMap<>();
                if(mapa.containsKey(aliasPath[0])){
                    subMapa = (Map<String, Object>) mapa.get(aliasPath[0]);
                }
                mapa.put(aliasPath[0], subMapa);

                for(int i=1; i<aliasPath.length-1; i++){
                    Map<String, Object> novoMapa = new HashMap<>();
                    subMapa.put(aliasPath[i], novoMapa);
                    subMapa = novoMapa;
                }

                subMapa.put(aliasPath[aliasPath.length-1], tuple.get(element.getAlias()));

            }else {
                mapa.put(alias, tuple.get(element.getAlias()));
            }

        }
        return mapa;
    }
}
