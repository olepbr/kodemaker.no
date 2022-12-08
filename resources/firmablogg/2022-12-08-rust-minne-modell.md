:title Hvordan hånterer man minne i Rust
:author andre
:tech [:programming]
:published 2022-12-07

:blurb

Rust er det mest populære programmeringsspråket i følge Stack Overflow, men hva er det som er så spesielt med det? 
Det som først fikk min oppmerksomhet var hvordan man håndterer minne.

:body

Rust er det mest populære programmeringsspråket i følge Stack Overflow, men hva er det som er så spesielt med det? 
Det som først fikk min oppmerksomhet var hvordan man håndterer minne. For å finne ut av hvorfor Rust sin håndtering er så spesiell, la oss først se på noen av de andre måtene dette gjøres.

## Hva mener vi med minnehåndtering
Når man i et program oppretter en variabel, så må det settes av nok minne for å holde på dataene variablene skal peke på. Siden minne er en begrenset ressurs, så ønsker man frigi ubrukt minne så snart som mulig  etter man ikke har behov for det lenger, for å kunne gjenbruke det.

Minne deles opp i to typer, Stack og Heap. Disse har forskjellige karakteristikker og bruksområder, så la oss ta en rask titt på de:

### Stack 

Man sier ikke å allokere og deallokere minne på Stacken, siden vi vet hvor i minnet variablene skal plasseres, samt størrelsen på minnet som trengs. Vi sier i stedet å "pushe" og "poppe" på stakken.

* Variabler blir liggende i samme rekkefølge som de blir "pushet" på stakken.
* Størrelsen på dataene som skal lagres på stacken er kjent ved kompileringstidspunktet. 
* Når en funksjon kalles, så vil argumenter som er sendt til funksjonen, samt funksjonens lokale variabler blir dyttet på stakken. 
* Når funksjonen er ferdig utført, så vil verdiene blir fjernet fra stakken i rekkefølgen sist inn, først ut.
* Tilgjengelig stackminne er (mye) mindre enn for heap.
* Data som er lagret på stakken kan sies å være "sikrere", siden data laget her kun kan aksesseres av tråden som utfører funksjonen.
* Det er raskt å pushe og poppe.


### Heap

* Størrelsen på minnet som må settes av er ikke kjent ved kompilering.
* Det avsatte minneområdet blir pekt på av en variabel som så pushes på stacken.
* Minne som er avsatt på heapen kan refereres til av flere pekere samtidig, og er synlig for alle tråder.
* Glemmer man å deallokere minnet etter bruk så vil det oppstå en minnelekasje.
* Allokering og deallokering av minne på heapen er mye tregere enn minne avsatt på stacken, siden man må først finne en stor nok minneblokk som kan holde på det vi ønsker å lagre der, og så man må "bokføre" hvor minnet er allokert for senere å kunne frigjøre det.

Det å holde orden på minne i ett program krever at man holder tungen rett i munnen for å ikke innføre minnelekasjer. Man må også passe på at en tråd ikke leser fra minnet samtidig som en annen skriver til det. I tillegg så må man passe på at man ikke frigjør minne som er pekt på av en annen variabler enn den man selv holder på. Det er flere måter man kan angripe denne utfordringer på. La oss se på noen eksempler:


## Manuell minnehåntering
La oss se på hvordan man håndterer minne i språket C.
Det opp til programmereren å allokere og deallokere minne som programmet har behov for.


```c
#include <stdlib.h>

void func()
{
    int *intPtr = (int *) malloc(sizeof(int));  // 1 Alloker en minneblokk som har plass til en int
    // Gjør noe med dataene pekeren peker på...
    free(intPtr);                               // 2 Frigi minne
    return;                                     // 3 returner fra funksjonen
}
```

I dette eksempelet så allokerer vi ett minneområde som er stort nok til å holde en `int`. Vi oppretter en variabel `intPtr` som peker til minnet vi satt av ved kallet til `malloc(sizeof(int))`, som blir pushet på stakken. Siden variabelen ligger på stacken, så vil den bli frigitt når funksjonen er ferdig. Minneområdet `intPtr` peker på, ble ikke lagt på stacken, men på heapen. For å frigi minnet allokert tidligere, så kaller vi `free(intPtr)` før vi returnerer fra funksjonen. Funksjonen `free` frigir minne som navnet indikerer. 

Dersom vi ikke husker å gjøre dette, så ville programmet lekke 4 bytes (på en 32-bit plattform) for hver gang funksjonen hadde blitt kalt. Det lekkede minnet hadde blitt frigitt når programmet avsluttes, men ikke før det. Fire bytes er ikke mye minne, men dersom den aktuelle funksjonen blir kalt veldig mange ganger, så kunne den totale minnelekkasjen blitt stor. 

