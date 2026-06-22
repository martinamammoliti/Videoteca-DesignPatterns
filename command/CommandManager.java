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
        if (comando.doIt()) {
            addToHistory(comando);
        } else {
            history.clear(); 
        }
        
        if (redoList.size() > 0) {
            redoList.clear();
        }
    }

    public void undo() {
        if (history.size() > 0) {
            Command undoCmd = history.removeFirst();
            undoCmd.undoIt();
            redoList.addFirst(undoCmd); 
        }
    }

    public void redo() {
        if (redoList.size() > 0) {
            Command redoCmd = redoList.removeFirst();
            redoCmd.doIt();
            history.addFirst(redoCmd); 
        }
    }

    private void addToHistory(Command cmd) {
        history.addFirst(cmd);
        if (history.size() > maxHistoryLength) {
            history.removeLast(); 
        }
    }
}