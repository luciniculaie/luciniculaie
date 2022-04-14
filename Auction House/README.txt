Niculaie Ionut-Lucian
325CB

Voi incepe prin a enumera design pattern-urile folosite in aplicatie:
In primul rand am folosit design pattern-ul Singleton pentru Casa
de Licitatii, deoarece este unica in aplicatie. In al doilea rand,
am folosit design pattern-ul Factory pentru Produse deoarece exista
produse de 3 feluri si astfel putem simplifica procesul de creare
al produselor. De asemenea, am ales sa folosesc design pattern-ul
builder pentru a crea Clientii din aplicatie, acestia avand 
suficient de multe argumente. Cu design pattern-ul builder, crearea
lor este mai human-readable pentru cine va lucra in aplicatie.
Ultimul design pattern folosit este Observer, cu ajutorul careia
am creat o comunicare intre Broker si Client. In momentul in care
brokerii sunt notificati ca a inceput un nou pas de licitatie,
acestia vor informa clientii despre detaliile licitatiei, iar clientii
isi vor exprima suma pe care vor sau pot sa o liciteze in continuare.

Ceea ce tine de multithreading a fost implementat in felul urmator:
Am creat niste thread-uri in care administratorul adauga produse in
sistem, broker-ul le elimina cand au fost vandute, iar clientul
solicita sa vada lista de produse. Toate acestea au fost aplicate
pe o lista sincronizata.
De asemenea, pentru fiecare Licitatia este Runnable, ceea ce inseamna
ca de fiecare data cand se atinge numarul minim de participanti va incepe
un nou thread. Licitatiile se vor desfasura in paralel.

Cu privire la genericitate, am folosit Liste si Map-uri (ArrayList,HashMap).

In Licitatie avem niste elemente in plus fata de cerinta: avem in primul
rand o lista de brokeri care participa la licitatie, avem un map care constituie
cat a licitat fiecare client la fiecare pas al licitatiei(clientMap) si un map
"stareParticipare" in care retin daca clientul respectiv mai are bani de licitat
sau nu. Daca ramane o singura persoana care mai poate sa liciteze va fi declarat
din oficiu castigator.

In clasa Client avem un Map in care retinem id-ul licitatiei si acesta are asociat
Broker-ul pe care-l are in acea licitatie si ce buget are pentru acea licitatie.

In main, mi-am creat niste comenzi pentru a fi mai usor de inteles procesul:
adclient - adauga un client in sistem
adprodus - adauga un produs in sistem
adbroker - adauga un broker in sistem
solicitalicitatie - clientul solicita accesul la licitatie pentru un anumit produs
printproduse - clientul poate vedea produsele actuale de vanzare 