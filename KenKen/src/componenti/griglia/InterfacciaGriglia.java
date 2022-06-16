package componenti.griglia;


import componenti.Blocco;
import componenti.Casella;

import java.io.File;
import java.io.IOException;
import java.util.*;

public interface InterfacciaGriglia {

    void addListener(Listener l);

    boolean creaBlocco(Casella[] caselle, int risultato, MathOperation op);

    void inserisciNumero(int numero, int riga, int colonna);

    void cancellaNumero(int riga, int colonna);

    void clear();

    int getSize();

    void setSize(int n);

    List<Casella> getCaselleDuplicate();

    List<Blocco> getBloccoRisultatoNonValido();

    boolean verificaIntegrita();

    void trovaSoluzioni(int maxSol) throws RuntimeException;

    void prossimaSoluzione();

    void soluzionePrecedente();

    boolean haSoluzioneSuccessiva();

    boolean haSoluzionePrecedente();

    boolean salva(String pathName);

    boolean carica(String pathName);

}
