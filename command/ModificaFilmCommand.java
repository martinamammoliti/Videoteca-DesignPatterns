package command;
import model.*;

public class ModificaFilmCommand implements Command {
    private final Videoteca videoteca;
    private final int id;
    private final DatiFilm nuoviDati;
    private DatiFilm vecchiDati;

    public ModificaFilmCommand(Videoteca videoteca, int id, DatiFilm dati) {
        this.videoteca = videoteca;
        this.id = id;
        this.nuoviDati = dati;
    }

    @Override
    public boolean doIt() {
        FilmIF film = videoteca.trovaFilmPerId(id);
        if (film != null) {
            this.vecchiDati = new DatiFilm(
                film.getTitolo(),
                film.getRegista(),
                film.getAnnoUscita(),
                film.getGenere(),
                film.getValutazione(),
                film.getStatoVisione()
            );
            
            videoteca.modifica(id, nuoviDati);
            return true;
        }
        return false; 
    }

    @Override
    public void undoIt() {
        if (vecchiDati != null) {
            videoteca.modifica(id, vecchiDati);
        }
    }
}
