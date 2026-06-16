package model;
import java.util.ArrayList;
import java.util.List;
import view.Observer;

public class Videoteca {
    private final List<FilmIF> elencoFilm=new ArrayList<>();
    private final List<Observer> observers=new ArrayList<>();

    public void attach(Observer o){
        if(o!=null && !observers.contains(o))
            observers.add(o);
    }

    public void detach(Observer o){
        observers.remove(o);
    }

    public void notifyObservers(){
        for(Observer o:observers)
            o.update();
    }

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

    public void setElenco(List<FilmIF> nuovaLista){
        this.elencoFilm.clear();
        this.elencoFilm.addAll(nuovaLista);
        System.out.println("Intero catalogo ripristinato/sostituito.");
        notifyObservers();
    }

    public List<FilmIF> getElenco(){
        return new ArrayList<>(elencoFilm;
    }

}
