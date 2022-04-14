
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Licitatie implements Runnable {
    int id;
    int nrParticipanti;
    int idProdus;
    int nrPasiMaxim;
    int nrCurentParticipanti = 0;
    List<Broker> brokers = new CopyOnWriteArrayList<>(); //lista de brokeri a licitatiei
    Map<Client, Double> clientMap = new ConcurrentHashMap<>(); // map in care tinem cat a licitat fiecare client
                                                                //la fiecare pas
    Map<Client, Boolean> stareParticipare = new ConcurrentHashMap<>(); //map in care retinem daca mai exista participanti
    //care mai au bani sa liciteze peste castigator
    double maxSum; //suma maxima care s-a licitat pana la un pas
    Client castigator = null;
    int round;

    //metoda care intoarce un produs dupa id-ul sau
    private Produs getProdusByID() {
        for(Produs p : CasaLicitatii.getInstance().produse)
            if(p.id == idProdus)
                return p;
        return null;
    }



    public Licitatie(int id, int idProdus, int nrPasiMaxim, int nrParticipanti) {
        this.id = id;
        this.idProdus = idProdus;
        this.nrPasiMaxim = nrPasiMaxim;
        this.nrParticipanti = nrParticipanti;
    }

    public void cresteNrCurentParticipanti() {
        nrCurentParticipanti++;
    }

    public void notifyBrokers(int round) {
        for (Broker broker : brokers) {
            broker.update(this, round);
        }
        //brokerii vor fi notificati de inceperea unui nou pas al licitatiei
    }

    private void setRound(int round) {
        this.round = round;
        notifyBrokers(round);
    }

    private double obtineComisionCastigator(Client castigator, double maxSum) {
        Broker brokerAsociat;
        brokerAsociat = castigator.obtineBroker(this.id);
        double comision = brokerAsociat.calculeazaComision(castigator, maxSum);
        return comision;
    }

    @Override
    public void run() {
        for(int i = 1; i < nrPasiMaxim + 1; i++) {
            setRound(i);
            int retrasi = 0;
            //La fiecare pas al licitatiei sunt notificati brokerii
            for (Map.Entry mapElement : clientMap.entrySet()) {
                if ((double) mapElement.getValue() > maxSum) {
                    maxSum = (double) mapElement.getValue();
                    castigator =  (Client)mapElement.getKey();
                } else if((double) mapElement.getValue() == maxSum) {
                    if(castigator != null)
                        if(((Client)mapElement.getKey()).nrLicitatiiCastigate > castigator.nrLicitatiiCastigate)
                            castigator = (Client)mapElement.getKey();
                }
                //mai sus am parcurs Map-ul de CLienti si Sumele pe care le-au licitat pana la acest pas
                //astfel vom obtine suma maxima care s-a licitat pana la acest pas dar si clientul respectiv
            }
            if(castigator != null)
                System.out.println("Licitatie ID: " + id+ " | Dupa runda " + i + " " +castigator.nume + " conduce.");
            for(Map.Entry mapElement:stareParticipare.entrySet())
                if(!((Boolean) mapElement.getValue()))
                    retrasi++;
            if(nrCurentParticipanti == retrasi + 1)
                break;
            //parcurgem map-ul care ne arata starea participantilor si vedem daca exista mai multi clienti
            //care mai au bani sa liciteze. Daca doar un singur participant a ramas in licitatie
            //il vom declara castigator
        }
        System.out.println(castigator.nume + " a castigat cu " + maxSum + " licitatia " + id + " pentru "+
                getProdusByID().nume +" din anul "+getProdusByID().an + "." + " I-a fost aplicat un comision de " +
                obtineComisionCastigator(castigator, maxSum));
        castigator.increaseNrLicitatiiCastigate();
        getProdusByID().pretVanzare = maxSum;
        //crestem numarul de licitatii castigate castigatorului si setam pretul de vanzare pt produs
        Thread thread = new Thread(() -> brokers.get((int) ((Math.random() * (brokers.size())))).stergeProdus(idProdus));
        thread.start();
        //pornim un thread pentru stergerea produsului din lista dupa ce s-a vandut
    }
}
