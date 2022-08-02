:title 5 små TypeScript-tips
:author olav
:tech [:typescript]
:published 2022-08-02

:blurb

Gitt utgangspunktet "la oss type JS" så er TypeScript ganske supert.
Dette er ikke nødvendigvis et premiss vi trenger godta,
men når man først besøker TS-land hjelper det å ha noen triks for hånden.

:body

Gitt utgangspunktet "la oss type JS" så er TypeScript ganske supert.
Dette er ikke nødvendigvis et premiss vi trenger godta,
men når man først besøker TS-land hjelper det å ha noen triks for hånden.

## 1. Refaktorer med _@ts-expect-error_

Annotasjonen `@ts-ignore` ber TypeScript-kompilatoren ignorere feil på neste linje.
Den alternative annotasjonen
[`@ts-expect-error`](https://www.typescriptlang.org/docs/handbook/release-notes/typescript-3-9.html#-ts-expect-error-comments)
gjør samme jobb på en litt bedre måte:
denne vil i tillegg rope høyt når linjen ikke lenger feiler.

```typescript
// @ts-expect-error
const a: number = "1"; // ✅

// @ts-expect-error
const b: number = 1; // ❌
```

`@ts-expect-error` er spesielt hjelpsom i kodebaser som en gang var JS,
og nå er 80% ferdig konvertert til TS (der 80% av jobben gjenstår).
For å komme helt i land kan man for eksempel:

1. Endre alle gjenstående filnavn fra `.js` til `.ts`,
2. legge til `@ts-expect-error` der det trengs, og
3. sette som mål å fjerne alle de nye annotasjonene.

Ved å løse én `@ts-expect-error` fikses ofte flere følgefeil,
og annotasjonene forteller deg når de kan fjernes.

## 2. Spesifiser strenger med _literal types_

Noen ganger er det ikke nok med en streng; vi trenger også et spesifikt format.
TypeScripts
[_template literal types_](https://www.typescriptlang.org/docs/handbook/2/template-literal-types.html)
lar oss spesifisere formatet vi ønsker i typesystemet,
slik at feil kan oppdages allerede ved kompilering:

```typescript
// Denne typen beskriver en streng som må starte med "/".
type LeadingSlash = `/${string}`;

// Denne funksjonen kan kun kalles med en streng som starter med "/".
function leadingSlashPlease(path: LeadingSlash) {}

leadingSlashPlease("./a"); // ❌
leadingSlashPlease("/b"); // ✅
```

Hvis vi trenger en _runtime_-sjekk kan vi ty til en _type guard_:

```typescript
function hasLeadingSlash(path: string): path is LeadingSlash {
    return path.startsWith("/");
}

leadingSlashPlease(someString); // ❌
if (hasLeadingSlash(someString)) {
    leadingSlashPlease(someString); // ✅
}
```

Hvis vi mot formodning trenger en _exception_ kan vi bruke nøkkelordet `asserts`:

```typescript
function assertLeadingSlash(path: string): asserts path is LeadingSlash {
    if (!path.startsWith("/")) {
        throw new Error(`expected leading slash: "${path}"`);
    }
}

leadingSlashPlease(someString); // ❌
assertLeadingSlash(someString);
leadingSlashPlease(someString); // ✅
```

## 3. Utforsk hjelpsomme hjelpetyper

TypeScript har mange innebygde
[hjelpetyper](https://www.typescriptlang.org/docs/handbook/utility-types.html)
som overraskende ofte er akkurat hva som trengs:

```typescript
// `Readonly` kan sikre at en funksjon ikke muterer sine argumenter.
function sortStrings(list: Readonly<string[]>): string[] {}

// `ReturnType` kan få tak i en type som ikke ble eksportert fra en avhengighet.
type MyReturnType = ReturnType<typeof someLibraryFunction>;

// `Omit` kan forenkle eksisterende typer.
type Unsaved<T> = Omit<T, "id" | "createdAt" | "updatedAt">;
```

Ved hjelp av _conditional types_ kan vi også lage spennende hjelpetyper på egen hånd:

```typescript
// Denne typen beskriver en streng som ikke kan være tom.
type NonEmptyString<T extends string> = T extends "" ? never : T;

// Denne funksjonen kan ikke kalles med en tom streng.
function emptyStringsAreWeird<T extends string>(s: NonEmptyString<T>) {}

emptyStringsAreWeird(""); // ❌
emptyStringsAreWeird("a"); // ✅
```

## 4. Synkroniser navn med _Pick_

Navngiving er vanskelig, så det er flott å kunne sette samme navn på samme ting overalt.

TypeScripts
[`Pick`](https://www.typescriptlang.org/docs/handbook/utility-types.html#picktype-keys)
kan hjelpe oss med å holde navn synkronisert ved å plukke felter fra eksisterende typer:

```typescript
type ServerConfig = {
    hostName: string;
};

// Hva er `name` her egentlig?
function startServer(name: string) {}

// Ah, det er `hostName` fra `ServerConfig`.
function startServer({ hostName }: Pick<ServerConfig, "hostName">) {}
```

## 5. Del opp globale typer

Globale domenetyper er alltid fristende, men ofte skumle saker.
Store produkttyper som brukes mange steder blir fort svekket via valgfrie felter og type-unioner.
Resultatet blir lett vanskelig å forstå. Når har vi hvert felt?

```typescript
type User = {
    id?: number;
    name: string;
    email?: string;
    createdAt?: Date | string;
};
```

Et alternativ er å bruke flere, strengere typer.
[`Pick`](https://www.typescriptlang.org/docs/handbook/utility-types.html#picktype-keys)
og
[`Omit`](https://www.typescriptlang.org/docs/handbook/utility-types.html#omittype-keys)
kan brukes til å synkronisere feltenes navn og undertyper:

```typescript
type UserRow = {
    id: number;
    name: string;
    email: string;
    createdAt: Date;
};

// UserRow bruker `Date`s, men JSON trenger strenger.
type UserResponse = Omit<UserRow, "createdAt"> & { createdAt: string };

// Kun `name` og `email` kan spesifiseres ved `create`.
type CreateUserRequest = Pick<UserRow, "name" | "email">;

// Oppdateringer trenger en `id`, men `email` kan ikke oppdateres.
type UpdateUserRequest = Pick<UserRow, "id" | "name">;
```

Globale domenetyper kan føre til sterk kobling mellom lagene i en applikasjon.
Oppdelte typer tydeliggjør inndelingen og understreker forskjellene mellom hvert lag.
Litt mer å skrive, men potensielt enklere å forstå.
