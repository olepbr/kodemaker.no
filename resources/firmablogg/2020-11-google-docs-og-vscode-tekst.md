:title Hvordan tegnes tekst i Google Docs og Visual Studio Code?
:published 2020-11-18
:author august
:tech [:frontend :web]

:blurb

Det er 2020, og nettleseren har [_fortsatt_ ikke UITableView](https://twitter.com/floydophone/status/1186903328560566272). Det er ikke fordi de som lager nettlesere er inkompetente, men fordi WC3-spesifikasjonene gjør det [umulig å lage](https://twitter.com/rikarends/status/1327192116968255488) kjappe implementasjoner av GUI-rendring på web.

Derfor lever vi med at Slack bruker 2gb minne for å vise et lite knippe tekstlinjer. (Desktop-appen til Slack er et webview.)

Men vi har to store unntak: både Google Docs og Visual Studio Code klarer å rendre _enorme_ dokumenter på null komma svisj, med lav minnebruk og greier. Hvordan klarer de det?

:body

Det er 2020, og nettleseren har [_fortsatt_ ikke UITableView](https://twitter.com/floydophone/status/1186903328560566272). Det er ikke fordi de som lager nettlesere er inkompetente, men fordi WC3-spesifikasjonene gjør det [umulig å lage](https://twitter.com/rikarends/status/1327192116968255488) kjappe implementasjoner av GUI-rendring på web.

Derfor lever vi med at Slack bruker 2gb minne for å vise et lite knippe tekstlinjer. (Desktop-appen til Slack er et webview.)
 
Men vi har to store unntak: både Google Docs og Visual Studio Code klarer å rendre _enorme_ dokumenter på null komma svisj, med lav minnebruk og greier. Hvordan klarer de det?

<script type="text/javascript">
(function (GLOBAL) {
  const demoFont = "16px Arial";

  const measurementDiv = document.createElement("div");
  measurementDiv.style.position = "absolute";
  measurementDiv.style.top = "0px";
  measurementDiv.style.left = "0px";
  measurementDiv.style.visibility = "hidden";
  measurementDiv.style.whiteSpace = "pre";
  measurementDiv.style.font = demoFont;
  document.body.appendChild(measurementDiv);
  
  GLOBAL.libDragonLolTextMeasurementDiv = measurementDiv;

  GLOBAL.libDragonLolTextMeasureProperly = (text) => {
    measurementDiv.textContent = text;
    
    const range = document.createRange();
    const textNode = measurementDiv.firstChild;

    const measuredChars = [];
    let idx = 0;
    
    for (const c of text) {
      const length = c.length;
      
      range.setStart(textNode, idx);
      range.setEnd(textNode, idx + length);
      
      const rect = range.getClientRects()[0];
      
      measuredChars.push({c: c, width: rect.width, left: rect.x});
      
      idx += length;
    }
    
    range.detach();
    
    return measuredChars;
  };
  
  GLOBAL.libDragonLolTextMeasureShitty = (text) => {
    const measuredChars = [];
    let currLeft = 0;
    
    for (const c of text) {
      measurementDiv.textContent = c;
      const width = measurementDiv.getBoundingClientRect().width;
      measuredChars.push({c: c, width: width, left: currLeft});
      currLeft += width;
    }
    
    return measuredChars;
  };
  
  const getRandRgb = (base, scale) => {
    const randBg = base + (Math.random() * scale);
    return `rgb(${randBg}, ${randBg}, ${randBg}, 0.5)`;
  };
  
  GLOBAL.libDragonLolTextRunAnimation = (opts) => {
    const text = opts.text;
    const target = opts.target;
    const label = opts.label;
    const measuredChars = opts.measuredChars;
    
    target.innerHTML = "";
    target.style.display = "flex";
    target.style.justifyContent = "center";
    target.style.margin = "40px auto";
    
    const wrapperEl = document.createElement("div");
    wrapperEl.style.padding = "30px";
    wrapperEl.style.border = "5px solid #ddd";
    target.appendChild(wrapperEl);
  
    measurementDiv.textContent = text;
    const fullTextWidth = measurementDiv.getBoundingClientRect().width;
      
    const originalHeader = document.createElement("div");
    originalHeader.textContent = `Original tekst`;
    originalHeader.style.fontWeight = "bold";
    wrapperEl.appendChild(originalHeader);
      
    const originalText = document.createElement("div");
    originalText.textContent = text;
    originalText.style.backgroundColor = "#dddddd";
    originalText.style.font = demoFont;
    originalText.style.width = `${fullTextWidth}px`;
    wrapperEl.appendChild(originalText);
    
    const originalTextWidthElem2 = document.createElement("div");
    originalTextWidthElem2.style.width = `${fullTextWidth}px`;
    originalTextWidthElem2.style.height = "1px";
    originalTextWidthElem2.style.backgroundColor = "#333";
    wrapperEl.appendChild(originalTextWidthElem2);
    
    const charByCharHeader = document.createElement("div");
    charByCharHeader.textContent = label;
    charByCharHeader.style.fontWeight = "bold";
    charByCharHeader.style.marginTop = "20px";
    wrapperEl.appendChild(charByCharHeader);
    
    const charByCharTarget = document.createElement("div");
    charByCharTarget.style.height = `${measurementDiv.getBoundingClientRect().height}px`;
    charByCharTarget.style.position = "relative";
    charByCharTarget.textContent = "...";
    charByCharTarget.style.font = demoFont;
    wrapperEl.appendChild(charByCharTarget);
   
    const originalTextWidthElem = document.createElement("div");
    originalTextWidthElem.style.width = `${fullTextWidth}px`;
    originalTextWidthElem.style.height = "1px";
    originalTextWidthElem.style.backgroundColor = "#333";
    wrapperEl.appendChild(originalTextWidthElem);
    
    let currChar = 0;
    
    const tickAnimationLoop = () => {
      if (currChar === measuredChars.length) {
        currChar = 0;
        setTimeout(() => {
          tickAnimationLoop();
        }, 2000);
        return;
      }
    
      if (currChar === 0) {
        charByCharTarget.innerHTML = "";
      }
      
      const mc = measuredChars[currChar];
    
      const cEl = document.createElement("span");
      cEl.style.position = "absolute";
      cEl.style.left = `${mc.left}px`;
      cEl.style.width = `${mc.width}px`;
      cEl.style.backgroundColor = getRandRgb(0, 255);
      cEl.textContent = mc.c;
      charByCharTarget.appendChild(cEl);
      
      currChar++;
      
      setTimeout(() => {
        tickAnimationLoop();
      }, 100)
    };
    
    tickAnimationLoop();
  };
  
  const editorLineHeight = 20;

  const partitionBy = (xs, f) => {
    const res = [];
    let curr = [xs[0]];
    let currState = f(xs[0]);
    xs.slice(1).forEach(it => {
       const newState = f(it);
       if (currState !== newState) {
         currState = newState;
         res.push(curr);
         curr = [it];
       } else {
         curr.push(it);
       }
    });
    
    res.push(curr);
    
    return res;
  };
  
  GLOBAL.libDragonLolTextEditorOmg = (opts) => {
    const target = opts.target;
    const text = target.textContent;
    const textMeasureF = opts.textMeasureF;
    const label = opts.label;
  
    target.innerHTML = "";
    target.style.display = "flex";
    target.style.justifyContent = "center";
    target.style.margin = "40px auto";
    
    const wrapperEl = document.createElement("div");
    wrapperEl.style.width = "400px";
    wrapperEl.style.border = "5px solid #ddd";
    wrapperEl.style.padding = "10px";
    target.appendChild(wrapperEl);
    
    const editorTitleEl = document.createElement("div");
    editorTitleEl.textContent = label;
    editorTitleEl.style.fontWeight = "bold";
    editorTitleEl.style.marginBottom = "15px";
    wrapperEl.appendChild(editorTitleEl);
    
    const editorEl = document.createElement("div");
    editorEl.style.font = demoFont;
    editorEl.style.position = "relative";
    editorEl.style.userSelect = "none";
    wrapperEl.appendChild(editorEl);
    
    const editorCursor = document.createElement("div");
    editorCursor.className = "lib-dragon-lol-text-editor-omg-blinking-cursor";
    editorCursor.style.backgroundColor = "#000";
    editorCursor.style.width = "2px";
    editorCursor.style.height = `${editorLineHeight}px`;
    editorCursor.style.position = "absolute";
    editorCursor.style.top = "0px";
    editorCursor.style.left = "0px";
    editorEl.appendChild(editorCursor);
    
    const arrowKeysWrapper = document.createElement("div");
    arrowKeysWrapper.style.marginTop = "10px";
    arrowKeysWrapper.style.display = "flex";
    arrowKeysWrapper.style.alignItems = "center";
    arrowKeysWrapper.style.justifyContent = "center";
    arrowKeysWrapper.style.gap = "5px";
    wrapperEl.appendChild(arrowKeysWrapper);
    
    const leftArrowKey = document.createElement("button");
    leftArrowKey.textContent = "< Flytt venstre";
    leftArrowKey.style.padding = "5px";
    leftArrowKey.style.touchAction = "manipulation";
    arrowKeysWrapper.appendChild(leftArrowKey);
    
    const rightArrowKey = document.createElement("button");
    rightArrowKey.textContent = "Flytt høyre >";
    rightArrowKey.style.padding = "5px";
    rightArrowKey.style.touchAction= "manipulation";
    arrowKeysWrapper.appendChild(rightArrowKey);
    
    const sillyKey = document.createElement("button");
    sillyKey.textContent = "Bevis idioti";
    sillyKey.style.padding = "2px";
    sillyKey.style.touchAction= "manipulation";
    arrowKeysWrapper.appendChild(sillyKey);
    
    const measuredText = textMeasureF(text);
    const editorWidth = editorEl.getBoundingClientRect().width;

    const mtWords = partitionBy(measuredText, (mt) => mt.c === " ")
      .map((mts) => {
        return {
          mts: mts, 
          left: mts[0].left,
          word: mts.map(it => it.c).join(""),
          wordWidth: mts
            .map(it => it.width)
            .reduce((res, curr) => res + curr, 0)
        }
      });
    
    const mtLines = [[]];
    let currLeftBaseX = 0;
    mtWords.forEach(mtWord => {
      const currLeft = (mtWord.left - currLeftBaseX) + mtWord.wordWidth;
      if (currLeft > editorWidth) {
        const currLine = mtLines[mtLines.length - 1];
        const currWord = currLine[currLine.length - 1];
        currLeftBaseX = currWord.left;
        mtLines.push([mtWord])
      } else {
        mtLines[mtLines.length - 1].push(mtWord);
      }
    });
    
    editorEl.style.height = `${mtLines.length * editorLineHeight}px`;
    
    const editorLineElements = mtLines.map((mtLine, idx) => {
      const editorLineEl = document.createElement("div");
      editorLineEl.className = "lib-dragon-lol-text-editor-omg-prove-lunacy";
      editorLineEl.style.position = "absolute";
      editorLineEl.style.top = `${editorLineHeight * idx}px`;
      editorLineEl.style.backgroundColor = getRandRgb(200, 50);
      editorLineEl.style.height = `${editorLineHeight}px`;
      editorLineEl.textContent = mtLine.map(it => it.word).join("");
      return editorLineEl;
    }); 
    
    editorLineElements.forEach(it => editorEl.appendChild(it));
     
    const cursorPositions = [];
    let currLeftBase = 0;
    mtLines.forEach((mtLine, lineIdx) => {
      const lineMts = mtLine.map(mtWord => mtWord.mts).flat();
      lineMts.forEach(mt => {
        cursorPositions.push({line: lineIdx, left: mt.left - currLeftBase, mt: mt})
      });
      
      const lastMt = lineMts[lineMts.length - 1];
      currLeftBase = lastMt.left + lastMt.width;
    });
    
    const lastCursorPosition = cursorPositions[cursorPositions.length - 1];
    cursorPositions.push({lineIdx: lastCursorPosition.lineIdx, left: lastCursorPosition.left + lastCursorPosition.mt.width});
    
    let currCursorPos = 0;
    const renderCursor = () => {
      const cursorPos = cursorPositions[currCursorPos];
      editorCursor.style.top = `${cursorPos.line * editorLineHeight}px`;
      editorCursor.style.left = `${cursorPos.left}px`;
      
      editorCursor.parentNode.removeChild(editorCursor);
      editorEl.appendChild(editorCursor);
    };
    
    renderCursor();
        
    leftArrowKey.addEventListener("click", () => {
      if (currCursorPos !== 0) {
        currCursorPos--;
        renderCursor();
      }
    });
    
    rightArrowKey.addEventListener("click", () => {
      if (currCursorPos < measuredText.length) {
        currCursorPos++;
        renderCursor();
      }
    });
    
    let isSilly = false;
    sillyKey.addEventListener("click", () => {
      isSilly = !isSilly;
      if (isSilly) {
        editorCursor.classList.add("lib-dragon-lol-text-editor-omg-prove-lunacy-flipping-eh");
        editorLineElements.forEach((it, idx) => {
           it.style.transform = `translate(${20 * (idx % 2 === 0 ? 1 : -1)}px, -10px)`; 
        });
      } else {
        editorCursor.classList.remove("lib-dragon-lol-text-editor-omg-prove-lunacy-flipping-eh");

        editorLineElements.forEach(it => {
          it.style.transform = "";          
        });
      }
    });
  }
}(window))
</script>

<style>
@keyframes lib-dragon-lol-text-editor-omg-blinking-cursor-animation {
    to {
        visibility: hidden;
    }
}

.lib-dragon-lol-text-editor-omg-blinking-cursor {
    visibility: visible;
    animation: lib-dragon-lol-text-editor-omg-blinking-cursor-animation 1s steps(2, start) infinite;
}

.lib-dragon-lol-text-editor-omg-prove-lunacy {
    transition: transform 1s;
}

@keyframes lib-dragon-lol-text-editor-omg-prove-lunacy-flipping-eh-animation {
  from {
    transform: rotate(0deg);
  }
  
  to {
    transform: rotate(360deg);
  }
}

.lib-dragon-lol-text-editor-omg-prove-lunacy-flipping-eh {
  animation: lib-dragon-lol-text-editor-omg-prove-lunacy-flipping-eh-animation 1s linear infinite;
}
</style>

## Problemet

Hva betyr det egentlig at nettleseren ikke har UITableView?

Husker du iPhone 3G? Første iPhone lansert i Norge?

Den kunne flagge med følgende specs: **128 MB RAM**, og en **620 MHz single core** 32bit ARM-prosessor fra Samsung. Lenge siden du har sett "MB" og "MHz" ved siden av tall, tipper jeg?

Kanskje viktigst: hastigheten mellom RAM og CPU var lav (103 MHz). Det førte til enorme utfordringer når man skulle scrolle igjenom store mengder data (e-poster, kontaktliste, musikkbibliotek...) i full fart, og opprettholde 60 FPS.

Kjapp scrolling ville rett og slett bruke opp _hastigheten_ til RAM-en, på å sende GUI-objekter frem og tilbake for prosessering mens den tegner.

UITableView løste dette så godt som det lot seg gjøre, og det var mulig å få skrudd sammen en app med kjapp scrolling i 60 FPS. Den gjorde hovedsaklig to viktige ting:

**UITableView rendret kun synlige rader.** Her var det plenty av detaljer. F.eks husker du kanskje at mail-appen viste emnet, og så to linjer med innholdet fra mailen. Hva betyr det? Jo, at Mail-appen ikke trengte å vite innholdet i mailen, men bare kjøre litt ren matte i CPU-en for å finne ut hvor du var i innboksen på en gitt scroll-posisjon.

**Den gjenbrukte GUI-objekter aggressivt**. Når en rad ble usynlig, ville den sende deg det gamle objektet for den usynlige raden, og be deg om å oppdatere de med ny tekst osv, sånn at iOS skulle slippe å bruke dyrebar tid på å lage nye UIView, UITextView, UIImageView osv over den uhorvelig trege RAM-en. 

Nå kjører vi jo ikke nødvendigvis websidene våre på en iPhone 3g lengere. Men du kan jo prøve å lage en React-app som skal tegne tabeller med noen tusen rader og se hvor gøy og kjapt _det_ er å lage.

Dette er fordi webben _ikke har noe som ligner på UITableView_.
 
Når du skal tegne en svær tabell på en webside, må du tegne alle radene på en gang.

Og dette er grunnen til at Slack bruker 2 GB minne.

Men hva er det Google Docs og Visual Studio code gjør?

Google Docs lar deg jo åpne et dokument som er hundrevis av megabytes stort, men nettleseren vil ikke allokere opp hundrevis av megabytes. 

Visual Studio Code, som er 100% web-basert, klarer helt fint å åpne en svær tekstfil, _uten_ å bruke opp RAM-en din til å rendre den. Den vil ligge langt under størrelsen på selve filen.

## Løsningen

La oss brainstorme litt.

Alle idéer er lov, gode eller dårlige!

Hva med å bare tegne alt selv?

Da må man jo finne ut størrelsen på teksten, bokstav for bokstav. Hvis ikke vil man jo ikke kunne plassere en blinkende cursor som viser hvor du er i teksten.

Så er det markering av tekst og alt sånt. Det må du også lage helt selv. Lytte på musepeker-events og X-koordinater og Y-koordinater, og tegne en div bak teksten din der den er markert.

Det sier jo seg selv at det blir helt tullete. Alt for upraktisk for den virkelige verden.

Så naturligvis er dette fremgangsmåten til både Visual Studio Code og Google Docs.

😅

<img style="width: 100%; max-width: 700px; border: 1px solid #ccc;" src="/images/blogg/monaco-lines-and-cursor.png" />

Jepp, over ser du Visual Studio Code som tegner en absolutt posisjonert div per linje i teksten. Og her mener jeg altså hver _visuelle_ linje. Så hvis du har en lang tekstlinje som brekker over to linjer, så er det to DIV-er. 😅😅

<img style="width: 100%; max-width: 600px; border: 1px solid #ccc;" src="/images/blogg/kix-paragraphs.png" />

Så absolutt! Dette er Google Docs som rendrer en div per avsnitt, og så en div per linje i avsnittet. De bruker ikke absolutt posisjonering, men setter høyden og lener seg på at nettleseren posisjonerer ting under hverandre riktig. Sikkert en veldig god grunn til det, som vi vanlig dødelige bare kan drømme om å forstå. 😅😅😅


<img style="width: 100%; max-width: 450px; " src="/images/blogg/Hindenburg_disaster.jpg" />

Dette blinkskuddet er fra et ukjent sted i kildekoden til enten Google Docs eller Visual Studio Code.

Og til slutt:


<img style="width: 100%; border: 1px solid #ccc;" src="/images/blogg/kix-cursor.png" />

Dette er naturligvis cursoren som tikker avgårde mens du skriver og viser hvor du er i teksten på Google Docs. En absolutt posisjonert div med en CSS-animasjon som får den til å blinke. Hvorfor 664px fra venstre og 314px fra toppen? Ja, det skulle du vel likt å vite, tenker jeg. 😅😅😅😅😅😅😅😅

## Implementasjonen 😅😅😅

Ja, ja. Vi får bare brette opp ermene og kjøre på.

La oss starte med måling av tekst.

Her møter vi fort på problemet med _kerning_. Du kan sikkert din ABC. Men kan du din CBA? Eller AV Wa WaWAWa? 

For det er nemlig ikke sikkert at nettleseren tegner A-en i ABC likt som A-en i BAC. Her en grafikk stjelt fra Wikipedia:

<img style="width: 100%; max-width: 450px" src="/images/blogg/kerning.png" />

Hvor bred en bokstav gjør en tekst kommer an på konteksten den er i. 

Vi prøver oss på en dum implementasjon av å måle tekst, som baserer seg på å måle en og en bokstav:

<noscript><p><strong>Til info: du har visst ikke aktivert JavaScript i nettleseren din. Denne bloggposten handler om JavaScript, og bruker JavaScript til å demonstrere ting. Så da vet du det.</strong></p></noscript>

<div id="lol_dragon_text_shitty_mode">AV Wa AV Wa AV Wa AV Wa</div>

<script type="text/javascript">
(function (GLOBAL) {
  const target = document.getElementById("lol_dragon_text_shitty_mode");
  const text = target.textContent;
  
  GLOBAL.libDragonLolTextRunAnimation({
    target: target,
    text: text,
    label: "En og en bokstav med createRange",
    measuredChars: GLOBAL.libDragonLolTextMeasureShitty(text)
  });
}(window))
</script>

Og det ble jo feil.

Det du ser over er ekte JavaScript som gjør tekst-beregninger i nettleseren din her og nå. Kort fortalt, gjør den følgende:

* Lag en skjult div et sted på siden.
* Gå igjennom en og en bokstav i teksten
* Set `textContent` på skjult div til denne bokstaven, og bruk `hiddenDiv.getBoundingClientRect().width`
* Finn frem koseputa, gjør deg klar til å gråte.
* Lag en span per bokstav, med `position: absolute;` og `left` satt basert på breddene du målte over.

På grunn av kerning, får vi ikke den faktiske bredden til bokstaven. 

(Spoiler: du kommer snart til å gråte. Stålsett deg.)

## Mellomspill: lage din egen kerning?

Nei. Niks og nei.

Hvis Google Docs og Visual Studio Code skal ha _sjans_ til å være kjappe, er de nødt til å lene seg på nettleseren sin egen tekst-rendring. Nettleseren har tilgang på API-er internt som du ikke får tilgang til fra JavaScript, _og_ nettleseren er jo implementert i Rust eller C++ eller noe annet snacks, som kjører i flere tråder og er optimalisert som bare det.

Heldigvis kan vi blande en hack med en hack, og få det vi trenger.


## Mål tekst med `createRange` og venner

Her er kravspesifikasjonen vår:

* Vis en tekslinje som én lang tekst, ikke en span per bokstav (sånn at nettleseren får rendret den fint)
* Mål faktisk størrelse på bokstaver slik de er, når de er tegnet slik.

Det får vi faktisk til, med "ranges". Et API du aldri trodde du skulle få bruk for! Men nå ha endelig (😅) dagen kommet.

<div id="lol_dragon_text_epic_mode">AV Wa AV Wa AV Wa AV Wa</div>

<script type="text/javascript">
(function (GLOBAL) {  
  const target = document.getElementById("lol_dragon_text_epic_mode");
  const text = target.textContent;
    
  GLOBAL.libDragonLolTextRunAnimation({
    target: target,
    text: text,
    label: "En og en bokstav med createRange",
    measuredChars: GLOBAL.libDragonLolTextMeasureProperly(text)
  });
}(window))
</script>

Voila! Det ser med ett mye bedre ut. Fremgangsmåten er:

* Putt _hele_ teksten i den skjulte måle-diven vår
* Lag et range-objekt med `document.createRange()`
* Kall `range.setStart()` og `range.setEnd()` slik at rangen din dekker over en og en bokstav i teksten
* Bruk `getClientRects()` på range-objektet for å spørre den om nøyaktige proporsjoner på teksten den dekker

Da måler vi "ekte" bredde på teksten, og ikke bare en og en bokstav i isolasjon. Og da kan jo nettleseren gjøre hva den vil av kerning og annet snacks, vi får gode data tilbake uansett.

## For the lulz: multi byte characters 

Det er lurt å ta definisjonen av "bokstav" seriøst når man skal måle bokstaver.

Det er flere måter å skyte seg selv i foten på her, avhengig av hvilken blemme du har klart å gjøre.

Her har jeg hoppet utenfor stupet med et par varianter hvor vår gode gamle venn, unicode-firkanten, kommer på besøk.

<div id="lol_dragon_text_emoji_fail_2">Hehe 😅 Funker det?</div>

<script type="text/javascript">
(function (GLOBAL) {
  const measurementDiv = GLOBAL.libDragonLolTextMeasurementDiv;
  
  const target = document.getElementById("lol_dragon_text_emoji_fail_2");
  const text = target.textContent;
    
  const measuredChars = [];
  let currLeft = 0;
  for (let i = 0; i < text.length; i++) {
    measurementDiv.textContent = text[i]; 
    const width = measurementDiv.getBoundingClientRect().width;
    measuredChars.push({c: text[i], width: width, left: currLeft});
    currLeft += width;
  }

  GLOBAL.libDragonLolTextRunAnimation({
    target: target,
    text: text,
    label: "Client rect + emoji + for loop",
    measuredChars: measuredChars
  });
}(window))
</script>

<div id="lol_dragon_text_emoji_fail">Hehe 😅 Funker det?</div>

<script type="text/javascript">
(function (GLOBAL) {
  const measurementDiv = GLOBAL.libDragonLolTextMeasurementDiv;
  
  const target = document.getElementById("lol_dragon_text_emoji_fail");
  const text = target.textContent;
  
  measurementDiv.textContent = text;
  
  const range = document.createRange();
  const textNode = measurementDiv.firstChild;
  const measuredChars = [];
  for (let i = 0; i < text.length; i++) {
    range.setStart(textNode, i);
    range.setEnd(textNode, i + 1);

    const rect = range.getClientRects()[0];    
    measuredChars.push({c: text[i], width: rect.width, left: rect.x});
  }

  GLOBAL.libDragonLolTextRunAnimation({
    target: target,
    text: text,
    label: "createRange + emoji + for loop",
    measuredChars: measuredChars
  });
}(window))
</script>

Generaltabben her er å gjøre noe så dumt som å iterere en collection med en for-loop. Lol, jeg er dust. JavaScript liker helst å tenkte på tekst som bytes. Selve målingen blir sånn passe OK because reasons (avrunding), men uansett.

Men:

<div id="lol_dragon_text_emoji_lol">Hehe 😅 Funker det?</div>

<script type="text/javascript">
(function (GLOBAL) {  
  const target = document.getElementById("lol_dragon_text_emoji_lol");
  const text = target.textContent;
    
  GLOBAL.libDragonLolTextRunAnimation({
    target: target,
    text: text,
    label: "createRange + emoji + @@iterator",
    measuredChars: GLOBAL.libDragonLolTextMeasureProperly(text)
  });
}(window))
</script>

Denne bruker `for (const c of text) { ... }`, som lener seg på `String.prototype["@@iterator"]`. Denne er så lur at den ser på tekst som et knippe bokstaver, ikke et knippe bytes, så dette er den "riktige" måten å plukke bokstaver ut fra en JavaScript-string.

Hvis du tilfeldigvis bruker ClojureScript, så skal du nå få slippe å bruke to timer til å finne ut av dette:

```clojure
(-> str 
    (js-invoke js/Symbol.iterator)
    (es6-iterator-seq))
```

## For the enda mere lulz: 0.1 + 0.2 != 0.3

JavaScript har noe som ligner litt på tall, men de heter egentlig 64-bit floats. Dette gjør at:

```js
0.1 + 0.2 == 0.3
// false
```

Bare for å gjøre arbeidsdagen din ekstra morsom, så gir nettleseren deg sånne tall som det her når den regner ut bredden til tekst:

```js
"F" // 11.25              (kjekt)
"u" // 11.683334350585938 (ikke så kjekt)
"n" // 11.683334350585938 (u og n er like, gøy)
"k" // 10.366668701171875
"e" // 11                 (jepp)
"r" // 8.466659545898438
```

Det betyr at hvis du skal finne ut hvor langt mot høyre en bokstav skal plasseres, er det bare å be om trøbbel å plusse sammen disse bredde-verdiene, for da vil du til slutt treffe på avrundingsfeil dersom du har lange nok tekstlinjer.

```js
 11.683334350585938 + 10.366668701171875 + 8.466659545898438 + ...
// teksten bl i r    s    å       n            n
```

Så la dette være mitt lille tips til deg: range-APIet gir deg en bredde _og_ en X-posisjon for hver eneste bokstav du måler. Bruk denne X-posisjonen når du skal plassere bokstaven, slik at ikke hver bokstav på linja gradvis forskyves litt og litt lengere fra der den hører hjemme. For du er vel ingen [Super Mario 64 speedrunner](https://www.youtube.com/watch?v=9hdFG2GcNuA)?

## Så var det en teksteditor, da

Nå har vi jo bare sett på hvordan vi måler bokstaver. Hva med å faktisk rendre tekst over flere linjer?

I bunn og grunn er jo det ganske enkelt. De fleste tekst-editorer bruker en fin og rolig greedy algoritme som spiser ett og ett ord og lager en ny linje når linja er full. Det får holde. En tekst-editor skal jo helst kunne la deg skrive mere enn 1 bokstav hvert 6. sekund. Det hadde sikkert blitt _sykt sexy_ om man brukte f.eks [Knuth og Plass sin episke algoritme](http://defoe.sourceforge.net/folio/knuth-plass.html) (M.F. Plass setter bokstavene på plass), men det passer best til når du skal lene deg tilbake og se på LaText varme opp CPU-en din mens den rendrer verdens vakreste PDF.

Et par ting skal sies her:

* Vi _måler_ en og en bokstav
* Vi tegner hele linjer som en svær tekst-streng. Ingen vits å lage en `<span>` for hver eneste bokstav
* Målingene brukes til å beregne når en linje skal brekke, og ting som å plassere en blinkende cursor på riktig sted ved bokstavene.

Vi kjører.

<div id="lol_dragin_text_editor_good">AV Wa AV Wa AV Wa AV Wa! What's true of every bug found in the field? It passed the type checker. What else did it do? It passed all the tests. Okay. So now what do you do?</div>

<script type="text/javascript">
(function (GLOBAL) {  
  const target = document.getElementById("lol_dragin_text_editor_good");
    
  GLOBAL.libDragonLolTextEditorOmg({
    label: "Range-basert (bra)",
    target: target,
    textMeasureF: GLOBAL.libDragonLolTextMeasureProperly
  });
}(window))
</script>

Trykk på knappene! Da flyttes cursoren rundt i teksten.

Bare sånn for morro skyld (dette er sånn vi web-utviklere synes er morro), her har du en ødelagt versjon:

<div id="lol_dragin_text_editor_bad">AV Wa AV Wa AV Wa AV Wa! What's true of every bug found in the field? It passed the type checker. What else did it do? It passed all the tests. Okay. So now what do you do?</div>

<script type="text/javascript">
(function (GLOBAL) {  
  const target = document.getElementById("lol_dragin_text_editor_bad");
    
  GLOBAL.libDragonLolTextEditorOmg({
    label: "En og en bokstav (dårlig)",
    target: target,
    textMeasureF: GLOBAL.libDragonLolTextMeasureShitty
  });
}(window))
</script>

Hvor forferdelig er dette? Legg merke til at begge tekst-editorene har en aktiv cursor til enhver tid. Bare for å ødelegge dagen din helt: her har du et input-felt:

<div style="display: flex; justify-content: center; margin: 50px auto;">
<input type="text" style="font-size: 18px;" />
</div>

Aktiver dette input-feltet, mens du observerer at begge de blinkende cursorene i editorene over lever videre som om ingenting har skjedd.

Slik må det altså være. Hver linje i boksen over er en div. Lover, bare sjekk i devtools! Cursoren er en div med en CSS-animasjon som får den til å blinke. Alt er tegnet og posisjonert helt manuelt.

## Hva med de tre milliarder andre tingene?

For å rekke å få ferdig denne bloggposten før småbarna mine flytter hjemmefra, tenkte jeg å la være å reimplementere hele Google Docs.

Det er jo forsåvidt bare å bruke fantasien. Når du har det grunnleggende -- detaljert info om bokstav-proporsjoner -- så er det jo bare å klistre på flere absolutt posisjonerte DIV-er og greier, så har du markering av tekst, cursor som blinker, kursiv og bold, og alt som hører med.

Til slutt vil jeg bare beklage at du nå vet hvordan pølser lages (view source!), og hvor totalt ineffektivt det kan være å lage kjappe GUI-er for nettlesere. Men hva er man ikke villig til å gjøre for litt markedsandeler?

## Hvorfor i alle dager kan jeg dette?

I februar og mars til neste år skal jeg ta meg fri fra jobben for å jobbe med et aldri så lite AS jeg har startet.

Jeg skal lage en CMS!

Hadde jeg vært god på business, hadde jeg hatt en landingsside klar hvor du kunne puttet inn en e-postadresse så jeg kunne irritert deg med dritt i inboksen din. Men du kan jo se frem til CMS-ens svar på iPhone til neste år en gang!

## Hva med contenteditable?

Nei.