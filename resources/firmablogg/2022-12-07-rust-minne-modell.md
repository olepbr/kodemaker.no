:title Hvordan hånterer man minne i Rust
:author andre
:tech [:programming]
:published 2022-12-07

:blurb

Rust er det mest populære programmeringsspråket i følge Stack Overflow, men hva er det som er så spesielt med det? 
Det som først fikk min oppmerksomhet var hvordan man håndterer minne.

:body

Rust er det mest populære programmeringsspråket i følge Stack Overflow, men hva er det som er så spesielt med det? 
Det som først fikk min oppmerksomhet var hvordan man håndterer minne. For å finne ut av hvorfor Rust sin håndtering er så spesiell, la oss se på noen andre mekanismer som finnes.

## Typer av minnehåndtering
For at ett program skal kunne opprette variabler, så må det allokeres nok minne for å holde på  dataene variablene skal inneholde. Siden minne er en begrenset ressurs, så bør (må) man frigi minnet etter at man ikke lenger trenger dataene i variabelen, slik at minnet kan gjenbrukes.

Variabler allokeres enten på stack eller heap. Disse har forskjellige karakteristikker og bruksområder. En veldig enkel beskrivelse av disse er:

### Stack 

* Variabler blir liggende i samme rekkefølge som de blir "dyttet" på stakken.*
* Kompilatoren vet størrelsen på dataene som skal lagres på stacken. 
* Når en funksjon kalles, så blir samt verdier som er sendt til funksjonen, samt funksjonens lokale variabler blir dyttet på stakken. 
* Når funksjonen er ferdig, så vil disse verdiene blir fjernet fra stakken, sist inn, først ut.
* Tilgjengelig stakk minne en mindre enn for heap.
* Sikrere siden data laget på stacken kun kan aksesseres av tråden som utfører funksjonen.
* Raskere enn allokering og deallokering på heap.

_Det å dytte verdier på stakken er ikke sett på som allokering, siden området som trengs er kjent fra før_


### Heap

* Minne allokeres runtime. TODO
* Størrelsen på det allokert minnet er ikke kjent ved kompilering.
* En referanse (peker) opprettes på stacken, som igjen peker på det allikerte minneområdet.
* Mindre sikkert, siden området på heapen kan refereres til av flere pekere samtidig, og er synlig for alle tråder.
* Glemmer man å deallokere minnet etter bruk så vil det oppstå en minnelekasje.
* Allokere og deallokere er tregere enn for minnes som allokeres på stacken.



La oss først se på de vanligste typene av minnehåndtering. 

## Manuell minnehåntering
La oss for eksempel ser på hvordan man håndterer minne i C. Her er det opp til programmereren å allokere og deallokere minne som programmet har behov for.


```c
#include <stdlib.h>

void func()
{
    int *intPtr = (int *) malloc(sizeof(int)); // 1 Alloker minne
    // Gjør noe med dataene pekeren peker på...
    free(intPtr);                               // 2 Frigi minne
    return;                                     // 3 returner fra funksjonen
}
```

I dette eksempelet, så allokerer vi ett minneområde som har plass til å holde en `int` for den aktuelle arkitekturen det kompileres for. `intPtr` peker på dette området. Variabelen `intPtr` blir dyttet på stakken, som betyr at når funksjonen returnerer, så frigis minnet hvor `intPtr` er lagret. Minneområdet som `intPtr` peker på frigis av kallet til `free(intPtr);`. Skulle man glemme å uføre dette kallet før man returnerer fra funksjonen, så vil ikke minnet bli frigjort, og vi har en minnelekasje. I dette tilfellet så er det bare kanskje 4 bytes som lekker, men dersom `func` kalles tilstrekkelig manage nok ganger, så kan dette bli en betydelig lekkasje, som igjen vil kunnes føre til programmet krasjer til slutt.

Siden ett allikert minneområde kan pekes på av flere pekere samtidig, så må man være påpasselig slik at vi ikke deallokerer ett minneområde som kan være i bruk av andre. Gjør man det, så vil applikasjonen kunne krasje.


## Referansetellere

For å gjøre livet litt lettere for det stakkars programmereren, så kan man benytte referansetellere. Det er ikke noe automatikk inne i bildet her, og det er i stor grad baser på at man følger konvensjoner... nøye.
Referansetellere er benyttet i f.eks Objective-C og Cocoa, som de følgende eksemplene benytter.


```objective-c
NSString* string = [[NSString alloc] init]; // Referanseteller: 1
[string retain];                            // Referanseteller: 2
[string release];                           // Referanseteller: 1    
[string release];                           // Referanseteller: 0, dealloker minne    

```

Hvert objekt har en referanseteller, som teller hvor mange som peker på dette objektet. Når man oppretter ett objekt, så settes referansetelleren til 1. Når man kaller `retain` på objektet, så inkrementeres telleren, og `release` dekrementerer telleren. For å finne ut om hvor store referansetelleren er så kan man sende en melding til objektet for å spørre om  hvor mange som peker på en.  


