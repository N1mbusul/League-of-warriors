
# League of Warriors

#### Manda Stefan-Edurad 322CC

    Avem jocul League of Warriors care implementeaza un joc de aventura bazat pe 
    text si jucat in terminal la inceput si acum foloseste libraria grafica swing.

### Clase implementate
    Clasa Account are declarat in interior clasa statica Information ca contine 
    credentialele, nume, tara so jocuri favorite. Pe langa aceasta mai are getere 
    pentru informatie, lista de caractere si nr jocuri jucate care sunt folosite 
    de alte clase.

    Clasa Credentials are email-ul si parola fiecarui user si am facut doar get-uri 
    pt ele.

    Clasa Entity implementeaza interfata Battle si are viata curenta si maxima, mana 
    curenta si maxima cat si rezistentele player-ului si ale inamicului. Functiile de 
    regen mana si viata sunt facute tot aici. Aici e implementata functia de useAbility 
    si dupa modificata in restul claselor. Aici mai am si functiile de dodge si 
    criticalHit care sunt random.  

    Clasa Character care extinde clasa Entity are numele caracterului, nivelul si xp ul 
    lui, tipul de clasa, si caracteristicile lui, cat si abilitatile. Pe langa get-uri 
    si set-uri am adaugat functia de initializare a statusurilor in functie de clasa 
    caracterului. Dupa am facut functia de generare a abilitatilor si cea de basic 
    attack. Am dat override la functia useAbility pentru a implementa damage ul ei. 
    Pe langa asta mai functiile lgate de nivel: levelUp care verifica daca a depasit 
    threshhold ul pentru a da level up si a primi atribute si functia levelUpUndercover 
    care da level la caracter inainte de a putea juca cu el in functie de nivelul pe care 
    il are in accounts.json . Functia receiveDamage e implementata la fiecare subclasa 
    separat. Clasa Warrior este rezistenta la foc, clasa Mage la gheata si clasa Rogue 
    la pamant.

    Clasa Enemy este la fel ca si clasa Character, doar ca nu are atributele strenght, 
    charisma si dexterity. Dar are totusi abilitati ca si player ul dar sunt selectate 
    random in timpul luptei. De asemenea i-am facut o functie sa-i puna nume random la inamic.

    Enum ul CellEntityType are tipul de celule : PLAYER, VOID, ENEMY,SANCTUAR, PORTAL si VISITED.

    Clasa Cell are ca informatii coordonatele x si y ale celulei, un boolean daca este vizitata 
    sau nu si tipEntitate. fiecare are cate un getter si am mai adaugat un setEntityType pt clasa 
    Grid.

    Clasa Grid este cea in care generez harta si fac functii pentru cazurile in care player ul 
    pica pe anumite celule. Mai intai generez harta cu dimensiuni random intre 4x4 si 10x10. 
    Dupa initializez toata harta cu Void si dupa plasez random portalul, sanctuarele si inamicii 
    si ma folosesc de constructorul privat sa inapoiez grid ul. In paralel mai am functia de 
    generat harta hardcodata pt test. Functia placeEntity cauta celule Void pe tabla si le 
    inlocuieste cu tipul de celula cerut si face asta pana sunt toate plasate pe harta. Functia 
    placeCharacter cauta o celula random VOID pe care pune player ul salvand ui pozitia pe harta. 
    La fel functia placeCharacterAt e pentru testele hardcodate. Functia diplayMap arata harta pe 
    care apare doar jucatorul si celulele nevizitate, lasand in urma V de la VISITED. Functia pt 
    harta de test arata unde sunt toate tipurile diferite de celule pt usurinta si testare. 
    Functiile de miscare moveNorth, moveSouth, moveEast si moveWest mai intai testeaza daca este 
    posibil sa mai mearga in acea directie caracterul. Daca nu ii da mesaj ca nu poate, dar in caz 
    contrar face celula curenta cea din directia selectata si lasa celula din urma vizitata. 
    Functia de handleSanctuary regenereaza o parte din viata si mana si nu depaseste viata si mana 
    maxima ale caracterului. Functia handlePortal ofera player-ului xp = nr jocuri jucate * 5, 
    deoarece nu am inteles daca trebuie asta sau nivelul actual al player ului dar ambele s ok i 
    guess. Dupa aceea incrementez nr jocuri jucate, verific daca a dat level up si resetez viata si 
    mana. Dupa sterg pozitia player ului de pe vechea harta, generez si setez o noua harta si dupa 
    pun player ul pe noua harta. Functia de handleBattle reseteaza abilitatile player ului in caz 
    ca a mai fost inainte intr o batalie, dupa initializeaza inamicul. In battle loop player ul are 
    prima alegere si daca selecteaza orice in afara de 1 sau 2 il readuce la acelasi ecran pana 
    selecteaza basic attack sau ability. De asemenea daca i se termina abilitatile, ii dispare 
    optiunea 2, dar daca o apasa ii spune ca nu are abilitati si poate continua doar daca fol 
    basic attack. In ecranul cu abilitati la poate selecta doar numarul unei abilitati, iar daca 
    selecteaza un numar inexistent il mai pune sa aleaga odata. De asemenea am adugat optiunea sa 
    si schimbe optiunea daca nu vrea sa fol o abilitate ci un basic attack daca alege 0. Dupa 
    verific daca a murit inamic, iar daca nu a murit acesta alege random intre 1 si 2 dupa alege 
    random intre abilitati daca asa a picat sau basic attack daca a ramas fara mana sau abilitati. 
    Dupa verific daca a murit player ul si daca da il scot din joc , iar daca nu continua lupta si 
    asfisez statusurile actualizate.

    Clasa Game se foloseste de clasa primita in arhiva de citire a fisierului JSON. Prima data 
    user ul trebuie sa se logheze si nu poate ajunge la joc pana nu face asta. Mai intai trebuie 
    un email valid ca sa poata baga parola si abia dupa ce amandoua sunt corecte, user ul alege 
    un caracter facut deja pe acel cont. Abia dupa poate incepe jocul unde se genereaza o harta 
    si player ul se misca pe aceasta pentru a gasi portalul.

