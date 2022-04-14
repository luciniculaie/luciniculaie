
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;

/**
 * Clasa unde se realizeaza comenzile.
 */
public class Main{
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception{
        String line;
        int nrStrazi;
        int nrPuncte;
        try {
            String filePath = new File("").getAbsolutePath();
            BufferedReader br = new BufferedReader(new FileReader(filePath + File.separator +  "map.in"));
            line = br.readLine();
            StringTokenizer tok = new StringTokenizer(line);
            nrStrazi = Integer.parseInt((String)tok.nextElement());
            nrPuncte = Integer.parseInt((String)tok.nextElement());
            Harta harta = new Harta(nrPuncte);

            for(int i = 0; i < nrStrazi; i++) {
                line = br.readLine();
                tok = new StringTokenizer(line);
                int sursa = Integer.parseInt(((String)tok.nextElement()).substring(1));
                int destinatie = Integer.parseInt(((String)tok.nextElement()).substring(1));
                int cost = Integer.parseInt((String)tok.nextElement());
                int limita_gabarit = Integer.parseInt((String)tok.nextElement());
                harta.addStreet(sursa, destinatie, cost, limita_gabarit);
            }

            while((line = br.readLine()) != null) {
                tok = new StringTokenizer(line);
                String tip = (String)tok.nextElement();
                if(((Character)tip.charAt(0)).equals('a') || ((Character)tip.charAt(0)).equals('t')
                        || ((Character)(tip.charAt(0))).equals('b')) {
                    int sursa = Integer.parseInt(((String)tok.nextElement()).substring(1));
                    int destinatie = Integer.parseInt(((String)tok.nextElement()).substring(1));
                    int cost = Integer.parseInt((String)tok.nextElement());
                    Ambuteiaj ambuteiaj = new Ambuteiaj(sursa,destinatie,tip,cost);
                    harta.addRestriction(ambuteiaj);
                }
                if(((Character)tip.charAt(0)).equals('d')) {
                    Character c = ((String)tok.nextElement()).charAt(0);
                    int sursa = Integer.parseInt(((String)tok.nextElement()).substring(1));
                    int destinatie = Integer.parseInt(((String)tok.nextElement()).substring(1));
                    if(c.equals('a')) {
                        Autoturism auto = new Autoturism();
                        harta.drive(sursa, destinatie, auto);
                    }
                    if(c.equals('b')) {
                        Bicicleta bicicleta = new Bicicleta();
                        harta.drive(sursa, destinatie, bicicleta);
                    }
                    if(c.equals('m')) {
                        Motocicleta motocicleta = new Motocicleta();
                        harta.drive(sursa, destinatie, motocicleta);
                    }
                    if(c.equals('c')) {
                        Camion camion = new Camion();
                        harta.drive(sursa, destinatie, camion);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
