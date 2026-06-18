package strategy;

import java.util.List;

import model.*;


public class FilmQueryContext{
    private FilmQueryStrategy strategy;

    public void setStrategy(FilmQueryStrategy strategy){
        this.strategy=strategy;
    }

    public List<FilmIF> eseguiQuery(List<FilmIF> catalogo){
        if(strategy==null)
            return catalogo;
        return strategy.eseguiQuery(catalogo);
    }

}
