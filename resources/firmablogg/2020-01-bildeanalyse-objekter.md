:title Bildeanalyse #3: Objekter
:author eivind
:tech [:opencv :python :bildeanalyse]
:published 2020-01-15

:blurb

Her kommer del 3 i bloggserien min om bildeanalyse. Denne gangen er det deteksjon av
objekter i et bilde som er temaet. Først bruker vi noen geometri-triks for å finne
rett utsnitt av bildet - så plukker vi ut objektene, og finner egenskaper ved disse
som lar oss si noe om hva det er vi ser på.

:body

I min [forrige post](/blogg/2019-09-bildeanalyse-linjer/) så vi på hvordan vi
kan finne linjer i et bilde. Dette brukte vi for å finne kantene på et bord, og
tegnet inn resultatet i originalbildet. Vi har altså nå et bord hvor vi vet hvor
kantene går - og skal jobbe videre med dette for å finne ut noe om hva som er på
bordet:

![4 linjer](/images/blogg/ba_linjer_fire.png)

Vi må lage oss et utsnitt basert på kantene vi har funnet. Så skal vi se på området
inni dette utsnittet for å prøve å analysere oss frem til hvor mange ting som ligger
på bordet, og hvilke egenskaper disse har.

## Utsnitt basert på linjer

Når man driver med linjer og punkter blir det fort en del funksjoner som minner om
mattetimene fra skolen. Funksjonen som tegner linjene i forrige post regner egentlig
bare ut endepunktene for linjene - og tegner streker mellom dem. `HoughLines` gir oss
en liste av vinkler (`theta`) og avstander (`rho`) - som vi kan bruke for å finne to
punkter i hver sin ende av linjen utenfor bildet:

```python
def findEndPoints(rho, theta):
  a = np.cos(theta)
  b = np.sin(theta)
  x0 = a * rho
  y0 = b * rho
  # Legg til/trekk fra 2000 piksler for å få et punkt utenfor bildet
  x1 = int(x0 + 2000 * -b)
  y1 = int(y0 + 2000 * a)
  x2 = int(x0 - 2000 * -b)
  y2 = int(y0 - 2000 * a)

  return (x1, y1), (x2, y2)
```

Jeg ønsker å finne skjæringspunktene mellom linjene - som da burde tilsvare hjørnene
på det fine kjøkkenbordet. Etter litt søking på internet fant jeg frem til en funksjon
som representerer linjen som en vektor - og en annen funksjon som finner krysspunktene
mellom to slike linjer. Jeg slang på noe maks-verdier på krysspunkt-sjekken for å være
sikker på at vi bare plukker opp punkter som ligger innenfor den synlige delen av
bildet:

```python
def createLine(p1, p2):
  A = (p1[1] - p2[1])
  B = (p2[0] - p1[0])
  C = (p1[0] * p2[1] - p2[0] * p1[1])
  print("p1: {} p2: {}".format(p1, p2))
  print("A: {} B: {} C: {}".format(A, B, C))
  return A, B, -C

def intersection(L1, L2, xMax, yMax):
  D = L1[0] * L2[1] - L1[1] * L2[0]
  Dx = L1[2] * L2[1] - L1[1] * L2[2]
  Dy = L1[0] * L2[2] - L1[2] * L2[0]
  if D != 0:
    x = Dx / D
    y = Dy / D
    if 0 <= x <= xMax and 0 <= y <= yMax: 
      return int(x), int(y)
    else:
      return False
  else:
    return False
```

Jeg endrer funksjonen som finner linjer i bildet - som ble skrevet i forrige post - til å
returnere linjene som par av endepunkter og slike vektor-linjer:

```python
...
    if(uniqueLines.get(roundedLine, None) is None):
      endPoints = findEndPoints(rho, theta)
      uniqueLines[roundedLine] = endPoints, createLine(*endPoints)
...
```

Ok - så med disse endringene og de nye funksjonene kan vi lage en funksjon som finner
alle bordhjørnene våre:

```python
def findIntersections(img, lines):
  height, width, _ = img.shape
  uniqueIsects = set()
  for (_, line) in lines:
    for (_, otherLine) in lines:
      if line != otherLine:
        isect = intersection(line, otherLine, width, height)
        if isect:
          uniqueIsects.add(isect)

  return sorted(uniqueIsects)
```

