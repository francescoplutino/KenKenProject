package comandiGui;

import comandi.Command;
import componenti.griglia.Griglia;


public class VerificaIntegrita implements Command {

    private final Griglia griglia;

    public VerificaIntegrita(Griglia griglia) {
        this.griglia = griglia;
    }

    @Override
    public boolean run(){
        griglia.verificaIntegrita();
        return false;
    }

}
