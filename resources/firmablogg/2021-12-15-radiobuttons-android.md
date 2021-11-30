:title Radio buttons for Android
:author olga
:tech [:android]
:published 2021-12-01

:blurb
Hvorfor i alle dager sjekker vi på radio button checked id for å finne ut av hvordan verdien av den skal representeres i koden? Det virker litt for manuelt og tungvint. Er det mulig å få til at radioknapper holder på den informasjonen selv, slik for eksempel en HTML radio button gjør?

:body

Jeg har vært på flere app-prosjekter uten å bli eksponert for radio buttons. Mulig det ikke er så utbredt i app-verden, eller at designere på de prosjektene ikke syntes de var noe særlig. Uansett, nå går jeg gjennom forberedelseskurs for Android-sertifisering, og en av oppgavene har radio buttons, yay! Ved å følge instruksjonene i [oppgaven](https://developer.android.com/courses/pathways/android-basics-kotlin-unit-2-pathway-1) lager man en enkel tip calculator app som ser sånn ut: 

<img style="width: 100%; max-width: 600px" src="/images/blogg/tip-calculator.png" alt="tip calculator" />


Vi lager en RadioGroup inne i `activity_main.xml`, og legger på RadioButtons - so far so good. Deretter går til `onCreate` i `MainActivity.kt`, legger på en click listener for calculate knappen. Derfra får vi tak i `checkedRadioButtonId`, og legger på en `when` statement for hva slags id som er valgt: 

```kotlin
val checkedId = findViewById<RadioGroup>(R.id.tip_options).checkedRadioButtonId
val tipPercentage = when (checkedId) {
    R.id.option_twenty_percent -> 0.20
    R.id.option_eighteen_percent -> 0.18
    else -> 0.15
}
```

Wait.. what? 

Her itererer vi over alle mulige id'ene for radioknapper i gruppen og manuelt setter en verdi som `checkedRadioButton` representerer - altså prosent. Dette virker tungvint. Hvorfor sjekker vi ikke bare på verdien til den valgte radioknappen? Går inn i layout editor og trippeltsjekker alle attributter på radio button for å finne `value` eller lignende jeg kan sette. Den finnes ikke?
Mulig jeg har vært for lenge på web-prosjekter. Hvorfor har ikke radio buttons mulighet for å sette en verdi? Nei, dette var irriterende, må jo ha en `value` man kan sende inn på en radio button?! 

## Custom radio button
Løsningen her ble å lage en custom radio button. 

```kotlin
class CustomRadioButton(context: Context?, attrs: AttributeSet?) :
    AppCompatRadioButton(context, attrs) {
}
```

Foreløpig speiler den bare den eksisterende implementasjonen av vanlig radio button. Neste steg er å få den til å ha `value` som attributt. Under `values` mappen, legger jeg inn en ny fil `attrs.xml` og deklarerer en ny `value` som en string attributt, knyttet til styleables for vår nye CustomRadioButton.

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="CustomRadioButton">
        <attr name="value" format="string" />
    </declare-styleable>
</resources>
```
og whoosh, nå kan vi sette verdi på custom radio button: 

```xml
<com.example.tiptime.CustomRadioButton
            android:id="@+id/option_20_percent"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/amazing_20"
            app:value="20"/>
```

Dette var gøy! Men, hvordan kan vi nå få tak i verdien på radio button fra koden? La oss teste direkte i `MainActivity.kt`: 

```kotlin
    val checkedId = findViewById<RadioGroup>(R.id.tip_options).checkedRadioButtonId
    val checked = findViewById<CustomRadioButton>(checkedId)
    val checkedValue = checked.value
```

Det gikk ikke, vi får kompileringsfeil og den banner på oss med `Unresolved reference: value`. Vi må eksponere `value` på noe vis. 

Tilbake i `CustomRadioButton.kt`, utvider klassen med:

```kotlin
class CustomRadioButton(context: Context?, attrs: AttributeSet?) :
    AppCompatRadioButton(context, attrs) {

    var value: String = ""

    init {
        val typedArray = context?.theme?.obtainStyledAttributes(attrs, R.styleable.CustomRadioButton,
            0, 0)
        value = typedArray?.getString(R.styleable.CustomRadioButton_value).toString()
        typedArray?.recycle()
    }
}
```

Her deklarerer vi `value`, henter alle styleables på `init`, og tilegner `value` til å være den som ble satt i XML-deklarasjonen vår. Til slutt må array med styleables frigjøres siden det er en delt ressurs. 

## Hva har vi lært?
* At radio buttons i Android ikke har innebygd støtte for å sette verdi
* At det går an å gjøre noe med det
* At det blir mer kode av det (med mindre det er snakk om en radio button group med en hel haug radio buttons, men da bør man kanskje revurdere designet.. 😅)

## Konklusjon
Dette var et morsomt tanke- og kodeeksperiment. Kommer jeg til å bruke custom radio buttons kun for å fiske ut verdi istedenfor en brute-force id-sjekk? Sansynligvis ikke. Men dersom man uansett skal lage en egen komponent for radio button for å utvide med mer funksjonalitet, så er det fort gjort å slenge på en `value` som supplement. 


