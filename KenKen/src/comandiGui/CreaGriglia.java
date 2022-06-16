package comandiGui;

import comandi.Command;
import componenti.griglia.Griglia;
import gui.PannelloDiControllo;
import gui.PannelloGriglia;

public class CreaGriglia implements Command {

    private final Griglia griglia;
    private final PannelloGriglia pannelloGriglia;
    private final PannelloDiControllo pannelloDiControllo;
    private final int n;

    public CreaGriglia(Griglia griglia, PannelloGriglia pannelloGriglia, PannelloDiControllo pannelloDiControllo, int n){
        this.pannelloGriglia = pannelloGriglia;
        this.griglia = griglia;
        this.pannelloDiControllo = pannelloDiControllo;
        this.n = n;
    }

    public boolean run(){
        if(griglia.getSize() != 0 && !pannelloGriglia.showNewSizeDialog()) return false;
        griglia.setSize(n);
        pannelloDiControllo.setBottoneCreaBlocco(true);
        pannelloDiControllo.setBottonePulisciGriglia(false);
        pannelloDiControllo.setBottoneVerificaIntegrità(false);
        pannelloDiControllo.setBottoneMostraSoluzioni(false);
        return false;
    }
}
