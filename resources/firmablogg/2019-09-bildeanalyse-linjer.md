:title Bildeanalyse #2: Linjer
:author eivind
:tech [:opencv :python :bildeanalyse]
:published 2019-09-18

:blurb

Dette er del 2 i min lille bloggserie om bildeanalyse. Her ser vi videre på en vanlig
utfordring - hvordan man finner linjer i et bilde. Dette er nyttig på en rekke
områder. Vi går gjennom grunnprinsippene for å finne linjer, og så viser jeg frem en
del små triks jeg har funnet ut at kan være fine når man driver med OpenCV og
linjer i bilder.

:body

I [forrige post](https://www.kodemaker.no/blogg/2019-09-bildeanalyse-intro/) gikk
jeg gjennom en kjapp introduksjon til bildeanalyse - og viste hvordan man kan komme
i gang med Python og [OpenCV](https://opencv.org/). Vi gikk gjennom hvordan man leser
inn og viser frem bilder, konverterer til svarthvit og binært, samt hvordan man kan
gjøre en 'morphological close' for å lukke huller i et bilde. Nå er det på tide å
gjøre noe mer fornuftig - prøve å få ut nyttig informasjon fra et bilde.

En av de mest grunnleggende problemene i bildeanalyse er det å finne linjer i et
bilde. Det kan for eksempel dreie seg om å finne igjen omrisset av et
registreringsskilt på et bilde av en bil, vinkelen på et hustak eller hva som helst
annet som dreier seg om ting som utgjør relativt rette streker på et bilde. Jeg har
for eksempel brukt det til å kjenne igjen de forskjellige sektorene på en dartskive.

## Dagens eksempel

Jeg har et kjøkkenbord som ser veldig fint ut når det er ryddig. Dagens noe teoretiske
eksempel går ut på å bygge et system som kan ta bilde av dette kjøkkenbordet og finne
ut om bordet er tomt eller fullt opp av forskjellige gjenstander. Jeg har tatt et
bilde vi kan bruke som utgangspunkt:

![Bilde av kjøkkenbordet](/images/blogg/ba_linjer_bordet.png)

Som vi ser er ikke bordet helt ryddig, men det er ikke så farlig. Vi ser også at
bildet er tatt fra en vinkel som gjør at bordet egentlig ikke ser helt firkantet ut -
og vi har fått med masse 'rot' i form av stoler, vinduskarm og gulv. Utfordringen blir
her i første omgang å identifisere selve bordflaten. Jeg er jo bare interessert i det
som befinner seg på bordet.

En observasjon jeg gjør meg er at kantene på bordet ser ut til å utgjøre ganske
distinkte linjer i bildet. Så planen min er altså å finne disse linjene og gjøre noe
smart som avgrenser området vårt basert på dette.

## Hough Transform - finne linjer

En vanlig måte å finne linjer i bilder er å bruke
[Hough Transform](https://en.wikipedia.org/wiki/Hough_transform). Det er en relativt
enkel, men smart, måte å bruke matematikk på bilder på. Man konverterer punktene i
bildet til polarkoordinater og lager en slags stemmeordning som finner punkter som
ligger innenfor en ønsket klasse - for eksempel linjer. Den kan også brukes til å
finne sirkler og andre fasonger. Selve teorien er godt forklart i
[Wikipedia-artikkelen](https://en.wikipedia.org/wiki/Hough_transform).

### Forarbeid - klargjøre bildet

OpenCV sin [implementasjon av Hough Transform](https://docs.opencv.org/master/d6/d10/tutorial_py_houghlines.html)
krever et binært bilde som input. Så vi starter med å lese inn bildet og konvertere
det til svarthvit:

```python
img = cv.imread("bordet.jpg", cv.IMREAD_UNCHANGED)

imgGray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
```

Deretter kan vi enten bruke Thresholding som vi gjorde i
[introduksjonen](https://www.kodemaker.no/blogg/2019-09-bildeanalyse-intro/), eller
en annen teknikk for kantgjenkjennelse. Jeg prøver meg på det som heter
[Canny Edge Detection](https://docs.opencv.org/master/da/d22/tutorial_py_canny.html).
Den tar to parametre i tillegg til selve bildet - `minVal` og `maxVal`. Alle gråtoner
over `maxVal` regnes som kant - alle under `minVal` utelukkes. Verdiene i midten
inkluderes om de er forbundet med sikre kanter. Jeg prøvde meg litt frem og fant ut
at `minVal=20` og `maxVal=150` gir ok resultater:

```python
edges = cv.Canny(imgGray, 20, 150)
```

Da sitter vi igjen med et bilde som gir et omriss av de viktigste formene. Ganske kult
i grunn. Dette blir da utgangspunktet vårt for å finne linjer:

![Canny Edge Detection](/images/blogg/ba_linjer_canny.png)

### Deteksjon av unike linjer

Nå er vi endelig klare til å prøve å finne disse linjene. Det er jo mange linjer i
bildet, men vi vet en del om de vi er ute etter så vi får prøve å tilpasse etter det.
Det første vi gjør er å bruke funksjonen `HoughLines` - som gir oss en liste av
`(rho, theta)` verdier tilbake. Dette er altså vinkel i radianer `theta` og avstand i
pixler `rho` (se [teori om Hough Transform](https://docs.opencv.org/master/d6/d10/tutorial_py_houghlines.html)):

```python
import numpy as np
...

lines = cv.HoughLines(edges, 1, np.pi / 720, 200)
```

Jeg har laget en liten funksjon for å tegne linjene vi har funnet i rødt tilbake på
det originale bildet. Det er kjekt for å se om vi er på rett spor:

```python
def drawLines(img, lines):
  for (rho, theta) in lines:
    a = np.cos(theta)
    b = np.sin(theta)
    x0 = a * rho
    y0 = b * rho
    x1 = int(x0 + 2000 * -b)
    y1 = int(y0 + 2000 * a)
    x2 = int(x0 - 2000 * -b)
    y2 = int(y0 - 2000 * a)
    cv.line(img, (x1, y1), (x2, y2), (0, 0, 255), 2)

...
drawLines(img, lines)
```

Vi har funnet masse forskjellige linjer! De ser ganske riktige ut - vi har funnet
alle de 4 vi er ute etter, og en hel del vi strengt tatt ikke trenger:

![56 linjer](/images/blogg/ba_linjer_mange.png)

Man kan fintune parametrene til `HoughLines` for å redusere antallet, men jeg
mistenker at vi fortsatt vil sitte igjen med duplikater. Derfor synes jeg det funker
bra å lage seg en funksjon som gjør noe smart for å luke vekk duplikater. Vi vet jo
at vi bare er interessert i en linje på hvert sted - så jeg runder av både `rho` og
`theta` og bruker en Python dictionary for å passe på at vi kun tar vare på en:

```python
lines = cv.HoughLines(edges, 1, np.pi / 720, 200)
print(f"Found {len(lines)} lines!")
uniqueLines = {}

for line in lines:
  rho, theta = line[0]

  roundedTheta = round(theta * 2) / 2
  roundedRho = round(rho / 500)
  roundedLine = (roundedRho, roundedTheta)

  if(uniqueLines.get(roundedLine, None) is None):
    uniqueLines[roundedLine] = (rho, theta)

print(f"Rounded to {len(uniqueLines)} lines")
return uniqueLines.values()
```

Avhengig av hvor mye man vet om linjene man ønsker å finne går det an å finne de som
ligger nærmest opp til et sett kjente verdier også. Men her ser det ut som den enkle
øvelsen med avrunding har gjort susen:

![4 linjer](/images/blogg/ba_linjer_fire.png)

Jeg slang på litt printing også - for å være sikker på at vi virkelig bare har 4
linjer igjen. Ser ut som vi hadde litt flaks med den avrundingen. Ofte vil man nok
oppleve at man må jobbe litt mer for å plukke ut kun de linjene man faktisk vil ha.

## Klar for å finne objekter

Nå som vi har funnet linjene som utgjør kanten på bordet har vi et perfekt
utgangspunkt når vi skal sjekke om bordet er tomt eller ikke. Og det er et fint tema
for neste post :)

Her er det komplette testprogrammet jeg har benyttet meg av i dag. Jeg har delt det
inn i litt forskjellige funksjoner. `resize` er en hjelpefunksjon som endrer
størrelsen på bildet for å passe på skjermen, `findLines` finner de unike linjene,
`drawLines` tegner et sett linjer på et bilde og `run` knytter hele greia sammen:

```python
import cv2 as cv
import numpy as np

def resize(img, frmt=0.5):
  if frmt != 1:
    return cv.resize(img, None, fx=frmt, fy=frmt, interpolation=cv.INTER_CUBIC)
  else:
    return img

def findLines(img):
  imgGray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
  edges = cv.Canny(imgGray, 20, 150)
  lines = cv.HoughLines(edges, 1, np.pi / 720, 200)
  print(f"Found {len(lines)} lines!")
  uniqueLines = {}

  for line in lines:
    rho, theta = line[0]

    roundedTheta = round(theta * 2) / 2
    roundedRho = round(rho / 500)
    roundedLine = (roundedRho, roundedTheta)

    if(uniqueLines.get(roundedLine, None) is None):
      uniqueLines[roundedLine] = (rho, theta)

  print(f"Rounded to {len(uniqueLines)} lines")
  return uniqueLines.values()

def drawLines(img, lines):
  for (rho, theta) in lines:
    a = np.cos(theta)
    b = np.sin(theta)
    x0 = a * rho
    y0 = b * rho
    x1 = int(x0 + 2000 * -b)
    y1 = int(y0 + 2000 * a)
    x2 = int(x0 - 2000 * -b)
    y2 = int(y0 - 2000 * a)
    cv.line(img, (x1, y1), (x2, y2), (0, 0, 255), 2)

def run():
  img = cv.imread("bordet.jpg", cv.IMREAD_UNCHANGED)
  lines = findLines(img)
  drawLines(img, lines)
  return img

img = run()

cv.imshow("bord", resize(img))
cv.moveWindow("bord", 0, 0)
cv.waitKey(0)
cv.destroyAllWindows()
```
