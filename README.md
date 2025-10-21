
# Tema 2 POO

#### Manda Stefan-Edurad 322CC

    Avem jocul League of Warriors care implementeaza un joc de aventura bazat pe 
    text si jucat in terminal.
    Acum implementam cerintele noi pt design patterns si interfata grafica.

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