```objective-c
NSString* string = [[NSString alloc] init]; 
[string retain];  
int count = [string refCount];
// Skriver ut: Referansetelleren for string er '2'.
NSLog(@"Referansetelleren for string er '%i'.", count); 
```


### Retain cycle TODO Mer/bedre forklaring
En feil som man fort kan innføre i kode som baserer seg på referansetellere er retain-cycles. For å beskrive hvordan en retain cycle fungerer så kan vi tenke oss ett objekt-A som peker på objekt-B, som igjen peker tilbake på objekt-A.  Her vil minnet aldri frigis, siden objektene peker på hverandre. For å komme rundt dette, så må den ene pekeren deklareres som `weak`. En `weak` referanse medfører at pekeren som har denne referansen ikke blir "eieren" av objektet den peker på, ved at referansetelleren ikke inkrementeres. Dersom objekt-B sin peker til object-A er deklarert som `weak`, så vil minnet disse to objektene holder på frigjøres når man kaller `release` på object-A.

### @autoreleasepool

Dersom man oppretter en variabel som skal returneres fra en funksjon, så vil man ikke overlate ansvaret til kalleren av funksjonen å bestemme når minnet av den frigis, men det bør være opp til funksjonen som opprettet variablen. Kaller vi `release` før vi returnerer variabelen, så referansetelleren dekremeteres til 0, og den returnerte pekeren peker på ett korrupt minneområde.

Cocoa har da `@autoreleasepool`. I eksempelet nedenfor så ser du at man kaller `[helloString autorelease]`, som gjør at vi overlater ansvaret til den kjørende trådens `autoreleasepool` til å frigir minnet på ett senere tidspunkt. Kalleren av funksjonen vil da motta en fortsatt gyldig minneområde som man kan `retain`e om man ønsker det. 


```objective-c

- (NSString*)createHelloString {
    NSString* helloString = [[NSString alloc] initWithString:@"Halloen!"];
    return [helloString autorelease];
}

```


## Dette var da komplisert...

Prinsippene vi har sett på ovenfor er kanskje ikke så kompliserte, men det krever fortsatt at man klarer å holde tungen rett i munnen, men dess større og mer komplisert ett program blir, dess lettere blir det å tråkke feil. Heldigvis så har de fleste moderne språk vi bruker i dag en eller annen form for automatisk minnehåndtering. 


## Garbage Collection

Garbage Collection hjelper oss med å unngå en rekke type feil som kan oppstå ved bruk av mekanismene bekrevet tidligere, som det å frigjøre minne som andre pekere peker på, minnelekasjer osv. Moderne implementasjoner av Garbage Collection har blitt uhyre effektive og bygger seg på kunnskap akkumulert over mange 10-år. Hvilke teknikker som er brukt i en Garbage Collector er et stort område som er langt utover hva denne artikkelen tar for seg, men noen eksempler er:

* Referanse tellere
* Tracing, hvor man holder orden på hvilke objekter som man kan nåes.
* Kompiletid ananlyse hvor man ser på hvilke heap allokeringer som kan konverteres til stack allokering. 
* Andre teknikker og kombinasjoner av disse.


Nå er det slik at Garbage Collection ikke er helt uten ulemper. En ting er at man må bruke CPU ressurser for å holde orden på hvilke minnelokasjoner man kan frigi. Bruken av minne er også større enn en for manuell minnehåndtering, siden man nødvendigvis ikke får deallokert minnet umiddelbart etter det ikke lenger er påkrevet. Avhengig av implementasjon så vil selve oppryddingen av minne føre til uforutsigbare pauser i eksekveringen av ett program, som i noen miljøer ikke er akseptable. 


## Automatisk referanseteller (ARC)
 
Ulempene til Garbage Collection er en av grunnene til at Apple ikke valgte å gå for denne løsningen. Garbage Collection var tilgjengelig for OS X i ca 5 år fra 10.5 (2007) til 10.8 (2012), hvor man da fjernet støtten for dette. 

Alternativet som ble valgt var ARC, Automatic Reference Counting. Som navnet hinter om så ligger Reference Counting som ett fundament, men ved kompilering så settes det inn kall til `retain` og `release` av kompilatoren. Det som ikke automatisk håndteres er retain cycles. Det er fortsatt opp til programmereren til å løse opp i ved hjelp av `weak` referanser der det behøves. 

Fordelen med ARC er at brukt minne deallokeres så og si med en gang man ikke har behov for det lenger. Dette optimaliserer minneforbruk, samt at man ikke trenger å kjøre en egen prosess som har i ansvar for å frigi ubrukt minne. 

Ulempen er at utvikler selv må vite hvor retain cycles kan oppstå, og dermed måtte løse opp i dette selv.

