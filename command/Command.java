package command;
public interface Command {
    boolean doIt();
    void undoIt();
}
