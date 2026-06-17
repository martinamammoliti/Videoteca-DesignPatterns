package strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.FilmIF;

public class OrdinamentoStrategy implements FilmQueryStrategy{
    private final String criterio;

    public OrdinamentoStrategy(String criterio){
        this.criterio=criterio.toUpperCase();
    }

    @Override
    public List<FilmIF> eseguiQuery(List<FilmIF> lista){
        List<FilmIF> ordinata=new ArrayList<>(lista);
        switch (criterio) {
            case "TITOLO":
                ordinata.sort(Comparator.comparing(f -> f.getTitolo().toLowerCase()));
                break;
            case "ANNO":
                ordinata.sort(Comparator.comparingInt(FilmIF::getAnnoUscita));
                break;
            case "VALUTAZIONE":
                ordinata.sort((f1, f2) -> Integer.compare(f2.getValutazione(), f1.getValutazione()));
                break;
            default:
                break;
        }
        return ordinata;
    }

}
