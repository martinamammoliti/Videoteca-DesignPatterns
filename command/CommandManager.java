package command;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<Command> cronologia = new ArrayList<>();

    public void eseguiComando(Command comando){
        comando.execute();
        cronologia.add(comando);
    }
}
