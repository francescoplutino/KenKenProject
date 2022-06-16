package componenti.griglia;

public class EventoGriglia {

    private final Griglia griglia;
    private final boolean nuovaGriglia ;
    private final boolean schemaAggiornato;
    private final boolean bloccoPulito;
    private final boolean integritaVerificata;
    private final boolean numeroInserito;
    private final boolean soluzioneRichiesta;

    public boolean isNuovaGriglia() { return nuovaGriglia; }
    public boolean isSchemaAggiornato() { return schemaAggiornato; }
    public boolean isBloccoPulito() { return bloccoPulito; }
    public boolean isIntegritaVerificata() { return integritaVerificata; }
    public boolean isNumeroInserito() { return numeroInserito; }
    public boolean isSoluzioneRichiesta() { return soluzioneRichiesta; }

    public Griglia getGriglia(){ return griglia; }

    public static class Builder {

        private final Griglia griglia;

        private boolean nuovaGriglia = false;
        private boolean schemaAggiornato  = false;
        private boolean bloccoPulito  = false;
        private boolean integritaVerificata = false;
        private boolean numeroInserito  = false;
        private boolean solutionRequested  = false;

        public Builder(Griglia griglia) { this.griglia = griglia;}

        public Builder nuovaGriglia(boolean b){
            nuovaGriglia = b;
            return this;
        }

        public Builder schemaAggiornato(boolean b){
            schemaAggiornato = b;
            return this;
        }

        public Builder bloccoPulito(boolean b){
            bloccoPulito = b;
            return this;
        }

        public Builder integritaVerificata(boolean b){
            integritaVerificata = b;
            return this;
        }

        public Builder numeroInserito(boolean b){
            numeroInserito = b;
            return this;
        }

        public Builder soluzioneRichiesta(boolean b){
            solutionRequested = b;
            return this;
        }

        public EventoGriglia build(){ return new EventoGriglia(this);}
    }

    private EventoGriglia(Builder builder){
        griglia = builder.griglia;
        nuovaGriglia = builder.nuovaGriglia;
        schemaAggiornato = builder.schemaAggiornato;
        bloccoPulito = builder.bloccoPulito;
        integritaVerificata = builder.integritaVerificata;
        soluzioneRichiesta = builder.solutionRequested;
        numeroInserito = builder.numeroInserito;
    }
}
