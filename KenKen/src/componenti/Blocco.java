package componenti;

import componenti.griglia.MathOperation;

import java.io.Serializable;
import java.util.*;

public class Blocco {

    private Casella[] caselle;
    private int risultato;
    public MathOperation operazione;
    public LinkedList<Casella> casellaSalvataggio = new LinkedList<>();


    public Blocco(Casella[] caselle, int risultato, MathOperation op) {
        this.caselle = caselle;
        this.risultato = risultato;
        this.operazione = op;
    }

    public Blocco() {}


    public int getRisultato() {
        return risultato;
    }

    public void setRisultato(int risultato) { this.risultato = risultato; }

    public Casella[] getCaselle() {
        return caselle;
    }

    public MathOperation getOperazione() {
        return operazione;
    }

    public int getDimensione(){
       return caselle.length;
    }

    public void setOperazione(MathOperation op){ this.operazione = op;}

    public boolean verificaRisultato(){
        for(Casella c : caselle){
            if(c.getValore() == 0) //casella vuota --> Blocco non pieno
                return true; //Non è ancora possibile calcolare il risultato del blocco
        }
        return verificaRisultato(0);
    }


    private boolean verificaRisultato(int x){
        boolean verificato = false;
        if(x == caselle.length){
            Casella prima = caselle[0];
            int ret = prima.getValore();
            for(int i = 1; i < caselle.length; i++){
                Casella casella = caselle[i];
                switch (operazione){
                    case ADDIZIONE -> {
                        ret = ret + casella.getValore();
                        break;
                    }
                    case SOTTRAZIONE -> {
                        ret = ret - casella.getValore();
                        break;
                    }
                    case MOLTIPLICAZIONE -> {
                        ret = ret * casella.getValore();
                        break;
                    }
                    case DIVISIONE -> {
                        ret = ret / casella.getValore();
                        break;
                    }
                }
            }
            return ret == risultato;
        }else {
            for(int j = 0; j < caselle.length && !verificato; j++){
                inverti(caselle,x,j);
                verificato = verificaRisultato(x+1);
                inverti(caselle,x,j);
            }
        }
        return verificato;
    }


    private void inverti(Casella[] caselle, int i, int j){ //Questo metodo serve ad invertire le caselle per verificare
        Casella tmp = caselle[i];                          //se il risultato ne rispetta il vincolo
        caselle[i] = caselle[j];
        caselle[j] = tmp;
    }

    @Override
    public String toString() {
        return "Blocco{" +
                "caselle=" + Arrays.toString(caselle) +
                ", risultato=" + risultato +
                ", operazione=" + operazione +
                '}';
    }

    public static boolean casellaContigua(boolean[][] caselle){
        boolean[][] section = new boolean[caselle.length][caselle.length];
        int colonna = 0;
        int riga = 0;
        int sezione = 0;
        for(int i = 0; i < caselle.length; i++){
            for(int j = 0; j < caselle.length; j++)
                if(caselle[i][j]){
                    sezione++;
                    section[i][j] = true;
                    colonna = j;
                    riga = i;
            }
        }
        return casellaContigua(riga, colonna, section, sezione-1) == 0;
    }

    private static int casellaContigua(int i, int j, boolean[][] caselle, int rimasti){
        if(i > 0 && caselle[i-1][j]){
            caselle[i][j] = false;
            rimasti = casellaContigua(i-1, j, caselle, rimasti-1);
        }
        if(i < caselle.length-1 && caselle[i+1][j]){
            caselle[i][j] = false;
            rimasti = casellaContigua(i+1, j, caselle, rimasti-1);
        }
        if(j > 0 && caselle[i][j-1]){
            caselle[i][j] = false;
            rimasti = casellaContigua(i, j-1, caselle, rimasti-1);
        }
        if(j < caselle.length-1 && caselle[i][j+1]){
            caselle[i][j] = false;
            rimasti = casellaContigua(i, j+1, caselle, rimasti-1);
        }
        caselle[i][j] = false;
        return rimasti;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blocco blocco = (Blocco) o;
        return risultato == blocco.risultato && Arrays.equals(caselle, blocco.caselle) && operazione == blocco.operazione;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(risultato, operazione);
        result = 31 * result + Arrays.hashCode(caselle);
        return result;
    }
}
