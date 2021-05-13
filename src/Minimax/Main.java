package Minimax;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private static JFrame frame = new JFrame("CubesGame with MiniMax by Nikolaos Psaltakis");
    private JPanel panel;
    private JPanel infoPanel;
    private JPanel buttonPanel;
    private JLabel headerLabel;
    private JLabel mLabel;
    private JLabel kLabel;
    private JLabel info1;
    private JLabel info2;
    private JLabel info3;
    private JLabel info4;
    private JLabel info5;
    private JLabel info6;
    private JLabel info7;
    private JButton settingsBTN;
    private JButton playBTN;
    private JButton takeCubesBTN;
    private JPanel settingsPanel;
    private JLabel settingsM;
    private JLabel settingsK;
    private JTextField mTextField;
    private JTextField Ktextfield;
    private JTextField numberTextfield;
    private JLabel chooseCubesLabel;
    private JPanel playPanel;
    private JPanel CubesNumPanel;
    private JPanel panel2;
    private JDialog dialog;
    // Δηλώνουμε τον αριθμό των κύβων και
    // τον αριθμό των μέγιστων κύβων που
    //μπορεί να πάρει ο παιχτής
    private boolean max = true;
    private int m = 0;
    private int k = 0;
    private int num = 0;
    private String currentPlayer;
    private String player_1 = "MAX";
    private String player_2 = "PLAYER 2";
    private ArrayList<Integer> availableMoves = new ArrayList();
    private HashMap<Integer,String> result = new HashMap<Integer,String>();

    public Main(){
        //border
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 7));
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        settingsPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        //Φτιάχνουμε το Hashmap για τα μυνήματα ποιος Νίκησε
        result.put(1,"Νίκησε ο MAX");
        result.put(-1,"Συγχαρητηρια νίκησε ο PLAYER 2");
        //Οριζουμε τον τρέχον player τον MAX
        currentPlayer = player_1;
        //Απενεργοποίηση των πεδίων που δεν χρειαζόμαστε στην εκκίνηση του παιχνιδιού.
        disableElements();

        //EventListeners
        playBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameSettings();
            }
        });

        takeCubesBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.this.startGame();
            }
        });

        settingsBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameReload();
            }
        });

        //Αν πατήσουμε εντερ να κάνει αποδοχή της τιμής και να πηγαίνει στο επόμενο πεδίο
        //Αν πατήσουμε esc να ζητά επιβαιβαίωση για έξοδο
        mTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Ktextfield.requestFocus();
                }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    exitGame();
                }
            }
        });

        Ktextfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gameSettings();
                }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    exitGame();
                }
            }
        });

        numberTextfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    startGame();
                }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    exitGame();
                }
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                int result = JOptionPane.showConfirmDialog(frame,
                        "Θέλετε σίγουρα να κλείσετε την Εφαρμογή ?", "Εξοδος : ",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION){
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
                else if (result == JOptionPane.NO_OPTION) {
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    //Απενεργοποίηση των πεδίων που δεν χρειαόμαστε στην εκκίνηση του παιχνιδιού.
    public void disableElements() {
        mLabel.setVisible(false);
        kLabel.setVisible(false);
        chooseCubesLabel.setVisible(false);
        numberTextfield.setVisible(false);
        settingsBTN.setVisible(false);
        takeCubesBTN.setVisible(false);
    }

    //Με το που νικήσει κάποιος παίχτης γίενται επαναφόρτηση του παιχνιδιού.
    public void gameReload() {
        disableElements();
        //loadingGUI();
        settingsM.setVisible(true);
        settingsK.setVisible(true);
        mTextField.setVisible(true);
        Ktextfield.setVisible(true);
        playBTN.setVisible(true);
    }

    //Βάζουμε σε μια λίστα όλες τις πιθανές κινήσεις που μπορούν να κάνουν οι παίχτες
    public void insertAvailableMoves() {
        availableMoves.clear();
        availableMoves.add(1);
        availableMoves.add(2);
        availableMoves.add(k);
    }

    //Ελέγχουμε αν μπορούν να γίνουν ολες οι πιθανές κίνησεις.
    //Αν κάποια δεν μπορεί να γίνει την αφαιρόυμε απο την λίστα.
    public void checkAvailableMoves() {
        for (int i = 0; i<availableMoves.size(); i++) {
            if (m< availableMoves.get(i)){
                availableMoves.remove(i);
            }
        }
    }

    //Έλεγχος των ρυθμήσεων που δώσαμε
    public void gameSettings() {
        try {
            /*
            Δηλώνουμε τις τιμές για τον αριθμό των κύβων πάνω στι τραπέζι
            και τον μέγιστο αριθμό των κύβων που μπορούμε να σηκώσουμε
            απο το τραπεζι
            */
            m = Integer.parseInt(mTextField.getText());
            k = Integer.parseInt(Ktextfield.getText());
            if (m < 0){
                JOptionPane.showMessageDialog(panel, "Λάθος Αριθμός κύβων. Οι κύβοι πρέπει να είναι πάνω απο 0",
                        "Προσοχη", JOptionPane.INFORMATION_MESSAGE);
                mTextField.requestFocus();
            } else if(k >= m || k <= 2) {
                JOptionPane.showMessageDialog(panel, "Λάθος Αριθμός κύβων. Ο αριθμός των κύβων που μπορουν " +
                                "να σηκωθουν ταυτόχρονα πρέπει να είναι πάνω απο 2 και μικρότερος απο τον Αριθμο των Κύβων στο τραπέζι",
                        "Προσοχη", JOptionPane.INFORMATION_MESSAGE);
                Ktextfield.requestFocus();
            } else{
                insertAvailableMoves();
                changeElementsVisible();
            }
        } catch (Exception exp) {
            JOptionPane.showMessageDialog(panel, "Δεν πληκτρολογήσατε Νούμερο",
                    "Προσοχη", JOptionPane.INFORMATION_MESSAGE);
            mTextField.requestFocus();
        }
    }

    /*
        Εξαφανίζουμε όλα τα elements των ρυθμήσεων του παιχνιδιού
        και εμφανίζουμε τα elements του παιχνιδιού
    */
    public void changeElementsVisible() {
        mLabel.setVisible(false);
        kLabel.setVisible(false);
        mTextField.setVisible(false);
        Ktextfield.setVisible(false);
        settingsM.setVisible(false);
        settingsK.setVisible(false);
        playBTN.setVisible(false);
        mLabel.setVisible(true);
        kLabel.setVisible(true);
        chooseCubesLabel.setVisible(true);
        numberTextfield.setVisible(true);
        settingsBTN.setVisible(true);
        takeCubesBTN.setVisible(true);
        setTitles();
        minimax();
    }

    //καλούμε την checkAvailableMoves(); για να τσεκάρουμε τι κινήσεις μπορούμε να κανουμε
    //και αλλάζουμε και τα κείμενα στις επικεφαλίδες ις εφαρμογής
    public void setTitles() {
        String text = "";
        //Αλλάζουμε το κείμενο στις ετικέτες (JLabel)
        mLabel.setText(Integer.toString(m));
        if (m >= k) {
            text = "Μπορείς να πάρεις 1 ή 2 ή " + availableMoves.get(availableMoves.size()-1) + " κύβους";
        }else if (m >= 2) {
            text = "Μπορείς να πάρεις 1 ή 2" + " κύβους";
        }else {
            text = "Μπορείς να πάρεις 1 κύβο";
        }
        kLabel.setText(text);
    }

    public void startGame() {
        /*
        Εκκίνηση του παιχνιδιού και έλεγχος του αριθμού που δίνει ο κάθε χρήστης να
        είναι εντος ορίων
         */
        if (m > 0) {
            num = 0;
            try {
                num = Integer.parseInt(numberTextfield.getText());
                if (availableMoves.contains(num)) {
                    Minimax.State state = new Minimax.State(m,false,k);
                    Minimax.State decision = Minimax.bestDecision(state);
                    play(num);
                    //έλεγχος αν νίκησε κάποιος παίχτης
                    //Τσεκάρουμε αν Νίκησε ο MIN
                    if (decision.isTerminal()) {
                        int check = decision.getUtility();
                        printWinner(check);
                    }else {
                        //Παίζει ο MAX και ενημερώνουμε τα νούμερα των κύβων στο τραπέζι.
                        currentPlayer = player_1;
                        numberTextfield.setText("");
                        setTitles();
                        minimax();
                    }
                }else {
                    JOptionPane.showMessageDialog(panel, "Δώσατε Τιμή εκτός ορίων",
                            "ΣΦΑΛΜΑ", JOptionPane.INFORMATION_MESSAGE);
                }
            }catch (Exception exp) {
                JOptionPane.showMessageDialog(panel, "Δώσατε μη αποδεκτή Τιμή",
                        "ΣΦΑΛΜΑ", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    //MAX player
    public void minimax(){
        /*
        Ο κωδίκας με τον οποίο θα υλοποιήσουμε τον max player
         */
        //Loading Window
        int temp = m;
        //Δημιουργια αντικειμένου
        Minimax.State state = new Minimax.State(m,true,k);
        //Υπολογίζουμε όλες τις πιθανές τελικές καταστάσεις
        System.out.println("Υπολογίζουμε όλες τις πιθανές Καταστάσεις");
        System.out.println("Παρακαλώ Περιμένετε...");
        if (m > 24) {
            JOptionPane.showMessageDialog(panel, "Υπολογίζουμε όλες τις πιθανές Καταστάσεις. Αυτο μπορεί να διαρκέσει μερικά λεπτά.",
                    "Παρακαλώ Περιμένετε...", JOptionPane.INFORMATION_MESSAGE);
        }
        Minimax.State decision = Minimax.bestDecision(state);
        //Τελος υπολογσισμού υελικών καταστάσεων
        System.out.println("Όλα έτοιμα");
        if (m > 24) {
            JOptionPane.showMessageDialog(panel, "Όλα έτοιμα",
                    "ΠΛΗΡΟΦΟΡΙΑ", JOptionPane.INFORMATION_MESSAGE);
        }
        //Νεος αριθμός κύβων στο τραπέζι
        m = decision.getM();
        //Πόοσυς πήρε ο υπολογιστής απο το τραπέζι
        int move = (temp - m);
        JOptionPane.showMessageDialog(panel, "Ο ΜΑΧ επέλεξε το νούμερο " + move,
                "ΠΛΗΡΟΦΟΡΙΑ", JOptionPane.INFORMATION_MESSAGE);
        //Τσεκάρουμε αν Νίκησε ο MAX.
        if (decision.isTerminal()) {
            int check = decision.getUtility();
            printWinner(check);
        }else {
            //Παίζει ο δεύτερος παίχτης και ενημερώνουμε τα νούμερα των κύβων
            //και των πιθανών κινήσεων που μπορεί να κάνει π Παιχτης.
            currentPlayer = player_2;
            checkAvailableMoves();
            setTitles();
        }
    }

    //Κανει την κίνηση του player 2 για να γίνει έλεγχος αν νίκησε κάποιος παίχτης
    public void play(int num) {
        m -= num;
        checkAvailableMoves();
        setTitles();
    }

    public void printWinner(int check) {
        if (result.containsKey(check)) {
            //εμφανίζουμε ποιος νίκησε
            JOptionPane.showMessageDialog(panel, result.get(check),
                    "ΤΕΛΟΣ ΠΑΙΧΝΙΔΙΟΥ", JOptionPane.INFORMATION_MESSAGE);
            gameReload();
        }
    }

    public void exitGame() {
        int result = JOptionPane.showConfirmDialog(frame,
                "Θέλετε σίγουρα να κλείσετε την Εφαρμογή ?", "Εξοδος : ",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION){
            frame.dispose();
        }
    }

    public static void main(String[] args) {
        frame.setContentPane(new Main().panel);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(700,530));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}