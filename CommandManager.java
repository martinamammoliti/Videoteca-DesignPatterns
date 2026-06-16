public class CommandManager {
    public void eseguiComando(Command comando){
        if(comando!=null){
            comando.execute();
        }
    }
}
