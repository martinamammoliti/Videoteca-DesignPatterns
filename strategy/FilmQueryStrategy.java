package strategy;

import java.util.List;
import model.*;

public interface FilmQueryStrategy {
    List<FilmIF> eseguiQuery(List<FilmIF> film);
}
