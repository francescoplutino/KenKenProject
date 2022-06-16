package comandiGui;


import comandi.Command;
import componenti.griglia.Griglia;

public class PulisciGriglia implements Command {

    private Griglia griglia;

    public PulisciGriglia(Griglia griglia) {
        this.griglia = griglia;
    }

    @Override
    public boolean run(){
        griglia.clear();
        return true;
    }

}