Når ett program har en minnelekkasje så vil man typisk først oppleve at programmet blir tregere og tregere siden allokering av minne blir mer og mer tidkrevende.Grunnen til der er at man må lete mer og kanskje defragmentere minnet for å finne etter ett sammenhengende område som er stort nok. Finner man ikke det, så har man ikke nok minne, og da vil programmer krasje.

Siden ett allokert minneområde kan refereres til av flere pekere samtidig, så må man være påpasselig slik at vi ikke deallokerer ett minneområde som kan være i bruk av andre. Gjør man det, så vil applikasjonen også kunne krasje. 


## Referansetellere

Det at ett allokert minneområde kan refereres til av flere pekere samtidig gjør det vanskelig å manuelt å holde orden på hvem som gjør hva med minneområdet og når. 
En teknikk som kan benyttes for å forenkle dette noe er å benytte referansetellere. Det er fortsatt ikke noe automatikk inne i bildet her og det er i stor grad basert på at man følger konvensjoner... nøye.
Referansetellere er benyttet i f.eks Objective-C og Cocoa, som de påfølgende eksemplene benytter.


```objective-c
NSString* string = [[NSString alloc] init]; // refCount: 1
[string retain];                            // refCount: 2
[string release];                           // refCount: 1    
[string release];                           // refCount: 0, dealloker minne    

```

Hvert objekt har en referanseteller som holder orden på hvor hvor mange andre som peker på seg. Når ett objekt opprettes, så settes referansetelleren til 1. Hver gang man kaller `retain` på objektet, så inkrementeres telleren, mens kall til `release` dekrementerer den. Man kan sende en melding til objektet for å finne ut hvor mange som peker på det aktuelle objektet..  


```objective-c
NSString* string = [[NSString alloc] init]; 
[string retain];  
int count = [string refCount];
// Skriver ut: Referansetelleren for string er '2'.
NSLog(@"Referansetelleren for string er '%i'.", count); 
```

La oss tenke oss at vi har to objekter A og B der A holder på en referanse til B. Når A opprettes, så oppretter objekt A også objektet B, og gjør en `retain` på B.
Når den som opprettet A gjør en `release` på A, så dekrementeres telleren til 0, og A blir deallokert. Dersom objekt A sin `dealloc` (destructor) også gjør en `release` av B så blir denne referansetelleren også 0, og B blir deallokert, og minnet er da frigitt. (Så sant ikke noen andre har gjort en `retain` av A eller B).


### Retain cycles
En situasjon som man må være oppmerksom på når man bruker referansetellere er det man kaller for `retain-cycles`.  

Tilbake til objektene A og B. Hva om B trenger en referanse tilbake til A, og på grunn av det gjør en `retain` mot A? Når den som opprettet A gjør en `release` på A, så dekrementeres telleren, men siden B også har `retain` på A, så faller ikke A telleren til 0. A har jo også en `retain` på B, så da vil ikke B sin teller heller bli 0. Selv om B kaller `release` på A i sin `dealloc`, så hjelper ikke dette siden den aldri vil bli kalt. Vi har en minnelekkasje.

På grunn av dette så anbefaler Cocoa rammeverket at barn-foreldre relasjonen bruker en `weak` referanse. Ett objekt som har en `weak`referanse til ett annet objekt anses ikke å være "eieren" av objektet den peker på, og en slik referanse fører ikke til at objektet den peker på ikke inkrementerer sin teller. 


### @autoreleasepool

Dersom man oppretter en variabel som skal returneres fra en funksjon, så vil man ikke overlate ansvaret til kalleren av funksjonen å bestemme når minnet av den frigis, men det bør være opp til funksjonen som opprettet variablen. Kaller vi `release` før vi returnerer variabelen, så referansetelleren dekremeteres til 0, og verdien returnert fra funksjonen vil peker på ett korrupt minneområde.

Cocoa har da `@autoreleasepool`. I eksempelet nedenfor så ser du at man kaller `[helloString autorelease]`, som gjør at vi overlater ansvaret til den kjørende trådens `autoreleasepool` til å frigi minnet på ett senere tidspunkt. Kalleren av funksjonen vil da motta ett fortsatt gyldig minneområde som man kan `retain`e om man ønsker det. 


```objective-c

- (NSString*)createHelloString {
    NSString* helloString = [[NSString alloc] initWithString:@"Halloen!"];
    return [helloString autorelease];
}

```


