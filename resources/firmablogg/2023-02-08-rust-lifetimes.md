:title Rust Lifetimes
:author andre
:tech [:rust :programming]
:published 2023-02-08

:blurb

Så du har endelig kommet i gang med dette spennede programmeringsspråket Rust. Du har fått tak på hvordan Borrow Checker fungerer, så nå kan du sette i gang og virkelig kode!
Men hva er det som møter deg? En ny kompilerfeil som sier:

```rust
'x' does not live long enough
```
Du har nettopp fått et bekjentskap form av Rust Lifetime, og det er nettopp det vi skal se nærmere på i denne bloggposten.

:body


Hver variablel i Rust har en "levetid" tilknyttet seg som definerer hvor lenge variablen skal leve i minnet. Dette gjør man for å unngå minnefeil som "dangling pointers", samt at variabler ikke blir brukt etter at minneområdet har blitt frigitt. Dette konseptet kalles Lifetimes, og er en del av Rust sitt typesystem.


La oss se på et eksempel som vil føre til en kompileringsfeil på grunn av brudd på en lifetime regel:

_...for du vet at minnefeil fanges ved kompilering og ikke run-time i Rust?_ Ikke? Se [her](http://localhost:3334/blogg/2022-12-08-rust-minne-modell/#hvordan-handteres-minne-i-rust) da!

```rust
let r;
{
    let x = 5;
    r = &x;
}
println!("r: {}", r);
```

Det som skjer her er at man først definerer en variabel `r`. Inne i den påfølgende blokken så defineres variabelen `x` som tilordnes verdien 5. Deretter så settes `r` til å referere til `x`, før man går ut av blokkens scope hvor `x` er definert. Dette vil føre til at minneområdet til `x` frigis, og `x` slutter å eksistere.  

Reultatet av dette er at `r` nå vil referere til et ugyldig minneområde, og det vil ikke Rust kompileren ha noe av. Kompileren er streng, men er såpass grei at den forklarer hvorfor den feiler.

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




## Lifetime annotations

En variabels levetid er noe Rust kompilatoren i mange tilfeller vil kunne utlede på egenhånd, og dette kalles `elision`. I de tilfellene den ikke er i stand til å utlede dette selv, så må vi tilby en hjelpende hånd ved å annotere de nødvendige variablene med en levetid. 

Syntaksen for dette er å definere en type med en ledende apostrof, etterfulgt av et variabelnavn som f.eks `'a`. Her har vi bare brukt navnet `a`, som også er det formatet man ofte ser, men det er ingenting som hindrer oss å bruke mer beskrivende navn dersom det er ønskelig. 

En eksplisitt annotering av en type ser da slik ut, 

```rust
&'a T
```
hvor `'a` allerede er introdusert. Dette leses da som: _En referanse til en type T hvor levetiden er 'a._



### Lifetime elisions

Kompileren benytter tre regler for å utlede lifetime for referanser som ikke er eksplisit annoterte. Regel nr 1 har med input å gjøre, mens de to andre har med output lifetimes. Etter at kompileren har gått gjennom disse tre reglene, og det fortsatt er referanser som man ikke har utledet lifetime for, så vil kompilatoren gi en feilmelding. 

#### Regel 1
Kompilatoren tilordner en lifetime til hvert referanseparameter. En funksjon med ett referanseparameter vil få følgende lifetime annotering:

```rust
fn one<'a>(x: &'a i32) {}
```

En funksjon med to referanseparametre:
```rust
fn two<'a, 'b>(x: &'a i32, y: &'b i32) {}
```
og så videre.


#### Regel 2

Dersom det er eksakt ett input lifetime parameter, så vil dennes lifetime bli satt på alle output lifetime parametere, slik:

```rust
fn one<'a>(x: &'a i32) -> &'a i32 {}

```

#### Regel 3

Dersom det er flere input lifetime parametere, men et av disse er `&self` eller `&mut self` fordi dette er en metode, så vil lifetime av `self` bli assignet til alle output lifetime parametere.


### Tid for "Demo"

La oss se på et praktisk eksempel. Vi har en funksjonen som tar i mot en referanse til en string som inneholder en eller flere kommaseparerte landkoder, som så splittes, trimmes og returneres som en vector av koder.


```rust

fn extract_languages(languages: &str) -> Vec<&str> {
    languages
        .split(',')
        .map(|lang| lang.trim())
        .collect()
}

```


Her tar vi eksakt en referanse som input parameter, `languages`. Argumentet er ikke `&self` eller `&mut self`. 
Dette betyr at regel 1, 2 og 3 oppfylt, referansene i returverdien vil få samme levetid som input, og vi trenger ikke å annotere typene med en levetid.


En stund senere så kommer det inn et ønske om at man skal kunne ekskludere en av landkodene dersom den finnes i strengen. Vi endrer funksjonen til å ta med et ekstra  argument `exclude_lang` som vi filtrerer på.

```rust
fn extract_languages(languages: &str, exclude_lang: &str) -> Vec<&str> {
    languages
        .split(',')
        .map(|lang| lang.trim())
        .filter(|code| *code != exclude_lang)
        .collect()
}
```


Her er regel 1 er fortsatt overholdt, så kompileren vil inplisitt sette `'a` for `languages` og `'b` for `excluded_lang`. 
Regel 3 er også oppfylt siden ikke noen av argumentetene er `&self` eller `&mut self`


Regel nummer 2 går derimot ikke gjennom, siden det er mer enn en input referanse og det returneres referanser. Vi kan da ikke vite hvilke lifetime returverdiene skal ha, så her må vi hjelpe til litt.



```rust
fn extract_languages<'a, 'b>(languages: &'a str, exclude_lang: &'b str) -> Vec<&'a str> {
    ...
}
```

Siden funksjonen ikke returnerer noe som har lifetime `'b`, så kan vi la være å definere den så funksjonen kan forenkles litt så den blir slik:

```rust
fn extract_languages<'a>(languages: &'a str, exclude_lang: &str) -> Vec<&'a str> {
    ...
}
```


Det å annotere Lifetimes er et konsept jeg ikke kjenner til i andre programmeringspråk, så det føles uvant. Det vi har sett så på så langt vil jeg allikvel påstå ikke er så vanskelig, så lenge man skjønner tre enkle regler. 

### Hva med referanser i en struct?

Dersom vi trenger å definere en struct som holder på referanser, så må vi definere en lifetime for disse.


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
Den aktuelle structen inneholder en referanse til en string slice, `line`. Man deklarerer lifetime som en generisk type som i dette tilfellet er `<'a>`. Man vil da kunne benytte denne lifetime inne i structen som ser her for `line`. Dette betyr at TextLine ikke kan leve lenger enn referansen i `line`.

Dersom en struct inneholder flere referanser, så vil man som oftest bruke samme lifetime for disse. Det finnes unntak, som f.eks dersom strukten har en metode som returnerer en referanse til en av medlemmene som er spesifikk for dette medlemmet.

Man kan og av og til må gå enda dypere inn i hvordan lifetimes fungere, men det vi har gått gjennom her vi ta oss langt på vei når vi programmerer i Rust. Vi sees...






