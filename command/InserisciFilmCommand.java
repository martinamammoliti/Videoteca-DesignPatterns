package command;
import model.Videoteca;

public class InserisciFilmCommand implements Command{
    private final Videoteca videoteca;
    private final FilmIF film;

    public InserisciFilmCommand(Videoteca videoteca, FilmIF film){
        this.videoteca=videoteca;
        this.film=film;
    }

    @Override
    public void execute(){
        videoteca.inserisci(film);
    }
}
