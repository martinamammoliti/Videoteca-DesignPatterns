import java.util.ArrayList;

public class Videoteca {
    private final List<FilmIF> elencoFilm=new ArrayList<>();
    
    public void inserisci(FilmIF film){
        elencoFilm.add(film);
        System.out.println("Film inserito: "+film.getTitolo());
    }

    public void modifica (int id, FilmIF nuovoFilm){
        for(int i=0; i<elencoFilm.size(); i++){
            if(elencoFilm.get(i).getId()==id){
                elencoFilm.set(i, nuovoFilm);
                System.out.println("Film modificato con ID: "+id);
                return;
            }
        }
    }

    public void elimina(int id){
        elencoFilm.removeIf(film -> film.getId()==id);
        System.out.println("Film eliminato con ID: "+id);
    }

    public List<FilmIF> getElenco(){
        return new ArrayList<>(elencoFilm;
    }

}
