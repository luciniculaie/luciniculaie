In primul rand, am folosit pentru clasa Product design pattern-ul de builder si pentru
clasa Store design pattern-ul Singleton. Nu am adaugat nici un design pattern suplimentar.

In clasa Store am mai adaugat un ArrayList de Currency-uri care semnifica toate currency-urile
care au fost adaugate pe Store si un ArrayList de Discount-uri cu functionalitate identica.
De asemenea, am folosit aceste colectii in loc de vectori.

Tin sa mentionez ca am modificat semnatura metodei addManufacturer si am eliminat exceptia
DuplicateManufacturerException. In consecinta, metoda addManufacturer va returna un obiect
de tip Manufacturer(Manufacturer-ul nou creat sau cel deja existent daca cumva exista deja 
unul in ArrayList-ul de manufactureri din Store. Am mai adaugat exceptia ProductNotFound
atunci cand la comanda calculatetotal se cauta un produs si nu se gaseste, sau cand dorim
sa afisam informatii despre un produs care nu exista.

Am mai creat si metodele applyPercentageDiscount si applyFixedDiscount care aplica discount-ul
folosind valoarea primita ca parametru. Se va arunca o exceptie daca nu se gaseste discount-ul
in ArrayList-ul de Discount-uri. applyFixedDiscount arunca si exceptia NegativePrice daca cumva
discount-ul care urmeaza sa fie aplicat ar duce produsul la un pret negativ.Am creat si o 
functie care va returna pretul unui produs cu tot cu aplicarea discount-ului de pe el daca exista
un discount. Metoda convertPrice(string price, Currency currency) va returna valoarea unui
produs in moneda actuala de pe store. Am creat o functie calculatetotal care va calcula pretul
produselor cu id-urile specificate. Daca produsul nu se gaseste se va arunca ProductNotFoundException.
Am modificat ce intoarce metoda readcsv(acum e void). Modificarea a fost facuta deoarece nu am nevoie
de acel ArrayList de produse.

Pentru citire la input am folosit un BufferedReader. Proiectul este creat cu Maven pentru a putea
folosi libraria Apache Commons, cu ajutorul careia citim din csv la comanda loadcsv. JAR-ul l-am
rulat din linia de comanda cu java -jar pathToFile.
