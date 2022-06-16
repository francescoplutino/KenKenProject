package comandiGui;


import comandi.Command;
import componenti.Casella;
import componenti.griglia.Griglia;
import componenti.griglia.MathOperation;

public class CreaBlocco implements Command {

    private final Griglia griglia;
    private MathOperation operation;
    private Casella[] caselleSelezionate;
    private final int risultato;

    public CreaBlocco(Griglia griglia, Casella[] caselleSelezionate, int risultato, MathOperation op) {
        this.griglia = griglia;
        this.risultato = risultato;
        this.caselleSelezionate = caselleSelezionate;
        this.operation = op;
    }

    public boolean run() {
        griglia.creaBlocco(caselleSelezionate,risultato,operation);
        return true;
    }


}
