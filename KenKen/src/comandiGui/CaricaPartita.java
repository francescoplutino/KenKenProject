package comandiGui;

import comandi.Command;
import componenti.griglia.Griglia;
import gui.PannelloDiControllo;
import gui.PannelloGriglia;

public class CaricaPartita implements Command {

    private Griglia griglia;
    private final PannelloGriglia pannelloGriglia;
    private final PannelloDiControllo pannelloDiControllo;

    public CaricaPartita(Griglia griglia, PannelloGriglia pannelloGriglia, PannelloDiControllo pannelloDiControllo) {
        this.griglia = griglia;
        this.pannelloGriglia = pannelloGriglia;
        this.pannelloDiControllo = pannelloDiControllo;
    }

    @Override
    public boolean run() {
        String filePath = pannelloGriglia.getFilePathInput(PannelloGriglia.OPERAZIONI_FILE.CARICA);
        if (filePath == null) {
            if (!griglia.carica(filePath))
                pannelloGriglia.showErrorDialog("Errore nel caricamento della partita", "Il file selezionato non contiene nessuna partita di Kenken!");
            else {
                pannelloDiControllo.setBottoneInizioPartita(true);
                pannelloDiControllo.setBottoneCreaBlocco(false);
                pannelloDiControllo.setBottoneVerificaIntegrità(false);
                pannelloDiControllo.setBottonePulisciGriglia(false);
                pannelloDiControllo.setBottoneMostraSoluzioni(false);
            }
        }
        return false;
    }
}
