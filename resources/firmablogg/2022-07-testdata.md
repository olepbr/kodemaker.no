:title Testdatatips
:author odin
:tech [:testing :typescript]
:published 2022-08-10

:blurb

Uansett hvilket språk du bruker, så trenger man testdata når man skriver tester. Her kommer noen tips til hvordan man
kan gjøre det lettere for seg selv, med eksempler i TypeScript.

:body

Uansett hvilket språk du bruker, så trenger man testdata når man skriver tester. Her kommer noen tips til hvordan man
kan gjøre det lettere for seg selv, med eksempler i TypeScript.

## Hva er problemet med testdata?

Det er ikke lett å skrive gode tester. Et kritisk aspekt er å kunne forstå hva testen handler om. Dette er viktig når
testen feiler og du lurer på hvorfor.

En stor kilde til forvirring er datasettet som kreves for å kjøre testkoden. Hvordan vet man hva som er viktig for
testen, og hva som bare er fyllmasse for å oppfylle kravet til grensesnittet?

## Et eksempel

La oss si vi har en `user.ts` med følgende kode som vi ønsker å teste:

```typescript
const usersByEmail: Record<string, IUser> = {};

export const addUser = (user: IUser) => {
  if (usersByEmail[user.email]) throw new Error("Email must be unique")
  usersByEmail[user.email] = user;
}

export const getUserByEmail = (email: string) => {
  return usersByEmail[email];
}
```

En `IUser` har en `IAddress` og en liste med `IPermission`.

Vi skriver en test for å sjekke at `addUser` oppdaterer `usersByEmail`.

```typescript
test("kan legge til en bruker", () => {
  // Fyller ut alt manuelt, vanskelig å se hva som er essensielt for testen
  const newUser: IUser = {
    email: "test@example.com",
    name: "Testing",
    age: 10,
    address: { country: "no", zip: 123, street: "Main street 45" },
    created: new Date(),
    permissions: [{ id: "somePermission", description: "Don't really care in this test" }]
  };
  addUser(newUser)

  const result = getUserByEmail("test@example.com");
  expect(newUser).toEqual(result);
})
```

Testen oppretter en ny bruker, og verifiserer at vi kan hente ut den samme brukeren igjen. Den essensielle egenskapen
til brukeren er eposten. Resten er bare støy. Dessverre må vi ha de med for å blidgjøre typesjekkeren.

## Et hack

Vi kan prøve oss med et tjuvtriks for å lure kompilatoren:

```typescript
test("et tjuvtriks", () => {
  // Cast til IUser 
  const newUser = { email: "fake@example.com" } as IUser;
  addUser(newUser);

  const result = getUserByEmail("fake@example.com");
  expect(newUser).toEqual(result);
})
```

Dette ble mer lesbart. Her er det tydelig at det bare er eposten til brukeren som er viktig. Å caste derimot, kan fort
føre til at vi lurer oss selv. Når vi først bruker TypeScript og har tatt oss bryet med å definere typer, så ønsker vi å
få mest mulig igjen for den investeringen.

## Hva kan vi gjøre?

Vi lager oss noen hjelpefunksjoner for å opprette brukere. `user` returnerer en gyldig `IUser`, med
standardverdier for hvert felt. Funksjonen tar en `Partial<IUser>` som argument, som blir spread'et på returverdien. Det
gjør at vi kan overstyre hva vi vil fra testene våre.

```typescript
export const permission = (overrides: Partial<IPermission>): IPermission => {
  return {
    id: "defaultPermission",
    description: "A default permission",
    ...overrides
  }
}

export const address = (overrides: Partial<IAddress>): IAddress => {
  return {
    zip: 123,
    street: "Default street 1",
    country: "no",
    ...overrides
  }
}

export const user = (overrides: Partial<IUser>): IUser => {
  return {
    name: "Default name",
    age: 1,
    address: address({}),
    created: new Date(2020, 10, 8),
    email: "default@default.com",
    permissions: [permission({})],
    ...overrides
  }
}
```

Hvis vi ønsker å opprette en bruker med en adresse i Sverige, så kan vi gjøre slik:

```typescript
user({ email: "sven@sverige.se", address: address({ country: "se" }) })
```

Vi kan nå skrive testen vår slik:

```typescript
test("kan lage user med en hjelpefunksjon", () => {
  const newUser = user({ email: "help@example.com" })
  addUser(newUser);

  const result = getUserByEmail("help@example.com");
  expect(newUser).toEqual(result);
})
```

Lesbart uten tjuvtriks! Jo mer komplisert domenet ditt er, jo kjekkere er disse komponerbare hjelpefunksjonene.
En bonus er at de gjør det lettere å utvikle koden videre. Når den endrer seg så kan man tilpasse hjelpefunksjonene ett
sted, istedenfor å måtte oppdatere alle testene når en user får et nytt felt.

## Navngitte variabler

I tillegg til hjelpefunksjonene over så kan man også lage seg noen navngitte testdata-variabler. Det
kan være varianter av testdata som er semantisk meningsfulle. Du kan gjerne benytte deg av hjelpefunksjonene for å
definere disse. Eksempelvis så kan det være en `adminUser`
eller `addressInTimezoneWithDaylightSaving`.

I eksemplet under så bruker vi to navngitte `permissions`.

```typescript
test("can use named test data", () => {
  const newUser = user({ email: "bad@example.com", permissions: [permissionToRead, permissionToWrite] })
  addUser(newUser);

  banUser("bad@example.com");

  const result = getUserByEmail("bad@example.com");
  expect(result.permissions).toEqual([])
})
```

## En advarsel

En potensiell fallgruve med delte testdatafunksjoner er at de kan lage skjulte avhengigheter mellom testene. Man må
tenke seg godt om når man definerer standardverdier. Det er ikke nødvendigvis lett å skjønne hva som er greit å endre på
uten å ødelegge for noen tester. I verste fall kan en testdataendring føre til at tester ikke lenger verifiserer det du
ønsket, men
fortsatt kjører grønt.

For de ekstra interesserte så ligger kildekoden til eksemplene
over [her](https://github.com/Odinodin/example-testdata-ts)


