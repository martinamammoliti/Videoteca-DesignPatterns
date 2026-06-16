public class RimuoviFilmCommand implements Command {
    private final Videoteca videoteca;
    private final int id;

    public RimuoviFilmCommand(Videoteca videoteca, int id){
        this.videoteca=videoteca;
        this.id=id;
    }

    @Override
    public void execute(){
        videoteca.elimina(id);
    }
}
