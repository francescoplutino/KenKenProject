package gui;

import comandi.HistoryCommandHandler;
import comandiGui.InserisciNumero;
import componenti.Blocco;
import componenti.Casella;
import componenti.griglia.*;
import componenti.griglia.MathOperation;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.io.File;
import java.util.List;

public class PannelloGriglia extends JPanel implements Listener{

    private int grandezzaGriglia;
    private JToggleButton[][] bottoneGriglia;
    private JTextField[][] inputGriglia;
    public enum OPERAZIONI_FILE { CARICA, SALVA};

    public PannelloGriglia(Griglia griglia, HistoryCommandHandler commandHandler){
        this.grandezzaGriglia = griglia.getSize();
        this.bottoneGriglia = new JToggleButton[grandezzaGriglia][grandezzaGriglia];
        this.inputGriglia = new JTextField[grandezzaGriglia][grandezzaGriglia];
        griglia.addListener(this);
    }

    @Override
    public void grigliaMod(EventoGriglia e) {
        int n = e.getGriglia().getSize();
        if(e.isNuovaGriglia()){ //griglia ricreata o caricata
            ricreaGriglia(e.getGriglia(),n);
        }
        if(e.isSchemaAggiornato()){ //Ripristino di una griglia o di un blocco
            rifaiSchemaBlocchi(e.getGriglia().getSchema(), n);
            resettaSelezione();
        }
        if (e.isBloccoPulito()) { //cancellazione di tutti gli inserimenti in griglia
            for(int i = 0; i < grandezzaGriglia; i++){
                for(int j = 0; j < grandezzaGriglia; j++){
                    inputGriglia[i][j].setText("");
                }
            }
        }
        if(e.isIntegritaVerificata()){ //Controllo dei vincoli
            List<Casella> caselleDuplicate = e.getGriglia().getCaselleDuplicate();
            List<Blocco> bloccoRisultatoNonValido = e.getGriglia().getBloccoRisultatoNonValido();
            for (Casella c : caselleDuplicate){
                inputGriglia[c.getRiga()][c.getColonna()].setForeground(Color.RED); //Evidenzio i doppioni in rosso
            }
            if(!bloccoRisultatoNonValido.isEmpty()){
                for(Blocco b : bloccoRisultatoNonValido){
                    int minRow = Integer.MAX_VALUE;
                    int minCol = Integer.MAX_VALUE;
                    for(Casella c : b.getCaselle()){
                        int i = c.getRiga();
                        int j = c.getColonna();
                        if(i < minRow && j < minCol){
                            minCol = j;
                            minRow = i;
                        }
                    }
                    bottoneGriglia[minRow][minCol].setUI(new MetalButtonUI(){
                        protected Color getDisabledTextColor(){
                            return Color.RED;
                        }
                    });
                }
            }
        }
        if(e.isNumeroInserito()){ //inesrimento di un numero in griglia
            for(int i = 0; i < grandezzaGriglia; i++){
                for (int j = 0; j < grandezzaGriglia; j++){
                    inputGriglia[i][j].setForeground(Color.BLACK);
                    bottoneGriglia[i][j].setUI(new MetalButtonUI(){
                        protected Color getDisabledTextColor(){
                            return Color.BLACK;
                        }
                    });
                }
            }
        }
        if(e.isSoluzioneRichiesta()){ //restituisce la prima soluzione se esiste
            try {
                int[][] soluzioneCorrente = e.getGriglia().getSoluzioneCorrente();
                if (soluzioneCorrente != null){
                    for(int i = 0; i < grandezzaGriglia; i++){
                        for(int j = 0; j < grandezzaGriglia; j++){
                            inputGriglia[i][j].setText("");
                            inputGriglia[i][j].setText(soluzioneCorrente[i][j]+"");
                        }
                    }
                }
            }catch (RuntimeException error){
                showErrorDialog("","Partita irrisolvibile");
            }
        }
        repaint();
        revalidate();
    }

