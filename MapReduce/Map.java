import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Map implements Runnable {
    private final String path;
    int offset;
    long fileLength;
    long fragmentLength;
    HashMap<Integer, Integer> dictionary;
    List<String> longestWords;

    public Map(String path, int offset, long fileLength, long fragmentLength, HashMap<Integer, Integer> dictionary,
               List<String> longestWords) {
        this.path = path;
        this.offset = offset;
        this.fileLength = fileLength;
        this.fragmentLength = fragmentLength;
        this.dictionary = dictionary;
        this.longestWords = longestWords;
    }

    @Override
    public void run() {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int dimension;
        //ne uitam daca pana la finalul fisierului sunt mai putin de
        //fragmentLength octeti
        dimension = (int) fragmentLength;
        if (fileLength - offset < fragmentLength) {
            dimension = (int) (fileLength - offset);
        }

        char[] buf = new char[(int) (fragmentLength * 2)];

        try {
            //daca nu ne aflam la inceputul fisierului ne mutam la offset-ul dorit
            if (offset != 0)
                br.skip(offset - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int flag = 0;
        char c1, c2;
        int begin = 0;
        //verificam daca e nevoie sa stergem caractere de la inceputul fragmentului
        //(daca prima litera face parte dintr-un cuvant inceput anterior)
        if (offset != 0) {
            try {
                c1 = (char) br.read();
                c2 = (char) br.read();
                if ((Character.isLetter(c1) || Character.isDigit(c1)) && (Character.isLetter(c2) || Character.isDigit(c2))) {
                    flag = 1;
                    dimension--;
                } else {
                    buf[0] = c2;
                    begin = 1;
                    dimension--;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //daca ne aflam la inceputul fisierului citim in mod normal toti octetii
        if (offset == 0) {
            try {
                br.read(buf, 0, dimension);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //ne uitam daca si urmatoarele litere in afara de prima fac parte din acelasi cuvant
            //in caz afirmativ le vom elimina si pe acestea
            while (flag == 1) {
                try {
                    char ch = (char) br.read();
                    if (Character.isLetter(ch) || Character.isDigit(ch)) {
                        dimension--;
                    } else {
                        dimension--;
                        begin = 1;
                        buf[0] = ch;
                        flag = 0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //daca descoperim ca am epuizat tot fragmentul si nu mai avem ce citi iesim din task
            if (dimension <= 0) return;
            try {
                br.read(buf, begin, dimension);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //ne uitam daca ultimul octet din fragment este litera sau nu
        if (Character.isDigit(buf[begin + dimension - 1]) || Character.isLetter(buf[begin + dimension - 1]))
            flag = 1;
        else {
            flag = 0;
        }
        int add = 0;
        //daca ultimul octet era litera ne uitam in continuare sa vedem daca putem continua cuvantul
        while (flag == 1) {
            char ch = 0;
            try {
                ch = (char) br.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if ((Character.isLetter(ch)) || (Character.isDigit(ch))) {
                buf[begin + dimension + add] = ch;
                add++;
            } else {
                flag = 0;
            }
        }

        char[] theString = new char[begin + dimension + add];
        String aux = new String(buf);
        for (int i = 0; i < begin + dimension + add; i++) {
            theString[i] = aux.charAt(i);
        }
        String str1 = new String(theString);
        //facem split la fragmentul final ajustat
        String[] splits = str1.split("[;:\\s/?`\\\\.,\\r\\n\\t><‘\\[\\]{}()_!@#$%ˆ& '’=*”+-]+");

        int maxLen = 0;
        for (int i = 0; i < splits.length; i++) {
            if (splits[i].length() > maxLen) {
                maxLen = splits[i].length();
            }
        }
        //adaugam in lista de cele mai lungi cuvinte
        for (int i = 0; i < splits.length; i++) {
            if (splits[i].length() == maxLen) {
                longestWords.add(splits[i]);
            }
        }

        //completam dictionarul care contine cuvintele si numarul de litere corespunzator
        for (int i = 0; i < splits.length; i++) {
            int len = 0;
            for (int j = 0; j < splits[i].length(); j++) {
                len = splits[i].length();
            }
            if (len != 0) {
                if (dictionary.containsKey(len)) {
                    int a = dictionary.get(len);
                    dictionary.put(len, a + 1);
                } else {
                    dictionary.put(len, 1);
                }
            }
        }
    }
}