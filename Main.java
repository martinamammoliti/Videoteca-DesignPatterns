import java.util.List;

import facade.VideotecaFacade;
import model.Videoteca;
import view.VistaTabellare;

public class Main {
    public static void main(String[] args) {
        System.out.println("Avvio dell'applicazione Videoteca...\n");
        Videoteca modelloVideoteca=new Videoteca();
        VistaTabellare vistaTabellare=new VistaTabellare(modelloVideoteca);

        VideotecaFacade facade=new VideotecaFacade(modelloVideoteca);

        System.out.println("- ESEGUO: Inserimento di 2 Film -");
        DatiFilm f1=new DatiFilm("Inception", "Christopher Nolan", 2010, "Sci-Fi", 9, StatoVisione.VISTO);
        DatiFilm f2 = new DatiFilm("Interstellar", "Christopher Nolan", 2014, "Sci-Fi", 10, StatoVisione.DA_VEDERE);

        facade.inserisciFilm(f1); 
        facade.inserisciFilm(f2);

        System.out.println("--- ESEGUO: Modifica del Film con ID 1 (Cambio stato in IN_VISIONE) ---");
        DatiFilm f1Modificato = new DatiFilm("Inception", "Christopher Nolan", 2010, "Sci-Fi", 9, StatoVisione.IN_VISIONE);

        facade.modificaFilm(1, f1Modificato);

        System.out.println("- ESEGUO: Ricerca mirata del titolo 'Interstellar' -");
        List<FilmIF> risultatiRicerca = facade.cercaPerTitolo("Interstellar");
        System.out.println("Risultati trovati dalla strategia di ricerca: " + risultatiRicerca.size());
        for(FilmIF f : risultatiRicerca) {
            System.out.println(" > Trovato: " + f.getTitolo() + " [" + f.getAnnoUscita() + "]");
        }
        System.out.println();

        System.out.println("- ESEGUO: Rimozione del Film con ID 1 -");
        facade.rimuoviFilm(1);

        System.out.println("- ESEGUO: Caricamento del backup da HD -");
        facade.caricaDati();
    }

}
