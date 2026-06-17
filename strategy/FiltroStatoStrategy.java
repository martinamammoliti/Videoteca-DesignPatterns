package strategy;

import model.*;
import java.util.List;
import java.util.stream.Collectors;

import model.StatoVisione;

public class FiltroStatoStrategy implements FilmQueryStrategy{
    private final StatoVisione stato;

    public FiltroStatoStrategy(StatoVisione stato){
        this.stato=stato;
    }

    @Override
    public List<FilmIF> eseguiQuery(List<FilmIF> lista){
        return lista.stream()
                .filter(f -> f.getStatoVisione()==stato)
                .collect(Collectors.toList());
    }

}