## Dette var da komplisert...

Prinsippene vi har sett på så langt er kanskje ikke så kompliserte for enkle eksempler, men det kreves at man klarer å holde tungen rett i munnen jo større og mer komplisert ett program blir. Heldigvis så har de fleste moderne språk vi bruker i dag en eller annen form for automatisk minnehåndtering. 


## Garbage Collection

Garbage Collection hjelper oss med å unngå en rekke type feil som kan oppstå ved bruk av mekanismene bekrevet tidligere, som det å frigjøre minne som andre pekere peker på, minnelekasjer osv. Moderne implementasjoner av Garbage Collection har blitt uhyre effektive og baserer seg på kunnskap akkumulert over mange 10-år. Hvilke teknikker som er brukt i en Garbage Collector er et stort område som er langt utover hva denne artikkelen tar for seg, men noen eksempler er:

* Referanse tellere
* Tracing, hvor man holder orden på hvilke objekter som man kan nåes.
* Kompileringstidsananlyse hvor man ser på hvilke heap allokeringer som kan konverteres til stack allokeringer. 
* Andre teknikker og kombinasjoner av disse.


Nå er det slik at Garbage Collection ikke er helt uten ulemper. En ting er at man må bruke CPU ressurser for å holde orden på hvilke minnelokasjoner man kan frigi. Bruken av minne er også større enn en for manuell minnehåndtering, siden man nødvendigvis ikke får deallokert minnet umiddelbart etter det ikke lenger er påkrevet. Avhengig av implementasjon så vil selve oppryddingen av minne føre til uforutsigbare pauser i eksekveringen av ett program, som i noen miljøer ikke er akseptable. 


## Automatisk referanseteller (ARC)
 
Ulempene til Garbage Collection er en av grunnene til at Apple ikke valgte å gå for denne løsningen for sine systemer. Garbage Collection var tilgjengelig for Objective-C på OS X i ca 5 år fra 10.5 (2007) til 10.8 (2012). Etter dette så ble støtten fjernet. 

Alternativet som ble valgt var ARC, eller Automatic Reference Counting. Som navnet hinter om så ligger Reference Counting som ett fundament, men ved kompilering så settes det automatisk inn kall til `retain` og `release` automatisk av kompilatoren. Det som ikke automatisk håndteres er retain cycles. Det er fortsatt opp til programmereren til å løse opp i ved hjelp av `weak` referanser der det behøves. 

Fordelen med ARC er at brukt minne frigis så og si umiddelbart etter man ikke lenger har behov for det lenger. Dette optimaliserer minneforbruket, samt at man ikke trenger å kjøre en egen prosess som har i ansvar for å frigi ubrukt minne. 

Senere versjoner av Objective-C har støtte for ARC, mens Swift har alltid basert seg på det. C++ har shared_ptr og "smart pointer" som er konseptuellt likt ARC, men støtten for dette får man ved bruk av biblioteker og er ikke en språkegenskap.


## Hvordan håndteres minne i Rust?

Jeg nevnte innledingsvis at det som først fikk min oppmerksomhet ved Rust var hvordan man håndterer minne. Som vi har sett, så er det største problemet med minnehåndtering det å holde orden på data allokert på heapen, samt det å frigjøre det på en trygg måte når man lenger ikke har behov for det. Her har Rust tatt en helt annen tilnærming til dette problemet enn det vi har sett tidligere.


### Ownership
Sentralt i Rust sin minnehåndtering er det man kaller **Ownership**, som gjør at Rust kan garantere minnesikkerhet uten behov for f.eks Garbage Collection. Regler rundt ownership blir sjekket allerede ved kompilering av koden. Dersom en eller flere av reglene brytes, så får man en kompileringsfeil. Dette betyr igjen at man får minnesikkerhet uten at dette går ut over ytelsen av det kjørende programmet, og det er ganske så unikt!


#### Reglene for ownership:

* Hver verdi har kun en eier. Verdien er borte når eieren går ut av scope,
* En verdi kan ha _en_ muterbar referanse, eller flere ikke-muterbare referanser til enhver tid. 
* En referanse til en verdi kan ikke være null og kan ikke leve lenger enn verdien den refererer til. 



#### Variabel scope
Levetiden for en variabel er som man forventer og er vant med fra andre språk.

```rust
{
    let i = 42;                   // i er gyldig fra nå
    println!("Svaret er {}!", i); // Gjør noe med i
}
// Utenfor scope, i finnes ikke lenger
```

`i` er en int, så størrelsen er kjent ved kompilering, og man kan dytte den på stacken.


