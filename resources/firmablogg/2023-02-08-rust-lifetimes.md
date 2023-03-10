:title Rust Lifetimes
:author andre
:tech [:rust :programming]
:published 2023-02-08

:blurb


Du har endelig kommet i gang med Rust, og du har fått tak på hvordan Borrow Checker fungerer. Nå kan du virkelig sette i gang å kode!
Men hva er det som møter deg? En ny kompilatorfeil som sier:

```rust
'x' does not live long enough
```

Det du nå har stiftet bekjentskap med er Rust Lifetimes, så da passer det fint at det er nettopp det vi skal se nærmere på i denne bloggposten.


:body

![Rust krabbe](/images/blogg/rust-crab.png)
Hver variablel i Rust har en "levetid" tilknyttet seg som definerer hvor lenge variablen skal leve i minnet. Dette gjør man for å unngå minnefeil som "dangling pointers", samt at variabler ikke blir brukt etter at minneområdet har blitt frigitt. Dette konseptet kalles Lifetimes, og er en del av Rust sitt typesystem.


La oss se på et eksempel som vil føre til en kompileringsfeil på grunn av brudd på en levetid-regel:

_...for du vet at minnefeil fanges ved kompilering i Rust?_ Ikke? Se [her](https://www.kodemaker.no/blogg/2022-12-08-rust-minne-modell#hvordan-handteres-minne-i-rust) da! 

```rust
let r;
{
    let x = 5;
    r = &x;
}
println!("r: {}", r);
```

Først definerer vi en variabel `r`. Inne i den påfølgende blokken defineres variabelen `x` som tilordnes verdien 5. Deretter settes `r` til å referere til `x`, før vi går ut av blokkens scope hvor `x` er definert. Dette vil føre til at minneområdet til `x` frigis, og `x` slutter å eksistere.

Resultatet er at `r` refererer til et ugyldig minneområde, og det vil ikke Rust kompilatoren ha noe av. Kompilatoren er streng, men er såpass grei at den forklarer hvorfor den feiler.


```
error[E0597]: `x` does not live long enough
  --> src/main.rs:21:13
   |
21 |         r = &x;
   |             ^^ borrowed value does not live long enough
22 |     }
   |     - `x` dropped here while still borrowed
23 |
24 |     println!("r: {}", r);
   |                       - borrow later used here
```


## Annoteringer for levetid
En variabels levetid er noe Rust kompilatoren i mange tilfeller vil kunne utlede på egenhånd, og dette kalles `elision`. I de tilfellene den ikke er i stand til å utlede dette selv, må vi tilby en hjelpende hånd ved å annotere de nødvendige variablene med en levetid. 

Syntaksen for dette er å definere en type med en ledende apostrof, etterfulgt av et variabelnavn som f.eks `'a`. 

Her har vi brukt `a`, som er en etablert konvensjon, men det er ingenting som hindrer oss å bruke mer beskrivende navn dersom det er ønskelig.
En eksplisitt annotering av en type vil se slik ut, 

```rust
&'a T
```
hvor `'a` allerede er introdusert. Dette leses som: _En referanse til en type T hvor levetiden er 'a._



### Levetid elisions

Kompilatoren benytter tre regler for å utlede levetid for referanser som ikke er eksplisitt annoterte. Regel nr 1 har med innparameter å gjøre, mens de to andre har med levetid på returverdi. Etter at kompilatoren har gått gjennom disse tre reglene, og det fortsatt er referanser som man ikke har utledet levetid for, så vil kompilatoren gi en feilmelding. 

#### Regel 1
Kompilatoren tilordner en levetid til hvert referanseparameter. En funksjon med ett referanseparameter vil få følgende levetid annotering:

```rust
fn one<'a>(x: &'a i32) {}
```

En funksjon med to referanseparametre:
```rust
fn two<'a, 'b>(x: &'a i32, y: &'b i32) {}
```
og så videre.


#### Regel 2

Dersom det er eksakt ett referanseparametre, så vil denne levetiden også bli benyttet på returverdien(e):

```rust
fn one<'a>(x: &'a i32) -> &'a i32 {}

```

eller dersom returverdien er en vector med referanser:

```rust
fn one<'a>(x: &'a i32) -> Vec<&'a i32>  {}

```

#### Regel 3

Dersom det er flere innparametere som er referanser, men et av disse er `&self` eller `&mut self` fordi dette er på en metode, så vil levetiden av `self` bli tilordnet alle returverdier.


### Tid for "Demo"

La oss se på et praktisk eksempel. Vi har en funksjon som tar imot en referanse til en string slice som inneholder en eller flere kommaseparerte landkoder, som så splittes, trimmes og returneres som en vector av koder.


```rust

fn extract_languages(languages: &str) -> Vec<&str> {
    languages
        .split(',')
        .map(|lang| lang.trim())
        .collect()
}

```


Her tar vi én referanse som innparameter, `languages`, og innparameter er ikke `&self` eller `&mut self`. 
Dette betyr at regel 1, 2 og 3 oppfylt. Referansene i returverdien vil få samme levetid som innparameter, og vi trenger ikke å annotere typene med en levetid.


En stund senere så kommer det et ønske om at man skal kunne ekskludere en av landkodene dersom den finnes i strengen. Vi endrer funksjonen til å ta med et ekstra innparameter `exclude_lang` som vi filtrerer på:

```rust
fn extract_languages(languages: &str, exclude_lang: &str) -> Vec<&'a str> {
    languages
        .split(',')
        .map(|lang| lang.trim())
        .filter(|code| *code != exclude_lang)
        .collect()
}
```


Regel 1 fortsatt overholdt, så kompilatoren vil implisitt sette `'a` for `languages` og `'b` for `excluded_lang`. 
Regel 3 er også oppfylt siden ingen av innparameterene er `&self` eller `&mut self`

Regel nummer 2 går derimot ikke gjennom, siden det er mer enn ett referanseparameter og returverdien er en referanse. Vi kan da ikke vite hvilke levetid returverdier skal ha, så her må vi hjelpe til litt.


```rust
fn extract_languages<'a, 'b>(languages: &'a str, exclude_lang: &'b str) -> Vec<&'a str> {
    ...
}
```

Siden funksjonen ikke returnerer noe som har levetid `'b`, kan vi unnlate å definere den. Funksjonen kan da forenkles slik:

```rust
fn extract_languages<'a>(languages: &'a str, exclude_lang: &str) -> Vec<&'a str> {
    ...
}
```

Det å annotere levetid er et konsept jeg ikke har sett i andre programmeringspråk, så det føles uvant. Det vi har sett på så langt vil jeg allikevel påstå ikke er så vanskelig, gitt at man skjønner tre enkle regler.


### Hva med referanser i en struct?

Dersom vi definerer en struct som holder på referanser, må vi definere levetid på disse.


```rust
struct TextLine<'a> {
    line: &'a str,
}


fn main() {
    let document: String = String::from("En\nto\ntre");
    let first_line = TextLine {line: document.split("\n").next().expect("Ingen linjer funnet")};
    println!("{}", first_line.line);
}

```


Denne structen inneholder en referanse til en string slice, `line`. Man deklarerer levetiden som en generisk type, i dette tilfellet `<'a>`. Man vil da kunne benytte denne inne i structen, som du ser her for `line`. Dette betyr at `TextLine` ikke kan leve lenger enn referansen i `line`.

Dersom en struct inneholder flere referanser, vil disse ofte bruke samme levetid. Det finnes unntak, som f.eks dersom structen har en metode som returnerer en referanse til en av medlemmene som er spesifikk for dette medlemmet.

Av og til må man gå enda dypere inn i hvordan levetider fungerer, men det vi har gått igjennom her tar oss langt på vei når vi programmerer i Rust. Vi sees...

