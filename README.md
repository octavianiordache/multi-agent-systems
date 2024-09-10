# Sisteme Multi-agent

## Laborator 2.

### Obiectivele laboratorului
-	Crearea dinamică a agenților
-	Ciclul de viață al agentului
-	Comportamente



### Crearea dinamică a agenților
Pentru crearea dinamică a unui agent, configurația de rulare nu mai este necesară; agenții pot fi creați și lansați direct din cod.

```java
public static void main(String[] args) {
        
    jade.core.Runtime rt = jade.core.Runtime.instance();
    Profile p = new ProfileImpl();
    AgentContainer mc = rt.createMainContainer(p);
    
    try {
        AgentController rma = mc.createNewAgent("rma", "jade.tools.rma.rma", null);
        rma.start();
            
        AgentController ac = mc.createNewAgent("Ion","lab2.FirstAgent");
        System.out.println(ac.getState()+"!");
        ac.start();
            
        mc.createNewAgent("Ana","lab2.SecondAgent", null).start();
        mc.createNewAgent("Petru", "lab2.SecondAgent", new Object[] {Integer.valueOf(1)}).start();
            
        System.out.println(ac.getState()+"!");
            
    } catch (StaleProxyException e) {
        e.printStackTrace();
    }
}
```


Exemplul de mai sus creează trei agenți în containerul principal (mc). Portul implicit folosit de JADE este 1099, dacă din orice motiv acest port este indisponibil, profilul trebuie definit prin specificarea tuturor argumentelor sale (gazda, portul și id-ul platformei):

```java
Profile p = new ProfileImpl ("192.168.123.147", 1000, "192.168.1.123:1000/JADE");
```

### Ciclul de viață al agentului [1]
Un agent în cadrul platformei JADE își poate asuma una dintre stările prezentate în Figura 1, în conformitate cu specificația FIPA *Agent Platform Life Cycle*.

**Initiated**: Este creată instanța agentului, dar nu a fost înregistrată la Serviciul de management al agenților (AMS). În această stare, agentul nu are nume și nici o adresă și, prin urmare, nu poate interacționa cu alți agenți.

**Active**: O instanță a agentului a fost înregistrată la AMS. Agentul are un nume comun și o adresă corespunzătoare. Prin urmare, poate avea acces la toate caracteristicile JADE.

**Suspended**: Agentul este în prezent oprit. Firul intern este suspendat. Niciun comportament al agentului nu poate fi executat atâta timp cât instanța agentului rămâne suspendată.

**Waiting**: Instanța agentului este blocată și așteaptă ceva. Firele interne ale agentului sunt în stare de adormire și se vor trezi când și dacă o anumită condiție este îndeplinită (în general, sosește un mesaj).

**Deleted**: În această stare, un agent este cu distrus. Firul intern al agentului a încheiat execuția. Aici agentul a fost șters din AMS.

**Transit**: Un agent își asumă această stare atunci când migrează într-un nou mediu (platformă sau container).

![alt text](Figura1.png "Agent Platform Life Cycle")

Figura 1. Agent Platform Life Cycle.

### Comportamentele agenților

Sarcinile atribuite unui agent sunt gestionate prin comportamente. Un comportament este implementat ca obiect al unei clase care extinde *jade.core.behaviours.Behaviour* (ierarhia de comportamente este prezentată în Figura 2). Un agent execută sarcina care este implementată de un obiect *Behaviour* dacă a fost adăugată la coada de comportamente a agentului folosind metoda *addBehaviour()*.

Fiecare clasă de *comportament* trebuie să implementeze 2 metode abstracte: metoda *action()* definește operațiunile care au loc la executarea agenților; metoda *done()* returnează o valoare booleană care indică dacă comportamentul s-a terminat sau nu. Metoda *block()* pune comportamentul curent în așteptare, și agenții trec la următorul comportament din coadă. Metoda *block(dt)* pune comportamentul curent în așteptare pentru o durată specificată.

Când nu există comportamente disponibile pentru execuție, agentul intră în starea de repaus (sleep) pentru a conserva resursele CPU și este trezit când un comportament devine disponibil.

![alt text](Figura2.png "Clase de comportamente ale agenților")

Figura 2. Clase de comportamente ale agenților.