Med disse byggeklossene kan vi nå lese inn bildet, finne linjer og krysningspunkter.
Punktene kobler vi sammen til en figur som vi kan trekke fra det originale bildet - slik
at vi sitter igjen med et bilde som kun viser bordet. Alt det rundt er maskert bort:

```python
# Les inn bildet
img = cv.imread("bordet.jpg", cv.IMREAD_UNCHANGED)

# Finn bildets dimensjoner
height, width, _ = img.shape

# Finn linjene i bildet
lines = findLines(img)

# Finn krysning mellom linjene
isects = findIntersections(img, lines)

# Lag et nytt bilde med samme dimensjoner
mask = np.zeros([height, width], dtype = np.uint8)

# Tegn mellom krysningspunktene
cv.fillPoly(mask, np.array([isects], np.int32), 255)

# Bruk det nye bildet som maske
maskedImg = cv.bitwise_and(img, img, mask=mask)
```

Vi er jo bare interessert i å se på tingene som faktisk ligger på bordet. Ikke stoler og
vinduskarm og sånt som er rundt. Så dette blir da utgangspunktet vårt når vi skal finne
tingene som står på bordet:

![Maskert bord](/images/blogg/ba_objekter_mask.png)

## Finne objekter i bildet - contour detection

For å finne objekter bruker vi noe som kalles
[contour detection](https://docs.opencv.org/master/df/d0d/tutorial_find_contours.html).
Da trenger vi først å gjøre om bildet vårt til svart-hvit og så et binært bilde. Dette
skrev jeg om i forrige post også - så vi bruker bare samme greiene om igjen. I tillegg
gjør vi noe opprydding for å luke vekk selve kanten på bordet og en del annen støy:

```python
  imgGray = cv.cvtColor(maskedImg, cv.COLOR_BGR2GRAY)
  edges = cv.Canny(imgGray, 20, 150)

  strEl = cv.getStructuringElement(cv.MORPH_ELLIPSE, (30, 30))
  closed = cv.morphologyEx(edges, cv.MORPH_CLOSE, strEl)
  strEl2 = cv.getStructuringElement(cv.MORPH_ELLIPSE, (5, 5))
  opened = cv.morphologyEx(closed, cv.MORPH_OPEN, strEl2)
```

Da sitter vi med vårt binære kant-bilde (`edges` i koden) - og det oppryddete bildet vi
skal bruke til å finne objektene (`opened` i koden). De ser slik ut ved siden av
hverandre:

![Kanter og svart-hvit objekter](/images/blogg/ba_objekter_th.png)

Et objekt eller 'contour' er jo egentlig bare en samling sammenhengende piksler. Som vi
ser på bildet over har vi fortsatt med en del smårusk - men disse er opplagt mye mindre
enn de store objektene vi har lyst å finne. Så kanskje vi bare kan luke dem vekk på
størrelse.

For å finne contours i bildet bruker vi funksjonen `findContours`. Med `contourArea` kan
vi måle arealet til et objekt i antall piksler. Og da er det enkelt å lage en liste med
bare de objektene som er større enn 1000 piksler:

```python
  cnts, hierarchy = cv.findContours(opened, cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)
  largeCnts = [cnt for cnt in cnts if cv.contourArea(cnt) > 1000]
```

OpenCV har også en funksjon `drawContours` som kan tegne de på et bilde. Vi bruker denne
for å markere på det originale bildet objektene vi har funnet:

```python
  cv.drawContours(img, largeCnts, -1, (255, 0, 0), 2, cv.LINE_AA)
```

Og da er vi vel egentlig i mål. Ambisjonen min var egentlig bare å lage et program som
kunne finne ut om kjøkkenbordet var tomt eller ikke. Og denne her svarer jo fint på det
spørsmålet - med fine blå kanter rundt de tingene vi fant:

![Bord med objekter markert](/images/blogg/ba_objekter_resultat.png)

## Konklusjon

Gjennom 3 forskjellige poster har jeg nå vist litt av hva man kan gjøre med Python og
OpenCV. Bibliotekene er som tidligere nevnt superraske - og kan gjøre ganske avanserte
ting. Vi har brukt denne typen teknologi for å lage bildeanalysen til dart-sjappa
[Oche](https://ochedart.com/) - og vil gjerne jobbe med mer bildegreier. Så ta kontakt om
du vil ha oss med på laget!
