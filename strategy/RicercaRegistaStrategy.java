package strategy;

import model.*;
import java.util.List;

public class RicercaRegistaStrategy implements FilmQueryStrategy{
    private final String regista;

    public RicercaRegistaStrategy(String regista){
        this.regista=regista.toLowerCase();
    }

    @Override
    public List<FilmIF> eseguiQuery(List<FilmIF> lista){
        return lista.stream()
                .filter(f -> f.getRegista().toLowerCase().contains(regista.toLowerCase()))
               .toList();
    }

}
