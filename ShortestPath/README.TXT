Niculaie Ionut-Lucian, 325 CB

Pentru inceput, am definit clasa abstracta vehicul,care are ca parametri cost si gabarit si care are 
subclasele abstracte: Moped, Autoutilitar si Autovehicul.
Acestea 3 din urma au in constructorul cu parametri gabaritul fixat deoarece orice clasa care extinde una 
dintre aceste 3 clase va avea parametrul gabarit identic(De exemplu Bicicleta si Motocicleta au ambele gabarit 1).

In clasa Harta avem pentru fiecare nod un LinkedList care contin obiecte de tip Strada. Clasa interna de tip Strada are
ca parametri sursa, destinatia, costul si un cost de ambuteiaje asociat fiecarei strazi. Initial este 0 si va creste
pe parcursul aparitiilor evenimentelor pe strada.

Pentru a gasi cel mai scurt drum avem metoda drive, in care am implementat algoritmul Dijkstra folosind o coada
de prioritati, care are ca elemente obiecte de tip Pereche, o clasa care are ca parametri un cost si un punct,
fiecare element din coada avand costul minim pana la un punct, si numarul acelui punct. Se vor scoate progresiv
elemente din coada si se vor inlocui costurile minime daca se gasesc. Se va scrie in fisier cu ajutorul unui
PrintWriter.

In main citim numarul de puncte, cream harta si apoi, succesiv, adaugam strazile. Mai departe avem comenzile de drive
si aparitiile ambuteiajelor pe parcurs, pe care le adaugam cu metoda addRestriction.