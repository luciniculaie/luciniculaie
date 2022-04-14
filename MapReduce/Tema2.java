import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class Tema2 {
    static void fibFill(int[] fibo) {
        fibo[0] = 1;
        fibo[1] = 1;
        for (int i = 2; i < fibo.length; i++) {
            fibo[i] = fibo[i - 1] + fibo[i - 2];
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ExecutorService tpe = Executors.newFixedThreadPool(Integer.parseInt(args[0]));
        BufferedReader br = new BufferedReader(new FileReader(args[1]));
        long total_dimension = Integer.parseInt(br.readLine());
        int documents_number = Integer.parseInt(br.readLine());
        //arraylist in care vom tine listele de dictionare ale fiecarui fisier
        ArrayList<ArrayList<HashMap<Integer, Integer>>> dictionaries = new ArrayList<>();
        //arraylist in care vom tine listele de cele mai lungi cuvinte ale fiecarui fisier
        ArrayList<List<String>> longestWords = new ArrayList<>();
        //lista cailor
        List<String> pathList = new ArrayList<>();
        //lista auxiliara ce va fi populata de mai multe taskuri in acelasi timp, pentru un anumit fisier
        List<String> words;

        for(int i = 0; i < documents_number; i++) {
            dictionaries.add(new ArrayList<>());
        }

        for(int i = 0; i < documents_number; i++) {
            HashMap<Integer, Integer> dictionary = new HashMap<>();
            words = Collections.synchronizedList(new ArrayList<>());
            longestWords.add(words);
            dictionaries.get(i).add(dictionary);
            String path = br.readLine();
            pathList.add(path);
            File file = new File(path);
            int offset = 0;

            Map t = new Map(path, offset, (new File(path)).length(), total_dimension, dictionary, words);
            tpe.submit(t);

            while(offset < file.length()) {
                //dictionarul va contine rezultatul task-ului, cate litere are fiecare cuvant
                dictionary = new HashMap<>();
                dictionaries.get(i).add(dictionary);
                offset += total_dimension;

                Map t2 = new Map(path, offset, (new File(path)).length(), total_dimension, dictionary, words);
                tpe.submit(t2);
            }

        }

        int flag = 1;
        tpe.shutdown();
        while(flag != 2) {
            if(tpe.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS))
                flag = 2;
        }

        ExecutorService tpe2 = Executors.newFixedThreadPool(Integer.parseInt(args[0]));
        //dictionar cu cheie un rank si valoare un path al unui fisier
        ConcurrentHashMap<Float, String> rankToPath = new ConcurrentHashMap<>();
        //lista rank-urilor
        List<Float> ranks = Collections.synchronizedList(new ArrayList<>());
        //dictionar cu cheie path-ul unui fisier si valoare numarul de litere ale
        //celui mai lung cuvant
        ConcurrentHashMap<String, Integer> pathToLength = new ConcurrentHashMap<>();
        //dictionar cu cheie path-ul unui fisier si valoare numarul de cuvinte care
        //au cele mai multe litere
        ConcurrentHashMap<String, Integer> pathToNumber = new ConcurrentHashMap<>();

        int[] fib = new int[100];
        fibFill(fib);

        for(int i = 0; i < dictionaries.size(); i++) {
            HashMap<Integer, Integer> finalDictionary = new HashMap<>();
            Reduce t = new Reduce(dictionaries.get(i), pathList.get(i), finalDictionary, rankToPath,
                            pathToLength, pathToNumber, ranks, fib, longestWords.get(i));
            tpe2.submit(t);
        }

        flag = 1;
        tpe2.shutdown();
        while(flag != 2) {
            if(tpe2.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS))
                flag = 2;
        }

        ranks.sort(Collections.reverseOrder());
        BufferedWriter bw = new BufferedWriter(new FileWriter(args[2]));
        for(float rank : ranks) {
            String[] pathSp = rankToPath.get(rank).split("/");
            String path = pathSp[pathSp.length -1];
            bw.write("" + path + "," + rank + "," + pathToLength.get(rankToPath.get(rank))
                    + "," + pathToNumber.get(rankToPath.get(rank)) + "\n");
        }
        bw.close();
    }
}