#### Minneallokering


```rust
{
    let s = String::from("hei");  // s er gyldig fra nå
    println!("{} på deg", s);     // Gjør noe med s 
}
// Utenfor scope, s finnes ikke lenger

```


Dette eksemplet ligner veldig på det forrige, men forsjellen er at vi her bruker `String::from`, som allokerer minne på heapen, men ellers så ser det likt ut. Vi gjør ikke ett kall til `free` før vi går ut av scope som vi måtte ha gjort i f.eks C. Derimot så vil Rust kalle en spesiell funksjon `drop`, når man går ut av scope, som deallokerer minnet.


#### Variabler og Move

I neste eksempel så setter vi verdien av `x` som er en `Int` til 5, før vi setter `y` til være det samme som `x`. 

```rust
let x = 5;
let y = x;
```

Siden `x` og `y` er enkle verdier med kjent størrelse, så blir disse to verdien pushet på stacken. `y` peker ikke på samme verdi som `x`, men det er to verdier, for reglene sier jo at en verdi har kun en eier. 
La oss prøve noe lignende, hvor vi i stedet for en enkel type som `Int` bruker en `String` type.

```rust
let s1 = String::from("Halloen");
let s2 = s1;
```

Dette ser jo også veldig likt ut som eksempelet over, men slik er det ikke. En String består av tre deler. En peker til ett minneområde på heapen, en lengde samt kapasitet. Denne informasjonen lagres på stacken, mens selve innholdet til strengen ligger på heapen, som pekeren peker på.


Når vi da tilordner s1 til s2, så kopieres de tre delene, peker, lengde og kapasitet til s2. 

![Bestandelene av s1](/images/blogg/rust-string.png)

```rust
let s1 = String::from("Halloen");
let s2 = s1;

println!("{}, verden!", s1);
```

Dataene som pekeren peker på kopieres ikke. s1 og s2 peker da på samme data på heapen. Vi sa at når en variabel går ut av scope, så vil dataene det pekes på bli frigitt. Dersom Rust hadde akseptert dette så hadde det medført at dataene som s1 og s2 peker på hadde blitt frigitt to ganger, noe som kan føre til minnefeil. 
Det betyr at dersom vi hadde forsøkt å kompilere koden over så hadde vi fått en kompileringsfeil som dette:


```
error[E0382]: borrow of moved value: `s1`
  --> src/main.rs:18:29
   |
16 |     let s1 = String::from("Halloen");
   |         -- move occurs because `s1` has type `String`, which does not implement the `Copy` trait
17 |     let s2 = s1;
   |              -- value moved here
18 |     println!("{}, verden!", s1);
   |                             ^^ value borrowed here after move

```

Grunnen til at man sier `move` og ikke `copy` er at man i tillegg til å kopiere de tre delene også invaliderer den første variabelen, så `s1` vil ikke bli liggende igjen.


Dersom man faktisk ønsker å gjøre det som kalles en "dyp" kopiering, altså opprette en kopi av selve dataene på heapen, så kan man kalle `clone`. Dette vil da kopiere både variabelen som ligger på stack, samt data som ligger på heap, slik at s1 og s2 ville ha refererert til to forskjellige minneområder, som betyr at man ikke vil gjøre en dobbel release når de går ut av scope, og de hadde hatt hver sin eier.

En `clone` er typisk en mye mer kostbar operasjon enn `move`.


### Eierskap og funksjoner

Når man sender verdier til funksjoner, så vil dette oppføre seg på tilsvarende måte som det gjør når man tilordner verdier til variabler.



```rust
fn main() {
  let counter = 1;                        // counter oppstår. 
  show(counter);                          // counter sendes (flyttes) til show, men  i32 er en Copy type .
 
  // …så counter er fortsatt gyldig.                       
  println!("{}", counter);

  let greeting = String::from("Halloen"); // greeting oppstår.
  shout(greeting);                        // eierskapet til greeting flyttes til shout funksjonen.                        
  println!("{}", greeting);               // greeting ikke lenger gyldig. 
}

fn shout(s: String) {
  println!("{}", s.to_uppercase());
}

fn show(value: i32) {
  println!("Opprinnelig verdi {}", value);
}
```

Kompilering av programmet over vil feile med denne meldingen.


```
error[E0382]: borrow of moved value: `greeting`
 --> src/main.rs:8:20
  |
6 | let greeting = String::from("Halloen");   
  |     -------- move occurs because `greeting` has type `String`, which does not implement the `Copy` trait
7 | shout(greeting);                        
  |       -------- value moved here
8 | println!("{}", greeting);                
  |                ^^^^^^^^ value borrowed here after move
```


