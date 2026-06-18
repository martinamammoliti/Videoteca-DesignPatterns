package strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.FilmIF;

public class OrdinamentoTitoloStrategy implements FilmQueryStrategy{
    @Override
    public List<FilmIF> eseguiQuery(List<FilmIF> lista){
        List<FilmIF> ordinata=new ArrayList<>(lista);
        ordinata.sort(Comparator.comparing(f -> f.getTitolo().toLowerCase()));
        return ordinata;
    }

}
