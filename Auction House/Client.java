import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Client implements Update {
    int id;
    String nume;
    String adresa;
    int nrParticipari;
    int nrLicitatiiCastigate;
    Map<Integer, Map.Entry<Broker, Double>> bugetLicitatii = new ConcurrentHashMap<>();

    void increaseNrParticipari() {
        nrParticipari++;
    }

    //metoda care obtine broker-ul clientului intr-o anumita licitatie
    Broker obtineBroker(int idLicitatie) {
        for(Map.Entry mapElem: bugetLicitatii.entrySet()) {
            if((Integer)mapElem.getKey() == idLicitatie) {
                return (Broker) ((Map.Entry)mapElem.getValue()).getKey();
            }
        }
        return null;
    }

    void increaseNrLicitatiiCastigate() {
        nrLicitatiiCastigate++;
    }

    @Override
    //metoda in care clientul isi exprima suma pe care o poate licita in licitatia respectiva
    public void update(Licitatie licitatie, int round) {
        double buget = 0;
        Map.Entry entry = null;
        for (Map.Entry mapElement : bugetLicitatii.entrySet()) {
            entry = (Map.Entry) mapElement.getValue();
            if ((Integer)mapElement.getKey() == licitatie.id) {
                buget = (Double)entry.getValue();
                break;
            }
        }//vom extrage bugetul alocat pentru licitatia actuala in "buget"
        if (buget > licitatie.maxSum) {//verificam daca mai are posibilitatea sa liciteze
            double sumaNoua = (Math.random() * (buget - licitatie.maxSum)) + licitatie.maxSum;
            System.out.println("Licitatie ID: " + licitatie.id + " | La runda " + round+ " " + nume +
                    " a licitat " + sumaNoua);
            ((Broker)entry.getKey()).liciteazaPentruClient(licitatie, sumaNoua, this.id);
            }//se decide pentru o suma noua de licitatie, iar brokerul isi face treaba si ii liciteaza
        else {
            for (Map.Entry mapElement : licitatie.stareParticipare.entrySet()) {
                if (this.id == ((Client) mapElement.getKey()).id) {
                    mapElement.setValue(false);
                }//daca nu mai are bani sa liciteze ii vom seta starea inactiva
            }
        }
    }
}
