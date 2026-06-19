package model;
import java.util.ArrayList;
import java.util.List;

import observer.Subject;

public class Videoteca extends Subject{
    private List<FilmIF> elencoFilm;

    public Videoteca(){
        this.elencoFilm=new ArrayList<>();
    }

    public void inserisci(FilmIF film){
        elencoFilm.add(film);
        System.out.println("Film inserito: "+film.getTitolo());
        this.notifyObservers();
    }

    public void modifica (int id, DatiFilm nuoviDati){
        FilmIF film = trovaFilmPerId(id);
        if (film != null) {
            film.aggiornaDati(nuoviDati);
        }
        this.notifyObservers();
    }

    public void elimina(int id){
        elencoFilm.removeIf(film -> film.getId()==id);
        System.out.println("Film eliminato con ID: "+id);
        this.notifyObservers();
    }

    public List<FilmIF> getElenco(){
        return new ArrayList<>(elencoFilm);
    }

    public void setElenco(List<FilmIF> nuovoElenco) {
        if (nuovoElenco != null) {
            this.elencoFilm = new ArrayList<>(nuovoElenco);
            this.notifyObservers();
        }
    }

    public FilmIF trovaFilmPerId(int id) {
        return elencoFilm.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
    }

    public int prossimoId() {
        int maxId = 0;
        for (FilmIF f : elencoFilm) {
            if (f.getId() > maxId) {
                maxId = f.getId();
            }
        }
        return maxId + 1;
    }

    public void svuota() {
        elencoFilm.clear();
    }

}
