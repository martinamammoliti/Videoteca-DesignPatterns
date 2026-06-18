package strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.FilmIF;

public class OrdinamentoValutazioneStrategy implements FilmQueryStrategy{
    @Override
    public List<FilmIF> eseguiQuery(List<FilmIF> film) {
        List<FilmIF> ordinata = new ArrayList<>(film);
        ordinata.sort(Comparator.comparingInt(FilmIF::getValutazione).reversed()); // Decrescente (da 5 a 1 stelle)
        return ordinata;
    }

}
