package strategy;

import model.*;
import java.util.List;
import java.util.stream.Collectors;

public class RicercaRegistaStrategy implements FilmQueryStrategy{
    private final String regista;

    public RicercaRegistaStrategy(String regista){
        this.regista=regista.toLowerCase();
    }

    @Override
    public List<FilmIF> eseguiQuery(List<FilmIF> lista){
        return lista.stream()
                .filter(f -> f.getRegista().toLowerCase().contains(regista))
                .collect(Collectors.toList());
    }

}