Pentru a defini un comportament, una dintre clasele de bază disponibile trebuie să fie moștenită. Aceste clase definesc mai multe tipuri de comportamente, cum ar fi:

**SimpleBehaviour**: comportamente atomice care implică o singură sarcină. Acestea nu prezinta nicio particularitate sau cazuri speciale, fiind cele mai flexibile comportamente.
OneShotBehavior: comportamente care se execută o singură dată și care nu pot fi blocate. Metoda done() returnează întotdeauna adevărat.

**CyclicBehavior**: comportamente care sunt executate în mod repetat. Metoda *done()* returnează întotdeauna false. În acest caz, metoda *block()* pune comportamentul în așteptare, până când este primit un nou mesaj. Când se întâmplă acest lucru, toate comportamentele sunt activate și metoda lor action() se va executa secvențial. Metoda *block()* method nu oprește execuția, ci face trecerea la următoarea. Dacă metoda *block()* nu este apelată, comportamentele de tip CyclicBehavior vor rula într-o buclă. În general, se recomandă ca toate metodele *action()* să se încheie cu un apel *block()*.

**TickerBehavior**: acest tip de comportament este executat periodic, la intervale de timp specificate, de exemplu, în constructor. Operațiile executate sunt specificate prin suprascrierea metodei *onTick()*.

**Observație!** Fiecare clasă de bază care definește un comportament are câmpul/proprietatea *myAgent* de tipul *Agent*. Când este creat un nou tip de comportament, acesta moștenește și această proprietate. Problema apare atunci când atribuim comportamentul unui agent definit în mod explicit, de exemplu, *PrimulAgent*, care moștenește *Agent*. Deoarece proprietatea *myAgent* a comportamentului este încă de tip *Agent*, comportamentul nu va avea acces la proprietățile specifice clasei *PrimulAgent*, chiar dacă este asociat cu acesta. Prin urmare, câmpul *myAgent* trebuie să fie înlocuit de un câmp cu nume identic din clasa de agent dorită.

## Exerciții laborator

**E1**. Creați un agent, dinamic, care primește un argument și efectuează diferite comportamente în funcție de valoarea argumentului.

```java
public class AlDoileaAgent extends Agent {
    private static final long serialVersionUID = 1L;
    private int activitate=0;

    @Override
    public void setup()
    {
        Object[] args = getArguments();
        if (args != null && args.length > 0)
            activitate = (Integer) args[0];
        
        System.out.println("Salut, ma numesc " + getLocalName() 
        + " si locuiesc in " + this.getContainerController().getName());
        
        if(activitate==1)
            addBehaviour(new MyOneShotBehaviour());
        else if(activitate==2)
             addBehaviour(new MyTickerBehaviour(this,3000));
        else if(activitate==3)
            addBehaviour(new MyWakerBehaviour(this, 5000));
        else if(activitate==4)
            addBehaviour(new MyCyclicBehaviour());
        else
            System.out.println("Agentul " + getAID().getName() + ": nu am primit nicio sarcina!");
    }
    ...
}

```

**E2**. Creați un agent care numără de la 1 la 100, cu o pauză de 0.5 secunde între două numere succesive. Agentul ar trebui să se autodistrugă atunci când și-a terminat sarcina dată. La fiecare incrementare, agentul va afișa în consolă valoarea la care a ajuns printr-un mesaj:

```Agentul [numele agentului]: am numarat pana la [valoarea la care a ajuns]```

**E3**. Creați 4 agenți care să facă aceeași operație ca la problema 2. Setați un interval aleatoriu pentru fiecare agent între 0.5 și 5 secunde pe care agentul îl așteaptă înainte de a trece la următorul număr. Creați agenții în mod dinamic în două containere diferite. Observați cum agenții lucrează în paralel.


**E4**. Creați un agent care simulează un termostat. Agentul primește preferințele, pragurile temperaturii, de ex. 21-23°C, de la utilizator și citește valorile din mediul ambiental (datele pot fi citite dintr-un fișier sau dintr-un vector). În funcție de valoarea citită, agentul poate porni/opri AC sau centrala termică pentru a menține temperatura în pragul specificat.


### Referințe
[1] K.T. Igulu, E.E. Ogheneovo, B.O. Eke. Intelligent Road Emergency Response System using GAIA and JADE. International Journal of Engineering Research & Technology (IJERT). Vol. 8 (4). 2019. CC 4.0.
