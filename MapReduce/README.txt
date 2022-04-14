---NICULAIE IONUT-LUCIAN 335 CB---

In clasa principala, in main, citesc informatiile citite de la tastatura.
Imi salvez informatiile, apoi incep sa deschid fiecare fisier si voi crea
task-uri trimitand pentru fiecare task path-ul unui fisier, offset-ul
de la care trebuie sa citeasca, dimensiunea fragmentului, dimensiunea
fisierului si dictionarul si lista pe care va trebui sa le completeze
cu dimensiunea cuvintelor si numarul lor, respectiv cele mai lungi cuvinte
in cazul listei.

In task-urile pentru map intai ne vom uita daca dimensiunea de citit este mai
mare decat distanta pana la finalul fisierului. Dupa ce ne-am asigurat de acest
lucru trebuie sa ne uitam daca prima litera din fragmentul citit nu apartine
cumva altui cumvant inceput anterior. Vom ajusta aceasta parte. Apoi, trebuie
sa ne asiguram ca in finalul fragmentului nu am inceput cuvinte pe care nu le-am
terminat. In cazul in care trebuie sa mai adaugam octeti, ii vom adauga.
Dupa ce am obtinut fragmentul ajustat si complet ii vom face split dupa simbolurile
mentionate in pdf-ul temei.
Folosindu-ne de lista de cuvinte rezultata vom folosi aceste rezultate pentru a popula
lista cu cele mai lungi cuvinte pe care le identificam cu usurinta, precum si pentru a popula
dictionarul cu dimensiunea cuvintelor si numarul lor. Lista de cuvinte cele mai lungi este
populata concurent de catre thread-urile asociate path-ului fisierului respectiv.

Odata obtinute aceste rezultate, vom crea noi task-uri pentru a prelucra aceste informatii.
Ne vom crea pentru usurinta o lista cu rank-urile fisierelor care va fi populata concurent
de catre task-urile care se ocupa de reduce, un dictionar care ne asociaza un rank cu un path,
un dictionar care ne asociaza un path cu dimensiunea celui mai lung cuvant din fisier si ultimul
care asociaza un path cu numarul de cuvinte cele mai lungi.

In task-urile de reduce unim dictionarele rezultate la etapa de Map pentru a avea informatiile
la un loc. Ne uitam care este dimensiunea celui mai lung cuvant cu ajutorul listei cu cele
mai lungi cuvinte aflam si cate cuvinte de acest fel exista folosindu-ne de dictionar.

Mai departe calculam rank-ul conform formulei si populam dictionarele auxiliare cu informatiile
rezultate.

Dupa ce avem toate rezultatele in main sortam descrescator lista de rank-uri si ne extragem
informatiile pe care le vom afisa in fisier.

