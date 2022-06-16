package gui;

import comandi.HistoryCommandHandler;
import comandiGui.*;
import componenti.Blocco;
import componenti.Casella;
import componenti.griglia.EventoGriglia;
import componenti.griglia.Griglia;
import componenti.griglia.Listener;
import componenti.griglia.MathOperation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class PannelloDiControllo extends JPanel implements Listener {

    private final PannelloGriglia pannelloGriglia;

    private final JButton bottoneCreaBlocco;
    private final JButton bottoneIniziaPartita;
    private final JButton bottoneVerificaIntegrita;
    private final JButton bottonePulisciGriglia;
    private final JButton bottoneMostraSoluzioni;
    private final JButton bottoneSoluzionePrecedente;
    private final JButton bottoneSoluzioneSuccessiva;
    private final JButton bottoneSalvaPartita;

    private boolean giocoIniziato = false;


    public PannelloDiControllo(PannelloGriglia pannelloGriglia, HistoryCommandHandler commandHandler, Griglia griglia) {

        this.pannelloGriglia = pannelloGriglia;
        pannelloGriglia.setBackground(Color.black);

        griglia.addListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JPanel comandiDiControllo = new JPanel(new GridLayout(2, 2, 5, 15));
        comandiDiControllo.setBackground(Color.black);

        JPanel creaBlocco = new JPanel();
        creaBlocco.setBackground(Color.black);

        bottoneCreaBlocco = new JButton("Aggiungi Blocco");
        bottoneCreaBlocco.setBackground(Color.black);
        bottoneCreaBlocco.setForeground(Color.white);
        bottoneCreaBlocco.addActionListener(evt -> {
            boolean[][] caselleSelezionate = pannelloGriglia.getCaselleSelezionate();
            boolean bottoneSelezionato = false;
            for(int i = 0; i < caselleSelezionate.length & !bottoneSelezionato; i++)
                for (int j = 0; j < caselleSelezionate.length && !bottoneSelezionato; j++)
                    if(caselleSelezionate[i][j]) bottoneSelezionato = true;

            if(!bottoneSelezionato){
                pannelloGriglia.showErrorDialog("Errore di selezione" , "Deve essere selezionata almeno una casella");
                return;
            }
            if(!Blocco.casellaContigua(caselleSelezionate)){
                pannelloGriglia.showErrorDialog("Errore di selezione", "Le caselle selezionate devono essere adiacenti!");
                return;
            }

            int result = pannelloGriglia.getInputRisultatoObbiettivo();
            if(result == -1) return;

            MathOperation operation = pannelloGriglia.getInputOperazione();
            if(operation == null) return;

            List<Casella> caselle = new LinkedList<>();
            for (int i = 0; i < caselleSelezionate.length; i++) {
                for (int j = 0; j < caselleSelezionate.length; j++) {
                    if(caselleSelezionate[i][j])
                        caselle.add(new Casella(i,j));
                }
            }
            commandHandler.handle(new CreaBlocco(griglia, caselle.toArray(new Casella[0]), result, operation));
        });

        creaBlocco.setEnabled(false);
        creaBlocco.add(bottoneCreaBlocco);

        JPanel iniziaPartita = new JPanel();
        //iniziaPartita.setBackground(Color.black);

        bottoneIniziaPartita = new JButton("Gioca");
        bottoneIniziaPartita.setBackground(Color.black);
        bottoneIniziaPartita.setForeground(Color.white);
        bottoneIniziaPartita.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pannelloGriglia.startGameView();
                giocoIniziato = true;
                bottoneIniziaPartita.setEnabled(false);
                bottoneVerificaIntegrita.setEnabled(true);
                bottonePulisciGriglia.setEnabled(true);
                bottoneMostraSoluzioni.setEnabled(true);
                bottoneSalvaPartita.setEnabled(true);
            }
        });
        bottoneIniziaPartita.setEnabled(false);
        iniziaPartita.add(bottoneIniziaPartita);

        bottoneSalvaPartita = new JButton("Salva Partita");
        bottoneSalvaPartita.setEnabled(false);
        bottoneSalvaPartita.setBackground(Color.black);
        bottoneSalvaPartita.setForeground(Color.white);
        bottoneSalvaPartita.addActionListener(evt -> commandHandler.handle( new SalvaPartita(griglia, pannelloGriglia)));

        iniziaPartita.add(bottoneSalvaPartita);
        iniziaPartita.setBackground(Color.black);

        comandiDiControllo.add(iniziaPartita);
        comandiDiControllo.add(creaBlocco);
        comandiDiControllo.setBackground(Color.black);


        JPanel comandiGioco = new JPanel(new GridLayout(4,1,0,15));
        comandiGioco.setBackground(Color.black);

        bottoneVerificaIntegrita = new JButton("Verifica integrità");
        bottoneVerificaIntegrita.addActionListener(evt -> commandHandler.handle(new VerificaIntegrita(griglia)));
        bottoneVerificaIntegrita.setEnabled(false);
        bottoneVerificaIntegrita.setBackground(Color.black);
        bottoneVerificaIntegrita.setForeground(Color.white);
        comandiGioco.add(bottoneVerificaIntegrita);

        bottonePulisciGriglia = new JButton("Pulisci Griglia");
        bottonePulisciGriglia.addActionListener(evt-> commandHandler.handle(new PulisciGriglia(griglia)));
        bottonePulisciGriglia.setEnabled(false);
        bottonePulisciGriglia.setBackground(Color.black);
        bottonePulisciGriglia.setForeground(Color.white);
        comandiGioco.add(bottonePulisciGriglia);

        bottoneMostraSoluzioni = new JButton("Soluzioni");
        bottoneMostraSoluzioni.addActionListener(evt -> commandHandler.handle(new MostraSoluzioni(griglia, pannelloGriglia)));
        bottoneMostraSoluzioni.setEnabled(false);
        bottoneMostraSoluzioni.setBackground(Color.black);
        bottoneMostraSoluzioni.setForeground(Color.white);
        comandiGioco.add( bottoneMostraSoluzioni);



        JPanel solutionNavigationCommands = new JPanel(new GridLayout(1, 2, 0, 10));
        solutionNavigationCommands.setBackground(Color.black);

        bottoneSoluzionePrecedente = new JButton("< Precedente");
        bottoneSoluzionePrecedente.setBackground(Color.black);
        bottoneSoluzionePrecedente.setForeground(Color.white);
        bottoneSoluzionePrecedente.addActionListener(evt -> commandHandler.handle(new NavigaSoluzioni(griglia, -1)));
        bottoneSoluzionePrecedente.setEnabled(false);
        solutionNavigationCommands.add(bottoneSoluzionePrecedente);

        bottoneSoluzioneSuccessiva = new JButton("Successiva >");
        bottoneSoluzioneSuccessiva.setForeground(Color.white);
        bottoneSoluzioneSuccessiva.setBackground(Color.black);
        bottoneSoluzioneSuccessiva.addActionListener(evt -> commandHandler.handle(new NavigaSoluzioni(griglia, 1)));
        bottoneSoluzioneSuccessiva.setEnabled(false);
        solutionNavigationCommands.add(bottoneSoluzioneSuccessiva);
        comandiGioco.add(solutionNavigationCommands);

        add(Box.createRigidArea(new Dimension(0,20)));
        add(comandiDiControllo);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(comandiGioco);
        add(Box.createRigidArea(new Dimension(0,20)));

    }

    public void setBottoneInizioPartita(boolean b) {
        bottoneIniziaPartita.setEnabled(b);
    }

    public void setBottoneCreaBlocco(boolean b) {
        bottoneCreaBlocco.setEnabled(b);
    }

    public void setBottoneVerificaIntegrità(boolean b) {
        bottoneVerificaIntegrita.setEnabled(b);
    }

    public void setBottonePulisciGriglia(boolean b) {
        bottonePulisciGriglia.setEnabled(b);
    }

    public void setBottoneMostraSoluzioni(boolean b) {
        bottoneMostraSoluzioni.setEnabled(b);
    }

    @Override
    public void grigliaMod(EventoGriglia e) {
        if(e.isNuovaGriglia()){
            giocoIniziato = false;
            bottoneIniziaPartita.setEnabled(false);
            bottoneSoluzionePrecedente.setEnabled(false);
            bottoneSoluzioneSuccessiva.setEnabled(false);
        }
        if(e.isSchemaAggiornato()){
            int n = e.getGriglia().getSize();
            int caselleBloccate = pannelloGriglia.getCaselleBloccate();
            if(caselleBloccate == n*n && !giocoIniziato){
                bottoneIniziaPartita.setEnabled(true);
                bottoneCreaBlocco.setEnabled(false);
            }
            else {
                bottoneIniziaPartita.setEnabled(false);
                bottoneCreaBlocco.setEnabled(true);
            }
        }
        if(e.isSoluzioneRichiesta()){
            bottoneSoluzionePrecedente.setEnabled((e.getGriglia().haSoluzionePrecedente()));
            bottoneSoluzioneSuccessiva.setEnabled(e.getGriglia().haSoluzioneSuccessiva());
        }
        repaint();
        revalidate();
    }
}
