import componenti.Blocco;
import componenti.Casella;
import componenti.griglia.Griglia;
import componenti.griglia.MathOperation;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class TestApp {

    private static final int TIMEOUT = 3000;
    private Griglia griglia = new Griglia();

    @Test(timeout = TIMEOUT)
    public void getCaselleDuplicateGrigliaVuota() {
        assertNull("Griglia vuota, numeri duplicati: ", griglia.getCaselleDuplicate());
    }

    @Test(timeout = TIMEOUT)
    public void getRisultatoNonValidoBloccoGrigliaVuota() {
        assertNull("L'integrità verificata su una griglia vuota deve ritornare null",
                griglia.getBloccoRisultatoNonValido());
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void setSizeScorretta() {
        int size = -1;
        griglia.setSize(size);
    }

    @Test(timeout = TIMEOUT)
    public void getSize() {
        int size = 6;
        griglia.setSize(size);
        assertEquals("Dimensioni griglia",size, griglia.getSize());
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void aggiungiNumeroErrato() {
        griglia.setSize(3);
        griglia.inserisciNumero(5,-1,0);
    }

    @Test(timeout = TIMEOUT)
    public void cancellaNumero() {
        griglia.setSize(9);
        griglia.inserisciNumero(2,0,0);
        griglia.cancellaNumero(0,0);
        assertEquals("I numeri stanno venendo eliminati",0, griglia.getGriglia()[0][0].getValore());
    }

    @Test(timeout = TIMEOUT)
    public void clear() {
        griglia.setSize(3);
        creaGriglia();
        griglia.clear();
        assertEquals("Griglia svuotata: ",0, griglia.getGriglia()[0][0].getValore());
    }

    @Test(timeout = TIMEOUT)
    public void verificaIntegrità() {
        griglia.setSize(3);
        // creo un blocco vi inserisco dei valori che non rispettano i vincoli del gioco
        creaGriglia();
        assertFalse("Griglia non valida", griglia.verificaIntegrita());

    }

    @Test(timeout = TIMEOUT)
    public void salvaGriglia() {
        griglia.setSize(3);
        Casella c1 = new  Casella(0,0);
        Casella c2 = new  Casella(0,1);
        Casella c3 = new  Casella(0,2);
        Casella[] caselle = {c1, c2, c3};
        griglia.creaBlocco(caselle,100, MathOperation.ADDIZIONE);
        griglia.inserisciNumero(1,0,0);
        griglia.inserisciNumero(1,0,1);
        griglia.inserisciNumero(1,0,2);
        griglia.salva("/risorse/testSalvataggio.kenken");
        griglia.clear();
    }

    @Test(timeout = TIMEOUT)
    public void caricaGriglia(){
        griglia.setSize(3);
        Casella c1 = new  Casella(0,0);
        Casella c2 = new  Casella(0,1);
        Casella c3 = new  Casella(0,2);
        Casella[] caselle = {c1, c2, c3};
        griglia.creaBlocco(caselle,100, MathOperation.ADDIZIONE);
        griglia.inserisciNumero(1,0,0);
        griglia.inserisciNumero(1,0,1);
        griglia.inserisciNumero(1,0,2);
        griglia.carica("/risorse/testSalvataggio.kenken");
        assertEquals("Grandezza blocchi caricati",caselle.length, griglia.getSchema().get(0).getDimensione());
    }

    @Test(timeout = TIMEOUT)
    public void salva() {
        assertFalse("Stai salvando un formato non valido",
                griglia.carica("/home/file.txt"));
    }

    @Test(timeout = TIMEOUT)
    public void carica() {
        assertFalse("Stai caricando un formato non valido",
                griglia.carica("/home/file.txt"));
    }

    @Test(timeout = TIMEOUT)
    public void creaBloccoCheckAliasing() {
        griglia.setSize(3);
        Casella c1 = new  Casella(0,0);
        Casella c2 = new  Casella(0,1);
        Casella c3 = new  Casella(0,2);
        Casella[] caselle = {c1, c2, c3};
        int result = 3;
        MathOperation operation = MathOperation.ADDIZIONE;
        griglia.creaBlocco(caselle,result,operation);
        Casella[][] currentGrid = griglia.getGriglia();
        for(Blocco b : griglia.getSchema()){
            for(Casella c : b.getCaselle()) {
                assertSame("Le caselle nel blocco referenziano le caselle nella griglia",
                        currentGrid[c.getRiga()][c.getColonna()], c);
            }
        }
    }

    private void creaGriglia() {
        Casella c1 = new  Casella(0,0);
        Casella c2 = new  Casella(0,1);
        Casella c3 = new  Casella(0,2);
        Casella[] caselle = {c1, c2, c3};;
        griglia.creaBlocco(caselle,100,MathOperation.ADDIZIONE);
        griglia.inserisciNumero(1,0,0);
        griglia.inserisciNumero(1,0,1);
        griglia.inserisciNumero(1,0,2);
    }


}
