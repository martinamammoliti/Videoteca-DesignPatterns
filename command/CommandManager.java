package command;

import java.util.LinkedList;

public class CommandManager {
    private int maxHistoryLength = 100;
    private final LinkedList<Command> history = new LinkedList<>();
    private final LinkedList<Command> redoList = new LinkedList<>();

    public CommandManager() {
        this(100);
    }

    public CommandManager(int maxHistoryLength) {
        if (maxHistoryLength < 0)
            throw new IllegalArgumentException();
        this.maxHistoryLength = maxHistoryLength;
    }

    public void eseguiComando(Command comando) {
        redoList.clear();

        // 1. Inseriamo provvisoriamente il comando nella cronologia PRIMA dell'esecuzione.
        // Così l'Observer vedrà canUndo() == true non appena doIt() notificherà il cambiamento.
        addToHistory(comando);

        boolean successo = false;
        try {
            successo = comando.doIt();
        } catch (Exception e) {
            // Se si verifica un'eccezione imprevista nel comando, facciamo il rollback
            history.removeFirst();
            throw e;
        }

        // 2. Se il comando restituisce false (operazione fallita o annullata dal comando stesso)
        if (!successo) {
            history.removeFirst(); 
        }
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command undoCmd = history.removeFirst();
            
            redoList.addFirst(undoCmd); 

            try {
                undoCmd.undoIt();
            } catch (Exception e) {
                redoList.removeFirst();
                history.addFirst(undoCmd);
                throw e;
            }
        }
    }

    public void redo() {
        if (!redoList.isEmpty()) {
            Command redoCmd = redoList.removeFirst();
            
            history.addFirst(redoCmd); 

            boolean successo = false;
            try {
                successo = redoCmd.doIt();
            } catch (Exception e) {
                history.removeFirst();
                redoList.addFirst(redoCmd);
                throw e;
            }

            if (!successo) {
                history.removeFirst();
                redoList.addFirst(redoCmd);
            }
        }
    }

    private void addToHistory(Command cmd) {
        history.addFirst(cmd);
        if (history.size() > maxHistoryLength) {
            history.removeLast(); 
        }
    }

    public boolean canUndo() {
        return !history.isEmpty();
    }

    public boolean canRedo() {
        return !redoList.isEmpty();
    }

    public void svuotaCronologia() {
        this.history.clear();
        this.redoList.clear();
    }
}