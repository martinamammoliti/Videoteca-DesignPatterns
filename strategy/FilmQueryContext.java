package strategy;

public class FilmQueryContext{
    private FilmQueryStrategy strategy;

    public void setStrategy(FilmQueryStrategy strategy){
        this.strategy=strategy;
    }

    public List<FilmIF> eseguiQuery(List<FilmIF> film){
        if(strategy==null)
            return film;
        return strategy.eseguiQuery(film);
    }

}
