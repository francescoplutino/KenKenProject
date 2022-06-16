package comandiGui;

import comandi.Command;
import componenti.griglia.Griglia;
import gui.PannelloGriglia;

public class SalvaPartita implements Command {

    private Griglia griglia;
    private PannelloGriglia pannelloGriglia;

    public SalvaPartita(Griglia griglia, PannelloGriglia pannelloGriglia){
        this.griglia = griglia;
        this.pannelloGriglia = pannelloGriglia;
    }

    @Override
    public boolean run(){
        String filePath = pannelloGriglia.getFilePathInput(PannelloGriglia.OPERAZIONI_FILE.SALVA);
        if(filePath != null){
            if(!griglia.salva(filePath))
                pannelloGriglia.showErrorDialog("Errore nel salvataggio della partita","Impossibile salvare una partita vuota");
            return true;
        }
        return false;
    }

}
