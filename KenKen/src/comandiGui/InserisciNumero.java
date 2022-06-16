package comandiGui;

import comandi.Command;
import componenti.griglia.Griglia;

public class InserisciNumero implements Command {

        private final Griglia griglia;
        private final int riga;
        private final int colonna;
        private final int numero;

        public InserisciNumero(Griglia griglia, int riga, int colonna, int n) {
            this.griglia = griglia;
            this.riga = riga;
            this.colonna = colonna;
            this.numero = n;

        }

        @Override
        public boolean run() {
            griglia.inserisciNumero(numero, riga, colonna);
            return false;
        }

}
