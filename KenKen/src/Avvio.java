
import comandi.HistoryCommandHandler;
import comandiGui.CaricaPartita;
import comandiGui.CreaGriglia;

import componenti.griglia.Griglia;
import gui.PannelloDiControllo;
import gui.PannelloGriglia;

import java.awt.Dimension;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Avvio {

    public static void main(String[] args) {

        JPanel pannelloMenu = new JPanel();
        JFrame menu = new JFrame("KenKen Menu");
        JFrame frame = new JFrame("Kenken");
        JPanel contentPane = new JPanel();
        JLabel titolo = new JLabel("KENKEN GAME");
        Font fontTitolo = new Font("Times New Roman", Font.PLAIN, 40);

        final Griglia griglia = new Griglia();
        final HistoryCommandHandler commandHandler = new HistoryCommandHandler();
        final PannelloGriglia pannelloGriglia = new PannelloGriglia(griglia, commandHandler);
        pannelloGriglia.setPreferredSize(new Dimension(400, 400));
        final PannelloDiControllo pannelloDiControllo = new PannelloDiControllo(pannelloGriglia,commandHandler, griglia);

        pannelloMenu.setBounds(100, 50,400,100);
        pannelloMenu.setBackground(Color.black);
        titolo.setForeground(Color.white);
        titolo.setFont(fontTitolo);
        pannelloMenu.add(titolo);

        menu.add(pannelloMenu);
        menu.setSize(400,400);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.getContentPane().setBackground(Color.black);
        menu.setLayout(new GridLayout(2,1));
        menu.setVisible(true);

        frame.getContentPane().add(contentPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(false);

        Toolkit it = Toolkit.getDefaultToolkit();
        Dimension d = it.getScreenSize();
        int w = menu.getWidth(), h = menu.getHeight();
        menu.setLocation(d.width/2-w/2, d.height/2-h/2);

        //Schermata impostazioni
        JFrame impostazioni = new JFrame("Impostazioni KenKen");
        impostazioni.setSize(400,400);
        impostazioni.setLayout(new GridLayout(2,1));
        JPanel pannelloImpostazioni = new JPanel();
        impostazioni.setLocation(d.width/2-w/2, d.height/2-h/2);
        impostazioni.add(pannelloImpostazioni);
        impostazioni.setVisible(false);
        pannelloImpostazioni.setBounds(400, 200,400,100);
        pannelloImpostazioni.setBackground(Color.black);
        JLabel scritta1 = new JLabel("  IMPOSTA PARTITA");
        scritta1.setFont(fontTitolo);
        JLabel scritta2 = new JLabel("      Scegli la dimensione della griglia,");
        JLabel scritta3 = new JLabel("         poi clicca su 'CONFERMA'");
        Font scritta = new Font("Times New Roman", Font.PLAIN, 24);
        scritta2.setFont(scritta);
        scritta3.setFont(scritta);
        scritta1.setForeground(Color.white);
        scritta2.setForeground(Color.white);
        scritta3.setForeground(Color.white);
        pannelloImpostazioni.add(scritta1);
        pannelloImpostazioni.add(scritta2);
        pannelloImpostazioni.add(scritta3);

        for(int i = 3; i<=6; i++){
            JButton nuovoBottone = new JButton(i+"x"+i);
            nuovoBottone.setForeground(Color.white);
            nuovoBottone.setBackground(Color.black);
            int finalI = i;
            nuovoBottone.setBounds(100,150,200,50);
            nuovoBottone.addActionListener(evt -> commandHandler.handle(new CreaGriglia(griglia, pannelloGriglia, pannelloDiControllo, finalI)));

            pannelloImpostazioni.setLayout(new GridLayout(9,1));
            pannelloImpostazioni.add(nuovoBottone);

        }

        JButton conferma = new JButton("CONFERMA");
        conferma.setBackground(Color.black);
        conferma.setForeground(Color.white);
        conferma.setBounds(100,150,200,50);
        pannelloImpostazioni.add(conferma);
        conferma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
                frame.setSize(500,650);
                int larghezza = frame.getWidth();
                frame.setLocation(d.width/2-larghezza/2 , d.height/14);
                impostazioni.setVisible(false);
            }
        });


        JButton nuovaPartita = new JButton("NUOVA PARTITA");
        nuovaPartita.setBounds(100,150,200,50);
        nuovaPartita.setBackground(Color.BLACK);
        nuovaPartita.setForeground(Color.white);
        nuovaPartita.setEnabled(true);
        nuovaPartita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                impostazioni.setLocation(d.width/2-w/2, d.height/2-h/2);
                impostazioni.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                impostazioni.getContentPane().setBackground(Color.black);
                impostazioni.setLayout(new GridLayout(1,1));
                impostazioni.setVisible(true);
                menu.dispose();
            }
        });

        JButton caricaPartita = new JButton("CARICA PARTITA");
        caricaPartita.setBounds(100,200,200,50);
        caricaPartita.setForeground(Color.white);
        caricaPartita.setBackground(Color.BLACK);
        menu.add(nuovaPartita);
        menu.add(caricaPartita);
        caricaPartita.setEnabled(true);
        //caricaPartita.addActionListener(evt -> commandHandler.handle(new CaricaPartita(griglia, pannelloGriglia, pannelloDiControllo)));
        caricaPartita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                commandHandler.handle(new CaricaPartita(griglia, pannelloGriglia, pannelloDiControllo));
                menu.dispose();
                frame.setVisible(true);
            }
        });

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(Box.createRigidArea(new Dimension(20,0)));
        contentPane.add(pannelloDiControllo);
        contentPane.add(Box.createRigidArea(new Dimension(20,0)));
        contentPane.add(new JSeparator(JSeparator.CENTER));
        contentPane.add(Box.createRigidArea(new Dimension(20,0)));
        contentPane.add(pannelloGriglia);
        contentPane.add(Box.createRigidArea(new Dimension(20,0)));

    }
}
