package view;

import model.*;
import observer.Observer;
import facade.*;
import java.util.List;

public class VistaTabellare implements Observer {
    private final VideotecaFacade videotecaFacade;

    public VistaTabellare(Videoteca videoteca, VideotecaFacade videotecaFacade){
        this.videotecaFacade = videotecaFacade;
        
        videoteca.attach(this);
    }

    @Override
    public void update(){
        System.out.println("\n[UI Testuale] Ricevuto segnale di update. Richiedo i dati...");
        
        List<FilmIF> datiAggiornati = videotecaFacade.ottieniCatalogoFiltratoEOrdinato("", "", "", "Tutti", "Nessuno");
        mostra(datiAggiornati);
    }

    private void mostra(List<FilmIF> lista){
        System.out.println("====== CATALOGO VIDEOTECA AGGIORNATO (VISTA TESTUALE) =======");
        if(lista.isEmpty()) {
            System.out.println("Il catalogo è vuoto.");
        } else {
            for(FilmIF f : lista){
                
                System.out.println(String.format("[%d] %s - Regia: %s (%d) | [%s] Voto: %d/5 ★", 
                    f.getId(), f.getTitolo(), f.getRegista(), f.getAnnoUscita(), f.getStatoVisione(), f.getValutazione()
                ));
            }
        }
        System.out.println("====================================================\n");
    }
}