package comandiGui;

import comandi.Command;
import componenti.griglia.Griglia;

public class NavigaSoluzioni implements Command {

    public Griglia griglia;
    private final int i;


    public NavigaSoluzioni(Griglia griglia, int i) {
        this.i = i;
        this.griglia = griglia;
    }

    @Override
    public boolean run(){
        if(i>0) griglia.prossimaSoluzione();
        else if (i<0) griglia.soluzionePrecedente();
        return false;
    }
}
