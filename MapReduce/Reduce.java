import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Reduce implements Runnable {
    //lista de dictionare corespunzatoare unui fisier
    ArrayList<HashMap<Integer, Integer>> dictionaryList;
    String path;
    //dictionarul in care vom uni lista de dictionare de mai sus
    HashMap<Integer, Integer> dictionary;
    //lista in care vom adauga rank-ul fisierului
    List<Float> rankList;
    //3 dictionare cu informatiile rezultate din task
    ConcurrentHashMap<Float, String> rankToPath;
    ConcurrentHashMap<String, Integer> pathToLength;
    ConcurrentHashMap<String, Integer> pathToNumber;
    int[] fibo;
    List<String> longestWords;

    public Reduce(ArrayList<HashMap<Integer, Integer>> dictionaryList,
                  String path, HashMap<Integer, Integer> dictionary,
                  ConcurrentHashMap<Float, String> rankToPath, ConcurrentHashMap<String,
            Integer> pathToLength, ConcurrentHashMap<String, Integer> pathToNumber,
                  List<Float> rankList, int[] fibo, List<String> longestWords) {
        this.dictionaryList = dictionaryList;
        this.path = path;
        this.dictionary = dictionary;
        this.rankToPath = rankToPath;
        this.pathToLength = pathToLength;
        this.pathToNumber = pathToNumber;
        this.rankList = rankList;
        this.fibo = fibo;
        this.longestWords = longestWords;
    }

    @Override
    public void run() {
        //unim lista de dictionare in unul singur
        for (int j = 0; j < dictionaryList.size(); j++) {
            HashMap<Integer, Integer> currentDictionary = dictionaryList.get(j);
            for (Integer key : currentDictionary.keySet()) {

                if (!dictionary.containsKey(key)) {
                    dictionary.put(key, currentDictionary.get(key));
                } else {
                    dictionary.put(key, currentDictionary.get(key) + dictionary.get(key));
                }
            }
        }

        int maxKey = 0;
        int maxValue = 0;
        //ne folosim de lista longestWords pt a obtine dimensiunea
        //celui mai lung cuvant
        for(int i = 0; i < longestWords.size(); i++) {
            if(longestWords.get(i).length() > maxKey) {
                maxKey = longestWords.get(i).length();
            }
        }
        //aflam cate cuvinte de acea dimensiune exista
        maxValue = dictionary.get(maxKey);

        //completam dictionarele cu informatiile rezultate
        pathToLength.putIfAbsent(path, maxKey);
        pathToNumber.putIfAbsent(path, maxValue);
        float rank = 0;
        float totalWords = 0;
        //calculam rank-ul
        for(Integer key : dictionary.keySet()) {
            totalWords += dictionary.get(key);
            rank += fibo[key] * dictionary.get(key);
        }
        rank = rank/totalWords;
        int rank2 = (int) (rank*1000);
        if(rank2 % 10 >= 5) {
            rank2 = rank2/10 + 1;
            rank = (float)rank2 / 100;
        }
        else {
            rank2 = rank2/10;
            rank = (float)rank2 / 100;
        }
        rankList.add(rank);
        rankToPath.putIfAbsent(rank, path);
    }
}