package strategy;

import java.util.ArrayList;

public class RicercaTitoloStrategy implements FilmQueryStrategy{
    private final String titoloTarget;

    public RicercaTitoloStrategy(String titoloTarget{
        this.titoloTarget=titoloTarget;
    }

    @Override
    public List<FilmIF> eseguiQuery(List<FilmIF> film){
        List<FilmIF> risultato=new ArrayList<>();
        for(FilmIF f:film){
            if(f.getTitolo().equalsIgnoreCase(titoloTarget)){
                risultato.add(f);
            }
        }
        return risultato;
    }

}
