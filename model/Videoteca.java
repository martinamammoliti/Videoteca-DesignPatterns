package model;
import java.util.ArrayList;
import java.util.List;

import observer.Observer;

public class Videoteca {
    private final List<FilmIF> elencoFilm=new ArrayList<>();

    public void inserisci(FilmIF film){
        elencoFilm.add(film);
        System.out.println("Film inserito: "+film.getTitolo());
    }

    public void modifica (int id, DatiFilm nuoviDati){
        FilmIF film = trovaFilmPerId(id);
        if (film != null) {
            film.aggiornaDati(nuoviDati);
        }
    }

    public void elimina(int id){
        elencoFilm.removeIf(film -> film.getId()==id);
        System.out.println("Film eliminato con ID: "+id);
    }

    public List<FilmIF> getElenco(){
        return new ArrayList<>(elencoFilm);
    }

    public FilmIF trovaFilmPerId(int id) {
        return elencoFilm.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
    }

    public void svuota() {
        elencoFilm.clear();
    }

}
