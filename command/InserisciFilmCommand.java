package command;
import model.*;

public class InserisciFilmCommand implements Command{
    private final Videoteca videoteca;
    private final FilmIF film;

    public InserisciFilmCommand(Videoteca videoteca, FilmIF film){
        this.videoteca=videoteca;
        this.film=film;
    }

    @Override
    public boolean doIt() {
        videoteca.inserisci(film);
        return true; 
    }

    @Override
    public void undoIt() {
        videoteca.elimina(film.getId());
    }
}
