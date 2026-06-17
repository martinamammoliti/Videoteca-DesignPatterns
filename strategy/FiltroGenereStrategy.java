package strategy;

import model.*;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroGenereStrategy implements FilmQueryStrategy{
    private final String genere;

    public FiltroGenereStrategy(String genere){
        this.genere=genere;
    }

    @Override
    public List<FilmIF> eseguiQuery(List<FilmIF> lista){
        return lista.stream()
                .filter(f -> f.getGenere().toLowerCase().contains(genere))
                .collect(Collectors.toList());
    }
}
