package componenti.griglia;

import backTrackingSolution.BackTrackingSolution;
import componenti.Blocco;
import componenti.Casella;

import java.io.*;
import java.util.*;

public class Griglia extends GrigliaAstratta {

    private int n;
    private Casella[][] caselle;
    private Casella[] caselleSalvate;
    private final ArrayList<int[][]> soluzioni = new ArrayList<>(); //In questo Array abbiamo memorizzato le soluzioni
    private int soluzioneCorrente = 0;

    private LinkedList<Blocco> schemaBlocchi = new LinkedList<>();// LinkedList dove sono memorizzati i blocchi dello schema

    private List<Casella> caselleDuplicate; //Le caselle i cui blocchi sono duplicati nelle righe o nelle colonne
    private List<Blocco> bloccoRisultatoNonValido; //Blocchi che non rispettano il vincolo aritmetico

    public Griglia(){}

    public int getSize(){ return n;}

    public Casella[][] getGriglia(){ return caselle;}

    public List<Blocco> getSchema(){ return Collections.unmodifiableList(schemaBlocchi); }

    public int[][] getSoluzioneCorrente() throws RuntimeException {
        if(soluzioni.size() > 0)
            return soluzioni.get(soluzioneCorrente);
        throw new RuntimeException("Non ci sono soluzioni!");
    }

    public List<Blocco> getBloccoRisultatoNonValido(){ return bloccoRisultatoNonValido;}

    public List<Casella> getCaselleDuplicate(){ return caselleDuplicate;}

