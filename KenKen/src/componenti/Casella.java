package componenti;

import java.io.Serializable;
import java.util.Objects;

public class Casella {

    private int riga, colonna;
    private int valore;

    public Casella(int riga, int colonna, int valore){
        this.colonna = colonna;
        this.riga = riga;
        this.valore = valore;
    }

    public Casella(Casella casella){
        this.valore = casella.valore;
        this.colonna = casella.colonna;
        this.riga = casella.riga;
    }

    public Casella(int riga, int colonna) {
        this.riga = riga;
        this.colonna = colonna;
    }


    public int getValore() {
        return valore;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }

    public int getColonna() {
        return colonna;
    }

    public int getRiga() {
        return riga;
    }

    @Override
    public String toString() {
        return "Casella{" +
                "riga=" + riga +
                ", colonna=" + colonna +
                ", valore=" + valore +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Casella casella = (Casella) o;
        return riga == casella.riga && colonna == casella.colonna && valore == casella.valore;
    }

    @Override
    public int hashCode() {
        return Objects.hash(riga, colonna, valore);
    }
}
