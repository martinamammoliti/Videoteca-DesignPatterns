package command;
import model.FilmIF;
import model.Videoteca;

public class RimuoviFilmCommand implements Command {
    private final Videoteca videoteca;
    private final int id;
    private FilmIF filmRimosso;

    public RimuoviFilmCommand(Videoteca videoteca, int id) {
        this.videoteca = videoteca;
        this.id = id;
    }

    @Override
    public boolean doIt() {
        this.filmRimosso = videoteca.trovaFilmPerId(id);
        if (filmRimosso != null) {
            videoteca.elimina(id);
            return true;
        }
        return false;
    }

    @Override
    public void undoIt() {
        if (filmRimosso != null) {
            videoteca.inserisci(filmRimosso);
        }
    }
}