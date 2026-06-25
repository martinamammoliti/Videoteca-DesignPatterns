package view;

import model.*;
import observer.Observer;
import facade.*;
import java.util.List;

public class VistaTabellare implements Observer {
    private final VideotecaFacade videotecaFacade;

    public VistaTabellare(Videoteca videoteca, VideotecaFacade videotecaFacade) {
        this.videotecaFacade = videotecaFacade;
        videoteca.attach(this);
        update();
    }

    @Override
    public void update() {
        List<FilmIF> datiAggiornati = videotecaFacade.ottieniCatalogoFiltratoEOrdinato("", "", "", "Tutti", "Nessuno");
        mostra(datiAggiornati);
    }

    private void mostra(List<FilmIF> lista) {
        System.out.println("\n=================================== CATALOGO VIDEOTECA (VISTA TABELLARE) ===================================");
        
        if (lista.isEmpty()) {
            System.out.println("   Il catalogo è attualmente vuoto.");
        } else {
            String rigaFormato = "| %-5s | %-25.25s | %-20.20s | %-6s | %-12s | %-10s |%n";
            
            System.out.printf(rigaFormato, "ID", "TITOLO", "REGISTA", "ANNO", "STATO", "VOTO");
            System.out.println("------------------------------------------------------------------------------------------------------------");

            for (FilmIF f : lista) {
                String stringaVoto = (f.getStatoVisione() == StatoVisione.VISTO) 
                        ? f.getValutazione() + "/5 \u2605" 
                        : "-";
                
                System.out.printf("| %-5d | %-25.25s | %-20.20s | %-6d | %-12s | %-10s |%n", 
                    f.getId(), 
                    f.getTitolo(), 
                    f.getRegista(), 
                    f.getAnnoUscita(), 
                    f.getStatoVisione(), 
                    stringaVoto
                );
            }
        }
        System.out.println("============================================================================================================\n");
    }
}