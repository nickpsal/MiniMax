package Minimax;

import java.util.*;

public final class Minimax {
    public static class State {
        //m =  τρέχων αριθμός των κύβων
        // max και min ποιος παίζει
        // k ο μεγιστος αριθμός των κύβων που μπορεί να πάρει
        private int m;
        private final boolean max;
        private final boolean min;
        private final int k;

        //constructor
        public State(int m, boolean max, int k){
            this.m = m;
            this.max = max;
            this.min = !max;
            this.k = k;
        }

        //getter - setter
        public int getM() {
            return m;
        }

        public void setM(int m) {
            this.m = m;
        }

        //Υπολογίζουμε τις πιθανές Τελικές Καταστάσεις
        public List<State> getNodes(){
            List<State> nodes = new LinkedList<>();
            //αφαιρούμε k κύβους
            int pick_3 = m - k;
            //αφαιρούμε 2 κύβους
            int pick_2 = m - 2;
            //αφαιρούμε 1 κύβο
            int pick_1 = m - 1;
            if(pick_3 >= 0){
                //Αν το αποτέλεσμα της αφαίρεσης της τρέχουσας κατάστασης - k >=0 προσθέτουμε το αποτέλεσμα στην Λίστα
                nodes.add(new State(pick_3, min, k));
            }
            if(pick_2 >= 0){
                //Αν το αποτέλεσμα της αφαίρεσης της τρέχουσας κατάστασης - 2 >=0 προσθέτουμε το αποτέλεσμα στην Λίστα
                nodes.add(new State(pick_2, min, k));
            }
            if(pick_1 >= 0){
                //Αν το αποτέλεσμα της αφαίρεσης της τρέχουσας κατάστασης - 1 >=0 προσθέτουμε το αποτέλεσμα στην Λίστα
                nodes.add(new State(pick_1, min, k));
            }
            return nodes;
        }

        //Έλεγχος άν η κατλασταση είναι τερματική
        public boolean isTerminal() {
            if (m <= 0) {
                return true;
            }
            return false;
        }

        //Επιστρέφει 1 αν έφτασε σε κατάσταση που νικάει ο MAX
        //Επιστρέφει -1 αν έφτασε σε κατάσταση που νικάει ο MIN
        public int getUtility() {
            if(min) {
                return 1;
            }
            return -1;
        }
    }

    //Επιστρέφει την Καλύτερη Κίνηση για τον MAX
    public static State bestDecision(State state) {
        boolean found = false;
        State best = null;
        Comparator<State> comparator = Comparator.comparing(Minimax::runMinimax);
        for (State state1 : state.getNodes()) {
            if (!found || comparator.compare(state1, best) == 1) {
                found = true;
                best = state1;
            }
        }
        if (found) {
            return Optional.of(best).get();
        }
        return Optional.<State>empty().get();
    }

    //Τρέχει τον αλγόριθμο
    protected static double runMinimax(State state) {
        //Ελεγχος αν η κατάσταση είναι τερματική
        if (!state.isTerminal()){
            //ΈΛΕγχος αν αυτός που παίζει είναι ο MAX
            if (state.max){
                //Σε κάθε επίπεδο του δέντρου επιστρέφει το μέγιστο
                boolean found = false;
                Double best = null;
                Comparator<Double> comparator = Comparator.comparing(Double::valueOf);
                for (State state1 : state.getNodes()) {
                    Double maxValue = runMinimax(state1);
                    if (!found || comparator.compare(maxValue, best) == -1) {
                        found = true;
                        best = maxValue;
                    }
                }
                if (found) {
                    return Optional.of(best).get();
                }
                return Optional.<Double>empty().get();
            }
            //Σε κάθε επίπεδο του δέντρου επιστρέφει το ελάχιστο
            boolean found = false;
            Double best = null;
            Comparator<Double> comparator = Comparator.comparing(Double::valueOf);
            for (State state1 : state.getNodes()) {
                Double minValue = runMinimax(state1);
                if (!found || comparator.compare(minValue, best) == 1) {
                    found = true;
                    best = minValue;
                }
            }
            if (found) {
                return Optional.of(best).get();
            }
            return Optional.<Double>empty().get();
        }
        return state.getUtility();
    }
}