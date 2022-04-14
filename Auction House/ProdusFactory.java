public class ProdusFactory {
    public static enum TipProdus {
        Tablou, Mobila, Bijuterie
    }

    public Produs creazaProdus(TipProdus p, int id , String nume, double pretVanzare, double pretMinim,
                               int an, String argument1, String argument2) {
        switch(p) {
            //in functie de tipul produsului care va urma sa fie creat avem 3 cazuri separate
            //pentru fiecare caz in parte parsam argumentele specifice fiecarui produs si folosim
            //builder-ul creat pentru produse
            case Tablou: {
                String numePictor = argument1;
                Culori culoare;
                if (argument2.charAt(0) == 'U')
                    culoare = Culori.Ulei;
                else if (argument2.charAt(0) == 'T')
                    culoare = Culori.Tempera;
                else
                    culoare = Culori.Acrilic;
                return new TablouBuilder()
                        .setId(id)
                        .setAn(an)
                        .setNume(nume)
                        .setPretMinim(pretMinim)
                        .setPretVanzare(pretVanzare)
                        .setCuloare(culoare)
                        .setNumePictor(numePictor)
                        .build();
            }
            case Mobila: {
                String tip = argument1;
                String material = argument2;
                return new MobilaBuilder()
                        .setId(id)
                        .setAn(an)
                        .setNume(nume)
                        .setPretMinim(pretMinim)
                        .setPretVanzare(pretVanzare)
                        .setTip(tip)
                        .setMaterial(material)
                        .build();
            }
            case Bijuterie: {
                String material = argument1;
                boolean piatraPretioasa;
                if (argument2.charAt(0) == 'T')
                    piatraPretioasa = true;
                else
                    piatraPretioasa = false;
                return new BijuterieBuilder()
                        .setId(id)
                        .setAn(an)
                        .setNume(nume)
                        .setPretMinim(pretMinim)
                        .setPretVanzare(pretVanzare)
                        .setPiatraPretioasa(piatraPretioasa)
                        .setMaterial(material)
                        .build();
            }
        }
        return null;
    }
}
