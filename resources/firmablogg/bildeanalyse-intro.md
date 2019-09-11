:title Introduksjon til bildeanalyse
:author eivind
:tech [:opencv :python]
:published 2019-09-11

:blurb

Bildeanalyse, kunsten å lese informasjon fra bilder, er en artig og utfordrende
del av IT-faget. Man kan gjøre tekstgjenkjenning, bygge automatiserte
industriprosesser, lage [nymotens dartspill](https://ochedart.com/) og en hel del
andre greier. Her beskriver vi en måte man kan komme i gang på og forklarer noen
av de enkleste prinsippene som kan være kjekke å kunne.

:body

Jeg lærte meg grunnleggende bildeanalyse da jeg tok
[mastergrad ved UiO](http://urn.nb.no/URN:NBN:no-25670). Siden
da har jeg hatt lyst til å gjøre noe annet enn hobbyprosjekter med bilder, og
for et par år siden fikk jeg sjansen da vi ble bedt om å hjelpe til med å lage
en ny type dartspill. [Oche-prosjektet](https://ochedart.com/) ble vellykket, og
tilbyr i dag sosial dart i Torggata. Jobben med dette spillet ga mersmak, og i
dette innlegget skal jeg vise deg hvordan du kommer i gang med bildeanalyse.

##Valg av språk og rammeverk

På universitetet gjorde vi bildeanalyse i
[MATLAB](https://se.mathworks.com/products/matlab.html).
Siden jeg hovedsaklig har jobbet med andre språk gjennom karrieren, og er glad i
Open Source, prøvde jeg å finne noe annet enn MATLAB å basere meg på denne gangen.
Da jeg kom over [OpenCV](https://opencv.org/) ble jeg glad. Det er både Open Source
og dessuten et raskt bibliotek med masser av nyttige funksjoner. Jeg fikk en del
trøbbel med JVM-koblingen. Etter å tenkt på om jeg ville dra frem gamle
C/C++-kunnskaper kom jeg over 
[Python-koblingene til OpenCV](https://docs.opencv.org/master/d6/d00/tutorial_py_root.html).
Disse fungerer utmerket. Python er et flott språk som støtter flere paradigmer, og
pga. de tette koblingene til C/C++ er det dessuten lynraskt.
[NumPy-biblioteket](https://docs.opencv.org/master/d6/d00/tutorial_py_root.html)
gir OpenCV god ytelse på Python - og gjør det behagelig å jobbe med. Et bilde i
OpenCV-Python er egentlig bare en NumPy-matrise du kan fikle med så mye du vil.

Så da ble det for min del altså OpenCV og Python. Installasjon neste.

##Installere

Jeg har en MacBook Pro - så da blir dette en slags oppskrift basert på det. Jeg har
kjørt all koden i produksjon på Linux - så det er ikke noe stress om du ikke liker
Mac.

###Python, virtualenv + OpenCV

På min Mac er det noe gamle Python 2-greier installert fra før. Det duger ikke - så
det første jeg gjør på en ny maskin er å installere nyeste Python (denne gir deg
Python 3 tilgjengelig som `python3`):

```bash
brew install python
```

Python er ikke så bra på lokale avhengigheter så man bør sette opp
[Virtualenv](https://virtualenv.pypa.io/en/latest/) for å holde hvert prosjekt
adskilt. Jeg bruker [virtualenvwrapper](https://virtualenvwrapper.readthedocs.io/en/latest/):

```bash
pip install virtualenvwrapper
```

Da er vi klar til å opprette et virtualenv basert på Python 3 - jeg kaller mitt
`opencv-test`:

```bash
mkvirtualenv -p python3 opencv-test
```

For å jobbe med OpenCV i Python funker pakken `opencv-python` fint. Den kommer i
litt forskjellige [varianter](https://pypi.org/project/opencv-python/) - jeg bruker
standard-utgaven her:

```bash
pip install opencv-python
```

Det var det. Fire kommandoer og fiks ferdig klar til å bildeanalysere verden!

##Komme i gang

For å redigere Python-koden bruker jeg [Emacs](https://www.gnu.org/software/emacs/)
med [Elpy-pakken](https://github.com/jorgenschaefer/elpy). Du kan sikkert bruke
favoritt editoren din - de fleste verktøy støtter Python har jeg inntrykk av.

Jeg synes det kan være fint å starte i det små. Først lager vi et bittelite
Python-program som leser inn et bilde og viser det frem på skjermen.

For å komme i gang er det kjekt å ha et bilde. Jeg har lastet ned et bilde av meg
selv - eller det var i hvertfall det første jeg fant på maskinen min. Bildet mitt
heter `eivind.jpg` og ligger på en katalog. I samme katalog oppretter jeg filen
`test.py` og skriver inn litt Python:

```python
import cv2 as cv

img = cv.imread("eivind.jpg", cv.IMREAD_UNCHANGED)

cv.imshow("test", img)
cv.moveWindow("test", 0, 0)
cv.waitKey(0)
cv.destroyAllWindows()
```

Hvis man har det virtualenv vi lagde først aktivt kan man bare kjøre programmet
direkte:

```bash
python test.py
```

Da skal bildet du har valgt dukke opp i et eget vindu på skjermen. Hos meg ser det
sånn ut:

![Emacs med Elpy er utviklingsmiljøet](/images/blogg/ba_intro_1.png)

Disse funksjonene for å vise bilder og flytte vinduer og sånt har jeg egentlig ikke
brukt i produksjonskode - men de er kjekke for å kjapt se resultater under
utvikling.

##Bittelitt videre

Ok. Nå som vi har OpenCV oppe med Python og greier kan vi prøve oss på å gjøre noe
med bildet. Det man typisk gjør i bildeanalyse er å få bildet over i svarthvit og
binært før man begynner å finne smarte ting i det.

Jeg kan gjøre bildet mitt svarthvit:

```python
imgGray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
```

Vi kan lage litt abstrakt kunst av det med en enkel `morphologic close` operasjon:

```python
strEl = cv.getStructuringElement(cv.MORPH_ELLIPSE, (15, 15))
imgClose = cv.morphologyEx(imgGray, cv.MORPH_CLOSE, strEl)
```

Men det man som regel er ute etter er å finne noe fornuftig i bildene. Så jeg kan
prøve meg på en binær thresholding - som gjør at vi sitter igjen med bare hvite
eller svarte pixler. Da gjelder det jo helst at de vi er interessert i er svarte
(underscore i Python betyr bare at jeg ikke bryr meg om den delen av resultatet):

```python
_, imgTH = cv.threshold(imgGray, 0, 255, cv.THRESH_BINARY + cv.THRESH_OTSU)
```

Til slutt kan jeg typisk gjøre noe closing på det binære bildet. Dette gjør man for
å lukke huller og få hele former:

```python
imgTHClose = cv.morphologyEx(imgTH, cv.MORPH_CLOSE, strEl)
```

Så putter jeg alt sammen inn i det fine testprogrammet og åpner resultatene i noen
stygger vinduer så jeg får se (hos meg åpner den alltid vinduene i bakgrunnen - men
det klarer jeg leve med). Full kode hos meg ser slik ut:

```python
import cv2 as cv

img = cv.imread("eivind.jpg", cv.IMREAD_UNCHANGED)

imgGray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)

strEl = cv.getStructuringElement(cv.MORPH_ELLIPSE, (15, 15))
imgClose = cv.morphologyEx(imgGray, cv.MORPH_CLOSE, strEl)

_, imgTH = cv.threshold(imgGray, 0, 255, cv.THRESH_BINARY + cv.THRESH_OTSU)
imgTHClose = cv.morphologyEx(imgTH, cv.MORPH_CLOSE, strEl)

cv.imshow("close", imgClose)
cv.moveWindow("close", 0, 0)

cv.imshow("th", imgTHClose)
cv.moveWindow("th", 0, 400)

cv.waitKey(0)
cv.destroyAllWindows()
```

Og når jeg kjører greiene ser det noe sånn ut på maskina mi - etter at jeg har
flyttet litt på vinduene. Øverst ser vi bildet etter svarthvit + close operasjonen,
nederst ser vi hvordan det ser ut etter svarthvit, binær threshold + close:

![Ferdig eksempel](/images/blogg/ba_intro_2.png)

Dette siste bildet begynner jo å ligne på noe man kunne tatt utgangspunkt i om man
ønsket å analysere fasongen på hodet mitt i profilbildet. Kanskje sammenligne med
andre profiler for å si noe om hvem det er bilde av eller noe i den duren. Da har
man straks noe som ligner litt på bildeanalyse.

Nå er det jo masse konsepter her jeg ikke har forklart. Om du er nysgjerrig kan jeg
anbefale å starte med å lese gjennom OpenCV sin
[Python tutorial](https://docs.opencv.org/master/d6/d00/tutorial_py_root.html). Ting
jeg har brukt i mitt eksempel - med litt enkel forklaring av teorien bak:

- [Getting Started with Images](https://docs.opencv.org/master/dc/d2e/tutorial_py_image_display.html)
- [Changing Colorspaces](https://docs.opencv.org/master/df/d9d/tutorial_py_colorspaces.html)
- [Image Thresholding](https://docs.opencv.org/master/d7/d4d/tutorial_py_thresholding.html)
- [Morphological Transformations](https://docs.opencv.org/master/d9/d61/tutorial_py_morphological_ops.html)

##Klar ferdig gå!

Alright. Det var det for denne gang. Nå bør du være i stand til å lese inn bilder og
starte jobben med å få noe fornuftig ut av dem. Jeg kommer tilbake med fler posts
der jeg lover å vise frem litt mer matnyttige greier enn tullebilder av meg selv :)

Ta gjerne kontakt om du vil jeg skal skrive mer - eller om jeg har sagt noe riv
ruskende gæli her - eller du har tips til noe som kan være enklere/bedre enn det
jeg har gjort. Hadde vært interessant å høre litt om hvilke språk og rammeverk
andre bruker til bildeanalyse-arbeid..
