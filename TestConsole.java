import facade.VideotecaFacade;
import model.DatiFilm;
import model.StatoVisione;
import model.Videoteca;
import view.VistaTabellare;

public class TestConsole {
    public static void main(String[] args) {
        // 1. Configurazione iniziale
        Videoteca videoteca = new Videoteca();
        VideotecaFacade facade = new VideotecaFacade(videoteca);

        // 2. Avviamo la vista tabellare
        VistaTabellare vista = new VistaTabellare(videoteca, facade);

        // 3. Proviamo a caricare i dati esistenti dal file
        System.out.println("Tentativo di caricamento dati...");
        try {
            facade.caricaDati(); 
        } catch (Exception e) {
            System.out.println("Nessun file di salvataggio trovato. Partiamo da catalogo vuoto.");
        }

        // 4. SIMULAZIONE: Inseriamo un film per vedere se l'Observer reagisce da solo
        System.out.println("\n[Test] Inserimento di un nuovo film tramite Facade...");
        
        // Sostituisci i parametri con i campi reali del tuo costruttore DatiFilm
        DatiFilm nuovoFilm = new DatiFilm("Inception", "Christopher Nolan", 2010, "Sci-Fi", 5, StatoVisione.VISTO);
        facade.inserisciFilm(nuovoFilm);

        // 5. SIMULAZIONE: Proviamo a fare l'Undo del film appena inserito
        System.out.println("\n[Test] Esecuzione dell'operazione di ANNULLA (Undo)...");
        facade.undo();
    }
}