
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * Tipul Harta.
 */
public class Harta {
    private final int nrPuncte;
    private final LinkedList<Strada>[] listaStrazi;

    /**
     * Instantiates a new Harta.
     *
     * @param nrPuncte Numarul de puncte de pe harta
     */
    public Harta(int nrPuncte) {
        this.nrPuncte = nrPuncte;
        listaStrazi = new LinkedList[nrPuncte];
        for (int i = 0; i < nrPuncte; i++) {
            listaStrazi[i] = new LinkedList<>();
        }
    }

    /**
     * Tipul Strada.
     */
    static class Strada {
        /**
         * Punctul de pe care incepe strada.
         */
        final int sursa;
        /**
         * Punctul unde se termina strada.
         */
        final int destinatie;
        /**
         * Costul strazii.
         */
        final int cost;
        /**
         * Costul ambuteiajelor asociat strazii.
         */
        int cost_ambuteiaje = 0;
        /**
         * Limita gabaritului strazii.
         */
        final int limita_gabarit;
        /**
         * Lista ambuteiajelor de pe strada.
         */
        LinkedList<Ambuteiaj> lista_ambuteiaje = new LinkedList<>();

        /**
         * Instantiates a new Strada.
         *
         * @param sursa          Punctul unde incepe strada
         * @param destinatie     Punctul unde se termina strada
         * @param cost           Costul strazii
         * @param limita_gabarit Limita gabaritului strazii
         */
        public Strada(int sursa, int destinatie, int cost, int limita_gabarit) {
            this.sursa = sursa;
            this.destinatie = destinatie;
            this.cost = cost;
            this.limita_gabarit = limita_gabarit;
        }
    }

    /**
     * Add street.
     *
     *  @param sursa          Punctul unde incepe strada
     *  @param destinatie     Punctul unde se termina strada
     *  @param cost           Costul strazii
     *  @param limita_gabarit Limita gabaritului strazii
     */
    public void addStreet(int sursa, int destinatie, int cost, int limita_gabarit) {
        Strada strada = new Strada(sursa, destinatie, cost, limita_gabarit);
        listaStrazi[sursa].addFirst(strada);
    }

    /**
     * Add restriction.
     *
     * @param ambuteiaj Ambuteiajul care apare pe strada
     */
    //functie in care adaugam o restrictie, actualizand costul ambuteiajelor de pe strada
    //si adaugand ambuteiajul in lista de ambuteiaje a strazii
    public void addRestriction(Ambuteiaj ambuteiaj) {
        LinkedList<Strada> list = listaStrazi[ambuteiaj.getSursa()];
        for(int i = 0; i < list.size(); i++)
            if (list.get(i).destinatie == ambuteiaj.getDestinatie()) {
                listaStrazi[ambuteiaj.getSursa()].get(i).cost_ambuteiaje += ambuteiaj.getCost();
                listaStrazi[ambuteiaj.getSursa()].get(i).lista_ambuteiaje.add(ambuteiaj);
            }
    }