### Design Patterns

    Singleton Pattern:
        Am implementat cv asemanator cu ideea de lazy implementation pentru a 
    restrictiona numarul de instantieri ale clasei Game si pentru a fi sincronizate.
    
    Builder Pattern:
        Am facut buildere pt clasele Account si Information pentru un control mai mare
    asupra procesului de constructie de obiecte noi.

    Factory Pattern:
        Am facut o interfata factory, careia ii dau override pt fiecare clasa pe care o
    am in joc, returnand constructorii claselor. Am adaugat verificari de null pentru 
    associateCharactersWithAccounts si getFactoryBypProfession pt a preveni 
    NullPointerException. In clasa associateCharactersWithAccounts se va popula lista 
    de personaje a fiecarui cont cu instante noi, create prin intermediul fabricilor.

    Visitor Pattern
        Am facut cele 2 interfete necesare : Element si Visitor, astfel incat Entity
    sa implementeze Element si Spell sa implementeze Visitor. Acum useAbility apeleaza
    ability.visit(target), astfel pune responsabilitatea de handle al damage ului si
    si rezistentelor specifice prin metoda visit.

### Interfata jocului

    Clasa GameLogin este doar pentru partea de login, adica verificare user si parola.
    Aici atat user ul cat si parola trebuie scrise de mana(nu merge cu copy-paste). De
    asemenea am adaugat functia de a arata parola pentru o verificare mai usoara cat si
    un buton de resetare. Am ales ca la toate clasele sa implementez modelul singleton
    pentru a fi mai eficient. Astfel, dupa ce login ul este successful, fereastra se va
    inchide si jucatorul va fi dus la urmatoarea fereastra, aceea fiind cea de selectie
    a caracterului.
    
    Clasa GameCharacterSelection este pentru selectia caracterelor fiecarui cont. Precizez
    ca implementarea de factory cu clasele aferente se afla in clasa Game originala,
    deoarece design patetrn uri le au fost implementate inainte de interfata. Fiecare clasa
    va avea o poza random(voi adauga 1-3 poze pt fiecare clasa si voi genera una random din
    ele) cat si statusurile din account.json, cu statusurile actualizate in functie de nivel.
    In acest meniu trebuie selectat un caracter dupa apasat pe confirm. Dupa aceea vei fi
    inclus in joc, iar acesta va incepe.

    Clasa GamePlay este pentru jocul propriu-zis care are controalele player ului pe stanga,
    poza lui in centru si statusurile in dreapta. Butoanele se actualizeaza in functie de
    directia in care poti sa mergi, adica daca esti langa un perete nu poti merge in acea
    directie. Sub acest panel am grid ul cu harta pe care se plimba player ul. Ea e
    initializata sa afiseze UNLNOWN, PLAYER si VISITED. In rest mai am cazurile pt cand 
    player ul ajunge pe anumite spatii ca la jocul anterior. Sanctuarul regenreaza viata si
    mana, portalul reseteaza harta is ofera xp si battle care are clasa separata. Stats uri le
    se actualizeaza dupa fiecare miscare.

    Clasa GameBattle este pentru batalia dintre player si inamic. Acesta in functie de clasa are
    o anumita poza si statusuri proportionale cu viata si mana player ului. Am folosit si aici
    singleton pattern si am mai facut o functie pt resetarea bataliei. De asemenea am clasa
    separata BattleManager pt desfasurarea bataliei propriu-zise. In GameBattle mai am butoanele
    de atac si abilitati, cea din urma ducand la o noua fereastra in care sa selectezi abilitatea.
    Dupa fiecare atac/abilitate, verific daca ambii combatanti sunt in viata. Daca nu, fie player-ul
    primeste viata, mana si xp, fie apare fereastra de EndGame. In clasele BattleManager si AbilitySelect
    folosesc un logArea pentru a afisa ce se intampla in batalie. In rest atacurile si folosirea 
    abilitatilor cu sansele de crit si dodge sunt luate din jocul anterior. In AbilitySelect doar mai
    adaug poze la vraji cat si sa actualizez dupa fiecare folosire lista de abilitati.

    Clasa GameEnd este folosita ca sa afiseze statusurile player ului dupa ce a murit sau dupa ce
    iese din joc. Aici are optiunea sa inchida jocul sau mai incerce odata si il aduce la ecranul
    de login.
