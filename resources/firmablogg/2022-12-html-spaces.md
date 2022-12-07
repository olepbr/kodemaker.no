:title Håndtering av spaces i HTML
:author eivind
:tech [:html :javascript]
:published 2022-12-07

:blurb

Mesteparten av whitespace i HTML er irrelevant og fjernes av browseren. Så lenge vi lager HTML-koden selv fungerer dette flott. Men hva skjer om man for eksempel lager en editor som skal støtte HTML både inn og ut - og noen limer inn HTML-kode med forskjellig typer spaces?

:body

Denne posten beskriver en problemstilling jeg har jobbet en del med - og hvordan jeg har valgt å løse det. Om du har en opplagt bedre måte å løse dette på setter jeg stor pris på tips og innspill :)

## WYSIWYG HTML editor

Noe av min hverdag i frontend verden går ut på å lage en editor i et sakssystem. Innhold som kommentarer og beskrivelser lagres som HTML i systemet - og skal kunne redigeres i en wysiwyg editor. Det kan for eksempel se slik ut når man lager en tekst med et par ord i fet og understreket skrift:
![HTML editor med enkle funksjoner](/images/blogg/html_spaces_1.png)
Når man lagrer får man HTML kode ut - kanskje slik som dette:
```html
<p>
  dette er <strong>bold</strong> og <u>understreket</u>..
<p>
```
Jeg har jobbet mest med [Plate](https://github.com/udecode/plate), men jeg vil tro de fleste slike editorer oppfører seg ganske likt. Innholdet i editoren er på et internt JSON-basert format - også konverteres dette til HTML når man lagrer. Eller man leser inn HTML og konverterer til det interne formatet når man skal redigere innhold. Plate støtter disse konverteringene - så man får det meste "gratis" ut av boksen.

## Copy og paste fra nettsider

Problemet mitt oppstår når brukere har kopiert innhold fra en nettside og vil lime dette inn i editoren. Da forventer de at innholdet skal formateres likt som det var på nettsiden. Det som er kult er at når man markerer tekst i en browser og kopierer - så får man faktisk kopiert HTML koden - ikke bare teksten. Hvis vi tar utgangspunkt i en HTML side som ser slik ut og markerer all teksten og kopierer:
![Firefox med enkel HTML](/images/blogg/html_spaces_2.png)
Vi limer dette inn i editoren - og forventer da at det skal se ut som i editor-eksempelet øverst i denne posten - også ser det kanskje slik ut i stedet:
![HTML editor med crazy spaces](/images/blogg/html_spaces_3.png)
WTF - hvor kommer alle linjeskift og unødvendige spaces fra? Det viser seg at HTML-koden for siden faktisk er formatert slik. Noe den gjerne er om det kopieres fra et eller annet system som har brukt noe dynamisk opplegg for å printe ut kode:
```html
<p>
  dette
     er <strong>bold
</strong> og
<u>understreket</u>..
<p>
```
Akkurat - så når vi kopierer fra en side i browseren får vi HTML koden ut slik den er formatert. Men browsere er fine og viser det helt som forventet på nettsiden - og da må jo vi prøve å gjøre det samme, eller risikere å få kjeft av brukerne. *"Hvor vanskelig kan det være å lage en brukbar editor?"* - som det stod i en bug-rapport knyttet til dette. Overraskende vanskelig vil jeg si..

## Fjerne de riktige spaces

Ok. Problemet er identifisert. Vi må bare leke litt browser og rydde opp i spaces før vi sender HTML til editoren vår. Mozilla beskriver problemet og forklarer hvordan CSS håndterer whitespace: [How does CSS process whitespace?](https://developer.mozilla.org/en-US/docs/Web/API/Document_Object_Model/Whitespace#how_does_css_process_whitespace)

Vi får inn HTML som en tekststreng som vi konverterer til DOM node. Så har laget en util som ser ca slik ut for å rydde opp i HTML på vei inn (i TypeScript):
```typescript
export const stripSpaces = (html: string): string => {
  // Remove all space at start and end of line
  const trimmedLines = html.replace(/(^\s+)|(\s+$)/gm, "");

  // Replace line breaks, tabs and multiple spaces with one space
  const trimmedSpaces = trimmedLines.replace(/\r\n|\n|\r|\t|(\s+)/gm, " ");
  
  return trimmedSpaces;
}
```
HTML-koden over ender da opp med å se slik ut - noe som funker greit for vårt behov:
```html
<p> dette er <strong>bold </strong> og <u>understreket</u>.. <p>
```

## Men hva med..?

Akkurat når jeg tenker problemet er løst og ute av verden kommer det inn et nytt eksempel som ikke funker. Vår editor støtter nemlig å lime inn kode som skal formateres nøyaktig slik den er. Så eksempel-HTML kan utvides til å se slik ut:
```html
<p>
  dette
     er <strong>bold
</strong> og
<u>understreket</u>..
</p>
<pre><code>
  const myFn = () => {
    return 1 + 1;
  }
</code></pre>
```
Og når dette limes inn i editoren forventer vår bruker at det ser slik ut:
![HTML editor med kode-blokk](/images/blogg/html_spaces_4.png)
Altså hvis linjeskift og spaces står inne i en `pre` eller `code` tag (eller begge) så ønsker vi å beholde dem.

Ved hjelp av min venn StackOverflow finner jeg ut av følgende kode for å lage en `replaceNonPreserve` funksjon som kan gjøre en regex replace på tekst som ligger utenfor `pre` og `code` tagger:
```typescript
const PRESERVE_WHITESPACE_TYPES = ["pre", "code"];

const createTypeRegex = (types: string[]): RegExp =>
  new RegExp(`(${types.map(tag => `<${tag}>[^]*</${tag}>`).join("|")})`, "gi");

const PRESERVE_REGEX = createTypeRegex(PRESERVE_WHITESPACE_TYPES);

const replaceNonPreserve = (
  str: string,
  pattern: string | RegExp,
  replacement: string
): string =>
  str
    .split(PRESERVE_REGEX)
    .map(s => {
      if (`${s}` !== s) {
        return "";
      }
      if (s.match(PRESERVE_REGEX)) {
        return s;
      }
      return s.replace(pattern, replacement);
    })
    .join("");
```
Og util fra i sted blir endret til å bruke denne:
```typescript
export const stripSpaces = (html: string): string => {
  // Remove all space at start and end of line
  const trimmedLines = replaceNonPreserve(html, /(^\s+)|(\s+$)/gm, "");

  // Replace line breaks, tabs and multiple spaces with one space
  const trimmedSpaces = replaceNonPreserve(trimmedLines, /\r\n|\n|\r|\t|(\s+)/gm, " ");
  
  return trimmedSpaces;
}
```
Puh. Alle tester kjører og brukerne er happy.

## Bedre løsning?

Grunnen til at jeg skrev dette innlegget er at jeg sitter med en følelse av at dette kan løses enklere. Går det ikke an å få browseren til å gi ut ferdig strippet HTML eller noe sånt? Jeg vet ikke - har ikke funnet noe enda. Så altså om du leser dette og tenker at det finnes bedre måter å løse det på - gi meg gjerne beskjed!