    /**
     * Drive.
     *
     * @param punctSursa      Punctul de unde se incepe drive
     * @param punctDestinatie Punctul unde se termina drive
     * @param v               Vehiculul care realizeaza drive
     */
    public void drive(int punctSursa, int punctDestinatie, Vehicul v) {
        class Pereche {
            final int cost;
            final int punct;
            //clasa interna pereche in care vom tine costul minimim pana la un punct si punctul
            public Pereche(int cost, int punct) {
                this.cost = cost;
                this.punct = punct;
            }

            public int getCost() {
                return cost;
            }

            public int getPunct() {
                return punct;
            }
        }
        //vector de booleani in care notam daca s-a ajuns la costul minim
        boolean[] verificat = new boolean[nrPuncte];
        //vector de costuri minime pana la fiecare punct
        int [] costMinim = new int[nrPuncte];
        //vector in care vom tine parintii punctelor d.p.d.v al costurilor minime
        //de exemplu: daca parinteCostMinim[5] = 2, atunci inseamna ca in punctul 5
        //se ajunge la costul minim trecandu-se inainte prin punctul 2
        int [] parinteCostMinim = new int[nrPuncte];
        parinteCostMinim[punctSursa] = -1;
        parinteCostMinim[punctDestinatie] = punctSursa;
        //setam costurile foarte mari, ca la final sa putem verifica daca nu s-a gasit un drum
        for (int i = 0; i < nrPuncte ; i++) {
            costMinim[i] = Integer.MAX_VALUE;
        }
        //clasa interna in care com compara 2 perechi in functie de cost
        class Compara implements Comparator<Pereche> {
            public int compare(Pereche p1, Pereche p2) {
                int d1 = p1.getCost();
                int d2 = p2.getCost();
                return d1 - d2;
            }
        }
        Comparator<Pereche> comparator = new Compara();
        PriorityQueue<Pereche> coada = new PriorityQueue<>(nrPuncte, comparator);
        costMinim[punctSursa] = 0;
        Pereche p0 = new Pereche(costMinim[punctSursa], punctSursa);
        //adaugam prima pereche in coada de prioritati
        coada.offer(p0);
        while(!coada.isEmpty()){
            //extragem perechea cu costul cel mai mic
            Pereche perecheExtrasa = coada.poll();
            int punctCoada = perecheExtrasa.getPunct();
            if(!verificat[punctCoada] && !verificat[punctDestinatie] ) {
                //atunci cand punctul de destinatie a fost verificat ne oprim din algoritm
                verificat[punctCoada] = true;
                LinkedList<Strada> list = listaStrazi[punctCoada];
                //verificam fiecare strada cu sursa punctul pe care l-am extras
                for (int i = 0; i < list.size(); i++) {
                    Strada edge = list.get(i);
                    int destinatie = edge.destinatie;
                    //daca punctul pe care ajunge strada nu a fost verificat incercam sa actualizam
                    if (!verificat[destinatie] && v.getGabarit() <= edge.limita_gabarit) {
                        //verificam daca costul necesita actualizare
                        int costNou =  costMinim[punctCoada] + edge.cost*v.getCost() + edge.cost_ambuteiaje ;
                        int costCurent = costMinim[destinatie];
                        if(costCurent > costNou){
                            Pereche p = new Pereche(costNou, destinatie);
                            coada.offer(p);
                            costMinim[destinatie] = costNou;
                            parinteCostMinim[destinatie] = punctCoada;
                        }//daca costul nou este mai mic, actualizam, cream o pereche cu acesta si
                    }    //o introducem in coada. Actualizam si vectorul de parinti
                }
            }
        }
        boolean [] negasit = new boolean[nrPuncte];
        //daca nu s-a gasit un drum, inseamna ca costul este null
        for(int i = 0; i < nrPuncte; i++)
            if(costMinim[i] == Integer.MAX_VALUE) {
                costMinim[i] = 0;
                negasit[i] = true;
            }
        printDrive(parinteCostMinim, costMinim, punctDestinatie, negasit);
    }

    /**
     * Print drive.
     *
     * @param costMinim          Vectorul de puncte parinte ale punctelor pe unde se realizeaza drumul cel mai scurt
     * @param parinteCostMinim        Vectorul de distante cele mai scurte asociate fiecarui punct
     * @param punctDestinatie Punctul unde trebuie sa se ajunga in comanda drive
     */
    //functie in care printam drumul pana la un anumit punct, urmat de costul minim asociat
    public void printDrive(int[] parinteCostMinim, int[] costMinim, int punctDestinatie, boolean[] negasit) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("map.out", true));
            PrintWriter pw = new PrintWriter(bw);
            printDrum(parinteCostMinim, punctDestinatie, pw);
            if (costMinim[punctDestinatie] == 0 && negasit[punctDestinatie])
                pw.println("null");
            else
            pw.println(costMinim[punctDestinatie]);
            pw.close();
        } catch(IOException ignored) {}
    }


    /**
     * Print drum.
     *
     * @param parinteCostMinim       Vectorul de puncte parinte ale punctelor pe unde se realizeaza drumul cel mai scurt
     * @param destinatie  Punctul destinatie unde trebuie sa se ajunga in comanda drive
     */

    //in urmatoarea functie printam recursiv calea pe care s-a ajuns la costul minim
    //apelurile recursive se opresc cand va trebui sa se printeze punctul sursa(inceputul)
    public void printDrum(int[] parinteCostMinim, int destinatie, PrintWriter pw) {
        if(parinteCostMinim[destinatie] == -1) {
            pw.print("P" + destinatie + " ");
            return;
        }
        printDrum(parinteCostMinim, parinteCostMinim[destinatie], pw);
        pw.print("P" + destinatie + " ");
    }
}
