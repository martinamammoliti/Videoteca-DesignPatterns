public class ModificaFilmCommand implements Command {
    private final Videoteca videoteca;
    private final int id;
    private finale FilmIF nuovoFilm;

    public ModificaFilmCommand(Videoteca videoteca, int id, FilmIF nuovoFilm){
        this.videoteca=videoteca;
        this.id=id;
        this.FilmIF=nuovoFilm;
    }

    @Override
    public void execute(){
        videoteca.modifica(id, nuovoFilm);
    }
}