Senere versjoner av Objective-C har støtte for ARC, mens Swift har alltid basert seg på det. C++ har shared_ptr og "smart pointer" som er konseptuellt likt ARC, men støtten for dette får man ved bruk av biblioteker og er ikke en språkegenskap.


## Hvordan håndteres minne i Rust?

Det som er spesielt med Rust sin tilnærming til minnehåntering er at man har som mål og fange feil ved kompilering fremfor i kjøretid, og som gjør språket ett tryggere valg for kritiske applikasjoner. 

Som vi har sett, så er det største problemet med minnehåndtering vært holde orden på kode som bruker data allokert på heapen, samt å frigjøre minnet på en trygg måte når det lenger ikke er i bruk.

For å løse dette problemet, så har Rust baserer seg på det de kaller for **Ownership**, og som gjør at Rust kan garantere minnesikkerhet uten behov for f.eks Garbage Collection. Minnereglene som ownership bygger på sjekkes ved kompilering, dvs det vil ikke gå ut over ytelsen av det kjørende programmet. 

### Reglene rundt ownership

* Hver verdi har en variabel som er en `eier`.
* Det kan til enhver tid kun være en `eier` av en variabel
* Minnet til en verdi vil frigjøres når `eier` av verdien går ut av scope.


```rust
let helloString = String::from("Halloen!");
let helloString2 = helloString;   // helloString er nå ikke gyldig lenger, siden det kun kan være
                                  // en eier av en variabel, og man får kompileringsfeil.
```


La oss se på følgende program:

```rust

fn main() {
    let hello_1 = create_hello_string(); // 1️⃣
    println!("{}", hello_1);                
    let hello_2 = return_me(hello_1);    // 2️⃣
    println!("{}", hello_2);                
    println!("{}", hello_1);             // 3️⃣💥
}

fn create_hello_string() -> String {
    return String::from("Halloen");
}

fn return_me(str: String) -> String {
    str
}

```


1️⃣ `hello_1` er nå eier av strengen "Halloen" opprettet i `create_hello_string()`

2️⃣ `hello_2` er nå eier av strengen

3️⃣ Siden `hello_2` har blitt eier av strengen, så er `hello_1` ikke lenger er gyldig, og man vil får en kompileringsfeil:


```
error[E0382]: borrow of moved value: `hello_1`
 --> src/main.rs:6:20
  |
2 |     let hello_1 = create_hello_string();
  |         ------- move occurs because `hello_1` has type `String`, which does not implement the `Copy` trait
3 |     println!("{}", hello_1);
4 |     let hello_2 = return_me(hello_1);
  |                             ------- value moved here
5 |     println!("{}", hello_2);
6 |     println!("{}", hello_1);
  |                    ^^^^^^^ value borrowed here after move
  |
```

### References og Borrowing

For å kunne få tilgang til data uten å eie de, så kan vi benytte oss av Rust sin **Borrowing** mekanisme. Så, istedet for å sende objekter som data så kan vi heller sende en referanse til dataene. Det kan vi gjøre ved å bruke referanse operatoren `&`.

Nedenfor ser vi et eksempel hvor man sender en referanse til en funksjon `reverse_string`, fremfor en verdi. Kompilatoren garanterer at referanser alltid peker på et gyldig objekt.   


```rust
fn main() {
    let hello = String::from("Halloen");
    let olleh = reverse_string(&hello);
    println!("{} {}", hello, olleh)
}

fn reverse_string(s: &String) -> String {
    s.chars().rev().collect()
}
```

### Muterbare data

Man kan "låne" muterbare data med `&mut T`, som gir tilgang til både lesing og skriving til objektet. 



```rust
fn main() {
    let mut hello = String::from("Halloen");
    shout(&mut hello);
    println!("{}", hello);
}

fn shout(message: &mut String) {
    message.replace_range(..message.len(), message.to_uppercase().as_str());
}
```

Når man kjører dette programmet så vil det skrives ut teksten "HALLOEN".

### Er ikke muterbare data en kilde for feil da?

Man kan ha mange ikke-muterbare referanser eller EN muterbar referanse til en variabel i Rust.
Dersom man er vant til å ha muterbare data i programmene sine i andre språk, så vil man kanskje oppleve dette som er frustrerende begrensning. Denne begrensningen finnes av en grunn, som er at man med dette unngår `data race`s. 

`Data race` kan oppstå når to eller flere pekere aksesserer samme data samtidig, hvor minst en av pekerene forsøker å skrive til til datene og man ikke har en mekanisme for å synkronisere tilgangen til dataene.

### Minnesikkerhet

Rust tillater altså ett antall immutable referanser eller en enkel muterbar referanse samtidig, men samtidig ar alle referanser må være gyldige. Dette gjør da at null pekere ikke kan eksistere i ett Rust program. 

Det man oppnår med Rust sin måte og håndtere minne på, er minnesikkerhet. Microsoft har estimert at ca 70% av deres CVE kommer av minnehåndteringsfeil, og de mener at Rust er den beste løsningen for å unngå disse typene av feil. 








