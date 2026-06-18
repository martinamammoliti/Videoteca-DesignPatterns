package strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.FilmIF;

public class OrdinamentoAnnoStrategy implements FilmQueryStrategy{
    @Override
    public List<FilmIF> eseguiQuery(List<FilmIF> film) {
        List<FilmIF> ordinata = new ArrayList<>(film);
        ordinata.sort(Comparator.comparingInt(FilmIF::getAnnoUscita));
        return ordinata;
    }
}
