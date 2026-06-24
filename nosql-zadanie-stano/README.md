# nosql-zadanie-stano

Projekt z NoSQL (MongoDB).

## Príprava prostredia

### 1. Java setup

Maven potrebuje `JAVA_HOME` aby ukazoval na JDK (nielen JRE).

**Windows:**
```powershell
setx JAVA_HOME "C:\Program Files\Java\jdk-22"
```

Otvor **nový terminal** a over:
```powershell
echo $env:JAVA_HOME
```

### 2. Build projektu

Z koreňového adresára:

```powershell
cd c:\Users\stani\Downloads\nosql-zadanie-stano\nosql-zadanie-stano
.\mvnw.cmd clean install
```

To stiahne MongoDB modul a všetky závislosti. Maven wrapper (`mvnw.cmd`) sa automaticky stiahne.

## Spustenie uloh - Interaktívny režim (Najjednoduchšie!)

Odporúčaný a najjednoduší spôsob je **interaktívny režim**, kde si v konzole vyberiete číslo úlohy a zadáte parametre.

### Prvý raz (s build):

```powershell
cd c:\Users\stani\Downloads\nosql-zadanie-stano\nosql-zadanie-stano
.\mvnw.cmd -pl mongodb-repository spring-boot:run -Dtask=interactive
```

### Nabudúce (bez build, rýchlejšie):

```powershell
cd c:\Users\stani\Downloads\nosql-zadanie-stano\nosql-zadanie-stano\mongodb-repository
java -Dtask=interactive -jar target\mongodb-repository-0.0.1-SNAPSHOT.jar
```

### Ako sa to používa:

Po spustení sa zobrazí **MENU**:

```
===== INTERAKTIVNY REZIM =====
Moznost spustat ulohy podle vlastneho vyberu.
Zmazat a znovu vlozit data? (y/n): y
OK - data su pripravene.

MENU ULOH:
 1) Uloha 1: Projekcia - meno/priezvisko podla akademickeho titulu
 2) Uloha 2: Studenti v danom roku a programu
 3) Uloha 3: Test indexu (porovnanie casov)
 4) Uloha 4: Pocty studentov podla rokov a programov
 q) Ukoncit

Zadaj cislo ulohy (1-4) alebo 'q' na ukoncenie:
```

**Príklad 1 - Hľadaj študentov s titulom:**
```
Zadaj cislo ulohy (1-4) alebo 'q' na ukoncenie: 1
Zadaj akademicky titul (napr Mgr., Bc., Dr.): Mgr.

Studenti s titulom Mgr.:
  Peter Novak
  Marek Hurban

MENU ULOH:
 ...
```

**Príklad 2 - Hľadaj študentov v roku a programe:**
```
Zadaj cislo ulohy (1-4) alebo 'q' na ukoncenie: 2
Zadaj akademicky rok (napr 1998): 1998
Zadaj skratku studijneho programu (napr MCH, INF): MCH

Studenti v roku 1998 na programe MCH:
  Peter Novak
  Marek Hurban
```

**Príklad 3 - Počty študentov:**
```
Zadaj cislo ulohy (1-4) alebo 'q' na ukoncenie: 4

Pocty studentov podla rokov a studijnych programov:
Rok        Program              Pocet
----------------------------------------
1998       MCH                  2
1999       INF                  1
1999       MCH                  2
...
```

Napísaním `q` sa ukončí program. Menu sa opakovane zobrazuje, takže môžeš vyskúšať všetky úlohy bez reštartu.

## Linux / Bash

Rovnaké príkazy, ale s `./` namiesto `.\`:

```bash
cd /path/to/nosql-zadanie-stano/nosql-zadanie-stano
./mvnw -pl mongodb-repository spring-boot:run -Dtask=interactive
```

Alebo s jar:
```bash
cd mongodb-repository
./mvnw -DskipTests clean package
java -Dtask=interactive -jar target/mongodb-repository-0.0.1-SNAPSHOT.jar
```

## Bežné príkazy

```powershell
# build bez testov
.\mvnw.cmd -DskipTests clean install

# spustenie testov
.\mvnw.cmd test

# len compile
.\mvnw.cmd clean compile
```

## Poznámky

- Maven wrapper (`mvnw.cmd`, `mvnw`) a `.mvn/` folder sú už v repozitári - nie je potrebný globálny Maven.
- Aplikácia sa pripája na MongoDB na `mongodb://localhost:27017/nosql2026`.
- Ak MySQL nie je dostupný (obvyklý prípad), aplikácia používa vzorové dáta.
