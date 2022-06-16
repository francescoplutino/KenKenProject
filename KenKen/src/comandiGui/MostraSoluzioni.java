package comandiGui;

import comandi.Command;
import componenti.griglia.Griglia;
import gui.PannelloGriglia;

public class MostraSoluzioni implements Command {

    private  Griglia griglia;
    private PannelloGriglia pannelloGriglia;

    public MostraSoluzioni(Griglia griglia, PannelloGriglia pannelloGriglia) {
        this.griglia = griglia;
        this.pannelloGriglia = pannelloGriglia;
    }

    @Override
    public boolean run(){
        int maxSol = pannelloGriglia.getInputMaxSoluzioni();
        if(maxSol == -1) return true;
        try {
            griglia.trovaSoluzioni(maxSol);
        }catch (RuntimeException e){
            pannelloGriglia.showErrorDialog("Irrisolvibile", "Non ci sono soluzioni per questa griglia!");
        }
        return true;
    }
}