De samme reglene for eierskap gjelder for returverdier fra funksjoner. Når en funksjon returnerer en verdi, så er det kalleren som overtar eierskapet.


```rust
fn main() {
  let greeting = create_greeting();       // main tar eierskapet til variabel opprettet av create_greeting
  println!("{}", greeting);
}

fn create_greeting() -> String {
  let greeting = String::from("Halloen"); // greeting oppstår og eies av create_greeting.
  greeting                                // greeting returnes, og overfører eierskapet til kaller.
}

```


### Referanser og "Låning"
Det virker jo litt tungvint å ikke kunne bruke variabler etter at man har flyttet eierskapet fordi man kaller en funksjon som tar variabelen som argument. Det er her *references* og *borrowing* kommer inn i bildet.

En referanse er en slags peker til data i minnet den har adressen til, men som er eid av en annen variabel. Til forskjell til pekere som vi er vant med fra andre språk, så er en referanse garantert til å peke på en gyldig verdi av en bestemt type. Garantien for gyldighet gjør da at null pekere ikke kan eksistere i ett Rust program.


La oss se hvordan `shout` kunne blitt implementert ved å få en referanse istedet for en verdi:

```rust
fn main() {
  let greeting = String::from(“Halloen”); // greeting oppstår
  shout(&greeting);                       // en referanse til greeting sendes til shout funksjonen.
  println!("{}", greeting);               // shout bare "lånte" bare greeting, så den er fortsatt gyldig.                                     
}

fn shout(message: &String) {              // Tar i mot en referanse til en String    
  println!("{}", message.to_uppercase());
}
```

Hva om vi skulle finne på å ville endre på det vi har "lånt"? Dette høres jo rett og slett uhøflig ut, men la oss si vi har fått lov da :)


### Muterbare referanser

Siden variabler er immutable som default, så må vi eksplisitt markere variabler vi ønsker å kunne modifisere med `mut`. 

```rust
fn main() {
    let mut hilsen = String::from("Halloen");
    change(&mut hilsen);
    println!("{}", hilsen);
}

fn change(message: &mut String) {
    message.replace_range(..message.len(), message.to_uppercase().as_str());
}
```


Om vi husker tilbake til reglene for eierskap, så var en av de: _En verdi kan ha _en_ muterbar referanse, eller flere ikke-muterbare referanser til enhver tid._


Det betyr da at dette er ok:

```rust
// Gyldig
fn main() {
  let greeting = String::from("Halloen");
  let copy1 = &greeting;
  let copy2 = &greeting;
  println!("{}, {}", copy1, copy2);
}
```

men ikke dette:

```rust
// Ikke gyldig
fn main() {
  let mut greeting = String::from("Halloen");
  let copy1 = &mut greeting;
  let copy2 = &mut greeting;
  println!("{}, {}", copy1, copy2);
}
```

Eksempelet overfor vil gi følgende kompileringsfeil:

```
error[E0499]: cannot borrow `greeting` as mutable more 
than once at a time
 --> src/main.rs:4:17
  |
3 | let copy1 = &mut greeting;
  |             ------------- first mutable borrow 
  |                               occurs here
4 | let copy2 = &mut greeting;
  |             ^^^^^^^^^^^^^ second mutable borrow 
  |                           occurs here
5 | println!("{}, {}", copy1, copy2);
  |                   ----- first borrow later used here
```


Noen kan kanskje tenke at denne begrensningen for muterbare referanser er en svakhet, men det er jeg ikke enig i, når man ser hvilken fordel det gir ved at man med dette unngår muligheten for `data race`s.
`Data race` kan oppstå når to eller flere pekere aksesserer samme data samtidig, hvor minst en av pekerene forsøker å skrive til til datene og man ikke har en mekanisme for å synkronisere tilgangen til dataene.


### Minnesikkerhet
Feil ved håndtering av minne kan gi dårligere ytelse og krasjer, men det gjør også at man kan usettes for hackerangrep som utnytter disse feilene. For eksempel så har Microsoft estimert at ca 70% av deres [CVE](https://www.redhat.com/en/topics/security/what-is-cve) kommer av minnehåndteringsfeil, og de mener at Rust er den beste løsningen for å unngå disse typene av feil. Google har kommet frem til andelen av CVE's i Chrome er like stor. 

Dette i tillegg til mindre minnebehov samt glimrende ytelse gjør at du kanskje burde vurdere Rust i ditt neste prosjekt? 