    private void ricreaGriglia(Griglia griglia, int n) {

        this.grandezzaGriglia = n;
        this.bottoneGriglia = new JToggleButton[n][n];
        this.inputGriglia= new JTextField[n][n];
        this.removeAll();
        this.setLayout(new GridLayout(grandezzaGriglia, grandezzaGriglia));
        for (int i = 0; i < grandezzaGriglia; i++) {
            for (int j = 0; j < grandezzaGriglia; j++) {
                JToggleButton casella = new JToggleButton();
                casella.setLayout(new BoxLayout(casella,BoxLayout.Y_AXIS));
                casella.add(Box.createRigidArea(new Dimension(0,25)));
                casella.setBackground(Color.WHITE);
                casella.setOpaque(true);

                bottoneGriglia[i][j] = casella;
                add(casella);

                JTextField input = new JTextField();
                input.setFont(input.getFont().deriveFont(Font.PLAIN, 30));
                input.setDisabledTextColor(Color.BLACK);
                input.setHorizontalAlignment(JTextField.CENTER);
                input.setBorder(BorderFactory.createEmptyBorder());
                InputFilter documentFilter = new InputFilter(grandezzaGriglia);
                ((AbstractDocument) input.getDocument()).setDocumentFilter(documentFilter);

                bottoneGriglia[i][j].add(input, BorderLayout.CENTER);
                inputGriglia[i][j] = input;
                input.setVisible(false);
                input.setEnabled(false);
                int riga = i;
                int colonna = j;
                // ascolta inserimenti e rimozioni all'interno del JTextField
                input.getDocument().addDocumentListener(new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent documentEvent) {
                        int val = Integer.parseInt(input.getText());
                        new InserisciNumero(griglia,riga,colonna,val);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent documentEvent) {
                        new InserisciNumero( griglia,riga,colonna,0);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent documentEvent) {}
                });
            }
        }
    }

    private void resettaSelezione() {
        for (int i = 0; i < grandezzaGriglia; i++) {
            for (int j = 0; j < grandezzaGriglia; j++) {
                bottoneGriglia[i][j].setSelected(false);
            }
        }
    }

    private void rifaiSchemaBlocchi(List<Blocco> schema, int n) {

        boolean[][] schemaCorrente = new boolean[n][n];

        for(Blocco b : schema) {
            boolean[][] blocco = new boolean[n][n];
            for(Casella c  : b.getCaselle()){
                int i = c.getRiga();
                int j = c.getColonna();
                int val = c.getValore();
                blocco[i][j] = true;
                schemaCorrente[i][j] = true;
                inputGriglia[i][j].setText(val == 0 ? "" : val + "");
            }
            int minRiga = Integer.MAX_VALUE;
            int minColonna = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if(blocco[i][j]){
                        disegnaBordiBottone(bottoneGriglia[i][j], i, j, blocco);
                        // individuo la cella più in alto a sinistra del blocco, dove inserire il risultato
                        // da ottenere combinando le cifre del blocco
                        if(i < minRiga && j < minColonna){
                            minRiga = i;
                            minColonna = j;
                        }
                    }
                }
            }
            disegnaBloccoRisultato(b.getRisultato(), b.getOperazione(), minRiga, minColonna);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(!schemaCorrente[i][j]) {
                    bottoneGriglia[i][j].updateUI();
                    bottoneGriglia[i][j].setBorder(UIManager.getBorder("Button.border"));
                    bottoneGriglia[i][j].setText("");
                    bottoneGriglia[i][j].setEnabled(true);
                    inputGriglia[i][j].setVisible(false);
                }
            }
        }

    }

    private void disegnaBloccoRisultato(int risultato, MathOperation operazione, int i, int j) {

        String op = null;
        switch(operazione) {
            case ADDIZIONE: op = "+"; break;
            case SOTTRAZIONE: op = "-"; break;
            case MOLTIPLICAZIONE: op = "*"; break;
            case DIVISIONE: op = "/"; break;
        }

        JToggleButton targetResultButton = bottoneGriglia[i][j];
        targetResultButton.setText(risultato + op);
        targetResultButton.setFont(targetResultButton.getFont().deriveFont(Font.BOLD, 20));
        targetResultButton.setHorizontalAlignment(SwingConstants.LEFT);
        targetResultButton.setVerticalAlignment(SwingConstants.NORTH);
        targetResultButton.setUI(new MetalButtonUI() {
            protected Color getDisabledTextColor() {
                return Color.BLACK;
            }
        });

    }

    private void disegnaBordiBottone(JToggleButton jToggleButton, int i, int j, boolean[][] caselleSel) {
        jToggleButton.setEnabled(false);
        inputGriglia[i][j].setVisible(true);
        int top, left, bottom, right;
        top = left = bottom = right = 4;
        if(i > 0 && caselleSel[i-1][j]) top = 1;
        if(i < caselleSel.length-1 && caselleSel[i+1][j]) bottom = 1;
        if(j > 0 && caselleSel[i][j-1]) left = 1;
        if(j < caselleSel.length-1 && caselleSel[i][j+1]) right = 1;
        jToggleButton.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
    }


    public void startGameView() {
        for (int i = 0; i < grandezzaGriglia; i++) {
            for (int j = 0; j < grandezzaGriglia; j++) {
                JTextField input = inputGriglia[i][j];
                input.setEnabled(true);
            }
        }
        revalidate();
    }

    public boolean[][] getCaselleSelezionate() {
        boolean[][] selectedSquares = new boolean[grandezzaGriglia][grandezzaGriglia];
        for (int i = 0; i < grandezzaGriglia; i++) {
            for (int j = 0; j < grandezzaGriglia; j++) {
                if(bottoneGriglia[i][j].isSelected()) selectedSquares[i][j] = true;
            }
        }
        return selectedSquares;
    }

    public int getCaselleBloccate() {
        int lockedSquares = 0;
        for (int i = 0; i < grandezzaGriglia; i++) {
            for (int j = 0; j < grandezzaGriglia; j++) {
                if(!bottoneGriglia[i][j].isEnabled())
                    lockedSquares++;
            }
        }
        return lockedSquares;
    }

    public int getInputRisultatoObbiettivo() {
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        boolean targetResultObtained = false;
        int result = 0;
        do {
            String input = JOptionPane.showInputDialog(mainFrame,"Inserisci il risultato da ottenere:");
            if(input==null)
                return -1;
            try {
                result = Integer.parseInt(input);
                targetResultObtained = true;
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(mainFrame,"Inserisci un numero intero", "Risultato non valido",JOptionPane.ERROR_MESSAGE);
            }
        }while(!targetResultObtained);
        return result;
    }

    public MathOperation getInputOperazione() {

        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        boolean operationObtained = false;
        String operazione = null;
        MathOperation op = null;
        do {
            String input = JOptionPane.showInputDialog(mainFrame,"Inserisci un'operazione:");
            if(input==null)
                return null;
            if(input.matches("[\\+\\-\\*/]")) {
                operazione = input;
                operationObtained = true;
            }
            else {
                JOptionPane.showMessageDialog(mainFrame,"Gli operatori validi sono: +, -, *, /.",
                        "Operatore non valido",JOptionPane.ERROR_MESSAGE);
            }
        }while(!operationObtained);

        switch(operazione){
            case "+": op = MathOperation.ADDIZIONE; break;
            case "*": op = MathOperation.MOLTIPLICAZIONE; break;
            case "-": op = MathOperation.SOTTRAZIONE; break;
            case "/": op = MathOperation.DIVISIONE; break;
        }
        return op;
    }

    public String getFilePathInput(OPERAZIONI_FILE fileOp) {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("partita.ken"));
        chooser.setFileFilter(new FileNameExtensionFilter("file .ken ","ken"));
        String filePath = null;
        if(chooser.showDialog(this, "Seleziona") == JFileChooser.APPROVE_OPTION) {
            filePath = chooser.getSelectedFile().getAbsolutePath();
            if (!filePath .endsWith(".ken"))
                filePath += ".ken";
            if(fileOp == OPERAZIONI_FILE.SALVA && new File(filePath).exists()) {
                int ret = JOptionPane.showConfirmDialog(topFrame,
                        "Vuoi sovrascrivere il file ?", "File già esistente", JOptionPane.YES_NO_OPTION);
                if(ret == JOptionPane.YES_OPTION) return filePath;
                else return null;
            }
        }
        return filePath;
    }

    public int getInputMaxSoluzioni() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        int maxSolutions = 0;
        boolean validInput = false;
        do {
            String input = JOptionPane.showInputDialog(topFrame, "Inserisci il valore massimo di soluzioni da mostrare: ");
            if (input == null)
                return -1;
            try {
                maxSolutions = Integer.parseInt(input);
                validInput = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(topFrame, "Inserisci un numero !", "Errore input", JOptionPane.ERROR_MESSAGE);
            }
        }while(!validInput);
        return maxSolutions;
    }

    public void showErrorDialog(String title, String message) {
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JOptionPane.showMessageDialog(mainFrame, message, title,JOptionPane.ERROR_MESSAGE);
    }

    public boolean showNewSizeDialog() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        int ret = JOptionPane.showConfirmDialog(topFrame,
                "Sei sicuro di voler cambiare dimensione alla griglia ?", "Crea nuova partita", JOptionPane.YES_NO_OPTION);
        return ret == JOptionPane.YES_OPTION;
    }

    static class InputFilter extends DocumentFilter {

        private final int dimGriglia;

        public InputFilter(int dimGriglia) { this.dimGriglia = dimGriglia; }

        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attribute) throws BadLocationException {
            int documentLength = fb.getDocument().getLength();
            if (documentLength - length + text.length() <= 1) {
                fb.getDocument().remove(0,documentLength); // svuota il JTextField
                fb.insertString(offset, text.replaceAll("[^1-" + dimGriglia + "]", ""), attribute);
            }
        }
    }
}
