import javax.swing.SwingUtilities;

import facade.VideotecaFacade;
import model.Videoteca;
import view.VideotecaGUI;

public class Main {
    public static void main(String[] args) {
        Videoteca modelloVideoteca = new Videoteca();
        VideotecaFacade facade = new VideotecaFacade(modelloVideoteca);

        SwingUtilities.invokeLater(() -> {
            VideotecaGUI gui = new VideotecaGUI(modelloVideoteca, facade);
            gui.setVisible(true);
        });
    }

}