    public void setSize(int n){ //Quando cambio la dimensione, creo una nuova griglia
        if(n < 3) throw new IllegalArgumentException();
        this.n = n;
        this.caselle = new Casella[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                caselle[i][j] = new Casella(i,j,0);
            }
        }
        schemaBlocchi.clear();
        soluzioni.clear();
        notifyListeners(new EventoGriglia.Builder(this).nuovaGriglia(true).build());
    }

    public void inserisciNumero(int numero, int riga, int colonna){
        if( numero < 0 || numero > n ) throw new IllegalArgumentException("Numero "+ numero+ " non valido!");
        if( colonna < 0 || colonna > n-1 ) throw new IllegalArgumentException("Colonna "+ colonna+ " non valida!");
        if( riga < 0 || riga > n-1 ) throw new IllegalArgumentException("Riga "+ riga+ " non valida!");
        caselle[riga][colonna].setValore(n);
        notifyListeners(new EventoGriglia.Builder(this).numeroInserito(true).build());

    }
    public void cancellaNumero(int riga, int colonna){
        if( colonna < 0 || colonna > n-1 ) throw new IllegalArgumentException("Colonna non valida!");
        if( riga < 0 || riga > n-1 ) throw new IllegalArgumentException("Riga non valida!");
        caselle[riga][colonna].setValore(0);
        notifyListeners(new EventoGriglia.Builder(this).numeroInserito(true).build());
    }
    private final List<Listener> listeners = new LinkedList<>();

    public void addListener(Listener l){
        if(listeners.contains(l)) return;;
        listeners.add(l);
    }

    protected void notifyListeners(EventoGriglia e) {
        for (Listener l : listeners)
            l.grigliaMod(e);
    }

    public void clear() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                caselle[i][j].setValore(0);
            }
        }
        notifyListeners(new EventoGriglia.Builder(this).bloccoPulito(true).build());
    }

    public boolean creaBlocco(Casella[] caselle, int result, MathOperation op) {
        if(caselle.length == 0) throw new IllegalArgumentException();

        boolean[][] caselleSelezionate = new boolean[n][n];
        for(Casella c : caselle) caselleSelezionate[c.getRiga()][c.getColonna()] = true;
        if(!Blocco.casellaContigua(caselleSelezionate)) throw new IllegalArgumentException("Selezione di caselle non valida");

        Casella[] localSquares = new Casella[caselle.length];
        for (int i = 0; i < caselle.length; i++) {
            Casella c = caselle[i];
            localSquares[i] = this.caselle[c.getRiga()][c.getColonna()];
        }
        Blocco blocco = new Blocco(localSquares, result, op);
        schemaBlocchi.add(blocco);
        notifyListeners(new EventoGriglia.Builder(this).schemaAggiornato(true).build());
        return true;
    }

    private List<Casella> trovaDuplicati() {
        Set<Casella> caselleDuplicate = new HashSet<>();
        for (int riga = 0; riga < n; riga++) {
            for (int colonna = 0; colonna < n; colonna++) {
                int val = caselle[riga][colonna].getValore();
                // verifico i duplicati soltanto per i numeri validi (che sono diversi da 0)
                for(int k = 0; k < n && val != 0; k++){
                    if(caselle[riga][k].getValore() == val && k != colonna){
                        caselleDuplicate.add(new Casella(riga, colonna));
                        caselleDuplicate.add(new Casella(riga, k));
                    }
                    else if (caselle[k][colonna].getValore() == val && k != riga){
                        caselleDuplicate.add(new Casella(riga, colonna));
                        caselleDuplicate.add(new Casella(k, colonna));
                    }
                }
            }
        }
        return new LinkedList<>(caselleDuplicate);
    }

    private List<Blocco> trovaBlocchiScorretti() {
        List<Blocco> blocchiIncorretti = new LinkedList<>();
        for(Blocco b : schemaBlocchi) {
            if(!b.verificaRisultato())
                blocchiIncorretti.add(b);
        }
        return blocchiIncorretti;
    }

    public boolean verificaIntegrita() {
        List<Casella> caselleDuplicate = trovaDuplicati();
        List<Blocco> invalidTargetResultCages = trovaBlocchiScorretti();
        if(caselleDuplicate.isEmpty() && invalidTargetResultCages.isEmpty())
            return true;
        this.caselleDuplicate = caselleDuplicate;
        this.bloccoRisultatoNonValido = invalidTargetResultCages;
        notifyListeners(new EventoGriglia.Builder(this).integritaVerificata(true).build());
        return false;
    }

    public void trovaSoluzioni(int maxSolutions) throws RuntimeException {
        // effettuo il calcolo delle soluzioni solo se non ho già a disposizione il numero di soluzioni richiesto
        if(soluzioni.size() != maxSolutions) {
            clear();
            soluzioni.clear();
            new SoluzioniKenken().risolvi(maxSolutions);
        }
        if(soluzioni.isEmpty()) throw new RuntimeException();
        soluzioneCorrente = 0;
        notifyListeners(new EventoGriglia.Builder(this).soluzioneRichiesta(true).build());
    }

    public int getTotaleSoluzioni() {
        return soluzioni.size();
    }

    public void prossimaSoluzione() {
        if(haSoluzioneSuccessiva()) {
            soluzioneCorrente++;
            notifyListeners(new EventoGriglia.Builder(this).soluzioneRichiesta(true).build());
        }
    }

    public boolean haSoluzioneSuccessiva() {
        return soluzioneCorrente + 1 <= soluzioni.size()-1;
    }

    public void soluzionePrecedente() {
        if(haSoluzionePrecedente()) {
            soluzioneCorrente--;
            notifyListeners(new EventoGriglia.Builder(this).soluzioneRichiesta(true).build());
        }
    }

    public boolean haSoluzionePrecedente() {
        return soluzioneCorrente - 1 >= 0;
    }
    
    @Override
    public boolean salva(String pathName) {
        if(!pathName.contains(".ken")) return false;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(pathName));
            String dimesnioneGriglia = "";
            bw.write("ORDINE\n");
            dimesnioneGriglia = n + "\n";
            bw.write(dimesnioneGriglia);
            bw.write("FINE-ORDINE\n");
            bw.write("MATRICE\n");

            for (int i = 0; i < caselle.length; i++)
                for (int j = 0; j < caselle.length; j++)
                    bw.write(caselle[i][j] + "\n");

            bw.write("FINE-MATRICE\n");

            for(Blocco b : schemaBlocchi){
                bw.write("BLOCCO\n");
                bw.write(b.getOperazione()+ ", "+ b.getRisultato()+"\n");
                for(Casella c : b.getCaselle())
                    bw.write(c.getValore()+"\n");
                bw.write("FINE-BLOCCO\n");
            }
            bw.write("FINE-BLOCCHI");
            bw.close();
        }catch(IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
   }

    @Override
    public boolean carica(String pathName) {
        if(!pathName.contains(".ken")) return false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathName));
            String linea = "";
            linea = br.readLine();
            StringTokenizer st;
            if(linea == null)
                return false;
            if(linea.equals("DIMENSIONE")){
                linea = br.readLine();
                while (!(linea.equals("FINE-DIMENSIONE"))){
                    n = Integer.parseInt(linea);
                    linea = br.readLine();
                }
            }

            linea = br.readLine();
            caselle = new Casella[n][n];
            if(linea.equals("MATRICE")){
                linea = br.readLine();
                for(int i = 0; i <= n && !linea.equals("FINE-MATRICE"); i++) {
                    for(int j = 0; j <= n; j++){
                        inserisciNumero(Integer.parseInt(linea),i,j);
                        linea = br.readLine();
                    }
                }
            }
            linea = br.readLine();
            schemaBlocchi = new LinkedList<>();

            while (!linea.equals("FINE-BLOCCHI")){
                if(linea.equals("BLOCCO")){
                    Blocco b = new Blocco();
                    linea = br.readLine();
                    st = new StringTokenizer(linea,",");
                    switch(st.nextToken()){
                        case "ADDIZIONE" :
                            b.setOperazione(MathOperation.ADDIZIONE);
                            break;
                        case "SOTTRAZIONE" :
                            b.setOperazione(MathOperation.SOTTRAZIONE);
                            break;
                        case "MOLTIPLICAZIONE" :
                            b.setOperazione(MathOperation.MOLTIPLICAZIONE);
                            break;
                        case "DIVISIONE" :
                            b.setOperazione(MathOperation.DIVISIONE);
                            break;
                    }
                    b.setRisultato(Integer.parseInt(st.nextToken()));

                    linea = br.readLine();
                    int i = 0;
                    while (!linea.equals("FINE-BLOCCO")){
                        caselleSalvate = b.getCaselle();
                        caselleSalvate[i].setValore(Integer.parseInt(linea));
                        linea = br.readLine();
                    }
                    schemaBlocchi.add(b);
                    linea = br.readLine();
                }
            }
            notifyListeners(new EventoGriglia.Builder(this).nuovaGriglia(true).schemaAggiornato(true).build());
        }catch( IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private class SoluzioniKenken extends BackTrackingSolution<Casella, Integer> {

        @Override
        public Casella primoPuntoDecisione() {
            return caselle[0][0];
        }

        @Override
        public Casella prossimoPuntoDecisione(Casella punto) {
            int riga = punto.getRiga();
            int colonna = punto.getColonna();
            if(colonna < n-1) colonna++;
            else if(colonna == n-1){
                riga++;
                colonna = 0;
            }
            return caselle[riga][colonna];
        }

        @Override
        public Casella ultimoPuntoDecisione() {
            return caselle[n-1][n-1];
        }

        @Override
        public Integer primaScelta(Casella point) {
            return 1;
        }

        @Override
        public Integer prossimaScelta(Integer choice) {
            return choice +1;
        }

        @Override
        public Integer ultimaScelta(Casella point) {
            return n;
        }

        @Override
        public boolean assegnabile(Integer choice, Casella point) {
            int tmp = point.getValore();
            point.setValore(choice);
            List<Casella> duplicateSquares = trovaDuplicati();
            if(duplicateSquares.isEmpty()) {
                for(Blocco b : schemaBlocchi) {
                    for(Casella c : b.getCaselle()) {
                        if(c.equals(point)) {
                            if(!b.verificaRisultato()) {
                                point.setValore(tmp);
                                return false;
                            }
                            return true;
                        }
                    }
                }
            }
            point.setValore(tmp);
            return false;
        }

        @Override
        public void assegna(Integer choice, Casella point) { point.setValore(choice); }

        @Override
        public void deassegna(Integer choice, Casella point) { point.setValore(0); }

        @Override
        public Casella precedentePuntoDecisione(Casella c) {
            int riga = c.getRiga();
            int colonna = c.getColonna();
            if(colonna > 0) colonna--;
            else if(colonna == 0) {
                riga--;
                colonna = n-1;
            }
            return caselle[riga][colonna];
        }


        public Integer ultimaSceltaAssegnata(Casella c) {
            return c.getValore();
        }

        @Override
        public void scriviSoluzione(int nSol) {
            int[][] copiaGriglia = new int[n][n];
            for(int i = 0; i < n; i++){
                for (int j = 0; j < n; j++){
                    copiaGriglia[i][j] = caselle[i][j].getValore();
                }
            }
            soluzioni.add(copiaGriglia);
        }
    }

    @Override
    public String toString() {
        int[][] ret = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ret[i][j] = caselle[i][j].getValore();
            }
        }
        return Arrays.deepToString(ret);
    }

}

