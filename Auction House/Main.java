import java.io.File;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void main(String[] args) {
        CasaLicitatii casaLicitatii = CasaLicitatii.getInstance();
        ProdusFactory fabrica = new ProdusFactory();
        Administrator admin = new Administrator("Gheorghita");
        try {
            File myObj = new File("text10.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {//citim linie cu linie din fisier
                String line = myReader.nextLine();
                String[] line_list = line.split(" ");
                if (line.startsWith("adprodus")) {
                    Produs p = casaLicitatii.parseazaProdus(line_list, fabrica);
                    Thread thread = new Thread(() -> admin.adaugaProdus(p)); //expresie lambda pentru a crea thread-ul
                    thread.start();                                          //in care se adauga un produs in lista
                    casaLicitatii.produse.add(p);
                    Licitatie l = new Licitatie(casaLicitatii.licitatiiActive.size() + 1, p.id,
                            Integer.parseInt(line_list[9]), Integer.parseInt(line_list[8]));
                    casaLicitatii.licitatiiActive.add(l);

                    for(Broker b:casaLicitatii.licitatiiActive.get(0).brokers) {
                        b.lista_Clienti.put(l.id, new CopyOnWriteArrayList<>());
                        l.brokers.add(b);
                        //daca intre timp s-au adaugat brokeri in sistem ii adaugam si pe acestia
                        //licitatiei care s-a creat acum
                    }
                }
                else if(line.startsWith("adclient")) {
                    casaLicitatii.addClient(line_list);
                }
                else if(line.startsWith("adbroker")) {
                    Broker b = new Broker(line_list[1]);
                    for(Licitatie licitatie:casaLicitatii.licitatiiActive)
                        licitatie.brokers.add(b);
                    //adaugam brokerul in toate licitatiile
                    for(int i = 1; i <=CasaLicitatii.getInstance().produse.size(); i++) {
                        b.lista_Clienti.put(i, new CopyOnWriteArrayList<>());
                    //pregatim map-ul de <IdLicitatie, ArrayList<Client>> introducand entry-uri
                    //cu id-urile licitatiilor deja existente
                    }
                }
                else if (line.startsWith("solicitalicitatie")) {
                    casaLicitatii.solicitaAccesLicitatie(line_list);
                }
                else if(line.startsWith("printproduse")) {
                    Thread t1 = new Thread(() -> casaLicitatii.printProduse());
                    t1.start();
                }
            }
            myReader.close();
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }
}
