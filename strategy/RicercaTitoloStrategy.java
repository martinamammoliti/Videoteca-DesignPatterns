package strategy;

import java.util.List;
import model.*;
public class RicercaTitoloStrategy implements FilmQueryStrategy{
    private final String titoloTarget;

    public RicercaTitoloStrategy(String titoloTarget){
        this.titoloTarget=titoloTarget.trim().toLowerCase();
    }

    @Override
    public List<FilmIF> eseguiQuery(List<FilmIF> film){
        return film.stream().filter(f -> f.getTitolo().toLowerCase().contains(titoloTarget.toLowerCase())).toList();
    }

}
