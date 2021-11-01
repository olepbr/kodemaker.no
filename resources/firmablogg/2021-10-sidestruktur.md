:title Sidestruktur
:author odin
:tech [:frontend]
:blurb

Ofte så lager vi løsninger med en fast visuell struktur. Du har for eksempel en header med en meny, en sidespesifikk kropp og 
en footer. Hva gjør vi hvis noen sider trenger en annen struktur?

:body

Ofte så lager vi løsninger med en fast visuell struktur. Du har for eksempel en header med en meny, en sidespesifikk kropp og 
en footer. Hva gjør vi hvis noen sider trenger en annen struktur?

# Problemet med fast sidestruktur 
For å illustrere problemet så har jeg laget et [eksempel](https://github.com/Odinodin/react-wrap-or-not) 
på en typisk React-applikasjon. Appen har en header med en meny, en kropp, og en footer. Den har tre forskjellige sider; Hjem, kattesiden og hundesiden. 
 
![Sidestruktur](/images/blogg/sidestruktur.jpg)

Jeg har brukt React Router for routing siden de fleste sikkert kjenner til det.

```
const FastApp = () => (
  <Router>
    <SideStruktur>
      <Switch>
        <Route path="/katt"><Kattesiden/></Route>
        <Route path="/hund"><Hundesiden/></Route>
        <Route path="/"><Hjemsiden/></Route>
      </Switch>
    </SideStruktur>
  </Router>
);
```

Sidestrukturen ser slik ut:

```
const SideStruktur = (props: { children: React.ReactNode }) => (
  <div className="app">
    <div className="struktur">
      <Header/>
      <div className="innhold">
        {props.children}
      </div>
      <Footer/>
    </div>
  </div>
);
```

Hver konkrete side blir sendt inn som `children`, og trenger bare å tenke på seg selv. 

## Dette ser jo helt greit ut, så hva er problemet?

Utfordringen oppstår den dagen produkteier banker på døra.

> Vi ønsker å endre farge på footeren, men *bare* på hundesiden. 

Hundesiden selv har ingen måte å påvirke footeren på siden den bor i `SideStruktur`-komponenten. 

Hva kan vi gjøre? Vi kan jo bare sjekke `location` i `SideStruktur`!

```
const SideStruktur = (props: { children: React.ReactNode }) => {
  const location = useLocation();
  const fotFarge = location.pathname === "/hund" ? "pink" : "lightgreen";

  return (
    <div className="app">
      <div className="struktur">
        <Hode/>
        <div className="innhold">
          {props.children}
        </div>
        <Fot bakgrunn={fotFarge}/>
      </div>
    </div>
  )
};
```
Hundesiden har nå skitnet til SideStruktur-komponenten. Det er et hack, men ikke noe vi mister nattesøvnen over.

Helt til du får nok en beskjed fra produkteier. 

> Katteeiere liker å se på kattebilder i fullskjerm

La oss brette opp ermene.

![Sidestruktur](/images/blogg/fyll_skjermen.jpg)

```
const SideStruktur = (props: { children: React.ReactNode }) => {
  const location = useLocation();
  const fotFarge = location.pathname === "/hund" ? "pink" : "lightgreen";

  const [fullskjerm, setFullskjerm] = useState(false);
  const visFullskjermKnapp = location.pathname === "/katt";
  const fullskjermKnapp = <button onClick={() => setFullskjerm(!fullskjerm)}>Endre Fullskjerm</button>;

  if (fullskjerm && visFullskjermKnapp) {
    return (
      <div>
        {fullskjermKnapp}
        {props.children}
      </div>
    );
  }

  return (
    <div className="app">
      <div className="struktur">
        <Hode/>
        <div className="innhold">
          {visFullskjermKnapp && fullskjermKnapp}
          {props.children}
        </div>
        <Fot bakgrunn={fotFarge}/>
      </div>
    </div>
  )
};
``` 
 
Dette er ikke noe gøy lenger. Vi får håpe det ikke kommer flere endringsønsker på sidestrukturen. 
Her smører vi sidespesifikk funksjonalitet ut over flere steder i koden. Det begynner å stinke. 

# Blanke ark
Hva hvis hver side var helt blank, uten noe struktur fra oven? 

![Sidestruktur](/images/blogg/fast_til_fri.png)

Her er koden for `FriApp`-komponenten. Det kommer ingen struktur fra oven. Hver side er ansvarlig for å legge på strukturen selv.

```
const FriApp = () => (
  <Router>
    <Switch>
      <Route path="/katt"><Kattesiden/></Route>
      <Route path="/hund"><Hundesiden/></Route>
      <Route path="/"><Hjemsiden/></Route>
    </Switch>
  </Router>
);
``` 
Hvordan kan vi få sidene som skal være like til å ha samme struktur uten å kopiere masse kode? Vi kan lage oss en 
komponent for å håndtere standardsidene som har hode og fot:

```
const StandardSide = (props: { children: React.ReactNode, bakgrunn?: string }) => {
  const { bakgrunn = "lightgreen", children} = props;

  return (
    <div className="app">
      <div className="struktur">
        <Hode/>
        <div className="innhold">
          {children}
        </div>
        <Fot bakgrunn={bakgrunn}/>
      </div>
    </div>
  )
};
```

Å fargelegge footeren på hundesiden blir da:

```
const Hundesiden = () => (
  <StandardSide bakgrunn={"pink"}>
    <h1>HUNDER</h1>
  </StandardSide>
);
```

Å støtte fullskjerm påvirker kun kattesiden:

```
const Kattesiden = () => {
  const [fullskjerm, setFullskjerm] = useState(false);
  const fullskjermKnapp = <button onClick={() => setFullskjerm(!fullskjerm)}>Endre Fullskjerm</button>

  const kropp = <div>
    {fullskjermKnapp}
    <h1>KATTESIDEN</h1>
  </div>

  if (fullskjerm) {
    return kropp
  } else {
    return <StandardSide>{kropp}</StandardSide>
  }
};
```

# Oppsummering
Ulempen er at det blir litt mer kode på hver side, men det er en lav pris å betale. Ved å la hver side bestemme selv
så oppnår du følgende:

* Mer forståelig kode. Du kan gå til en hvilken som helst side og finne koden til *hele* siden
* Lettere å tilpasse hver side, uten å påvirke andre
 
