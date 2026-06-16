package strategy;

import java.util.List;

public interface FilmQueryStrategy {
    List<FilmIF> eseguiQuery(List<FilmIF> film);
}
