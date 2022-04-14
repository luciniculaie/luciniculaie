import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Broker extends Angajat implements Update {
    Map<Integer, CopyOnWriteArrayList<Client>> lista_Clienti = new ConcurrentHashMap<>();

    public Broker() {
    }

    public Broker(String nume) {
        this.nume = nume;
    }

    //metoda care ii permite brokerului sa stearga un element din lista de produse
    void stergeProdus(int idProdus) {
        CasaLicitatii casa = CasaLicitatii.getInstance();
        for(Produs p:casa.produseActuale)
            if(p.id == idProdus) {
                casa.produseActuale.remove(p);
                break;
            }
    }

    //metoda in care vom calcula comisionul aplicat clientului in functie de istoric
    double calculeazaComision(Client client, double suma) {
        if(client instanceof PersoanaFizica) {
            if (client.nrParticipari <= 5)
                return (20 * suma) / 100;
            else
                return (15 * suma) / 100;
        }
        else{
            if(client.nrParticipari <= 25)
                return (suma*25)/100;
            else
                return (suma*10)/100;
        }

    }



    void liciteazaPentruClient(Licitatie licitatie, double suma, int id) {
        for (Map.Entry mapElement : licitatie.clientMap.entrySet()) {
            Client client = (Client) mapElement.getKey();
            if (client.id == id)
                mapElement.setValue(suma);
        }//brokerului i-a fost transmisa de catre client suma pe care acesta vrea sa o liciteaze
    }   //si va pune in map-ul asociat licitatiei suma aceasta

    @Override
    public void update(Licitatie licitatie, int round) {
        for(Map.Entry mapElement:this.lista_Clienti.entrySet()) {
            if ((Integer)mapElement.getKey() == licitatie.id) {
                CopyOnWriteArrayList<Client> list = (CopyOnWriteArrayList)mapElement.getValue();
                for(Client client:list) {
                    if(client.bugetLicitatii.get(licitatie.id).getKey().nume.equals(this.nume))
                        client.update(licitatie, round);
                }//fiecare broker isi anunta clientii de faptul ca a inceput
            }
        }


    }
}
