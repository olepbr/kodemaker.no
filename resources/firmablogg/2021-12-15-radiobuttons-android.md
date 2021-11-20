:title RadioButtons for Android
:author olga
:tech [:android]
:published 2021-12-15

:blurb
Hvorfor i alle dager sjekker vi på radio button selected id?

:body

Jeg har vært på flere app-prosjekter uten å bli eksponert for radiobuttons. Mulig det ikke er så utbredt i app-verden, eller at designere på de prosjektene ikke syntes de var noe særlig. Uansett, nå går jeg gjennom forberedelseskurs for Android sertifisering, og ett av oppgavene har radio buttons, yay! Ved å følge instruksjonene i [oppgaven](https://developer.android.com/courses/pathways/android-basics-kotlin-unit-2-pathway-1) lager man en enkel tip calculator app som ser sånn ut: 

<img style="width: 100%; max-width: 600px" src="/images/blogg/tip-calculator.png" alt="tip calculator" />


Vi lager en RadioGroup inne i `activity_main.xml`, og legger på RadioButtons - so far so good. Deretter går til `onCreate` i `MainActivity.kt`, legger på en click listener for calculate knappen. Derfra får vi tak i selected id på radio button, og legger på en switch statement for hva slags id som er selected. 

```kotlin
val tipPercentage = when (selectedId) {
    R.id.option_twenty_percent -> 0.20
    R.id.option_eighteen_percent -> 0.18
    else -> 0.15
}
```

Wait.. what? 

Hvorfor sjekker vi ikke bare på verdien av selected? Går inn i layout editor og trippeltsjekker alle attributter på radiobutton for å finne "value" jeg kan sette. Den finnes ikke?
Mulig jeg har vært for lenge på web-prosjekter. Hvorfor har ikke radio buttons en value? Nei, dette var irriterende, må jo ha en value man kan sende inn på en radio button?!

## Custom radio button
Løsningen her ble å lage en custom radio button. 

```kotlin
class CustomRadioButton(context: Context?, attrs: AttributeSet?) :
    AppCompatRadioButton(context, attrs) {
}
```

Foreløpig speiler den bare den eksisterende implementasjon av vanlig radio button. Neste steg er å få den til å ha value som attributt. Under `values` mappen, legg inn en ny fil `attrs.xml` og deklarer en ny value som en string attributt, knyttet til styleables for vår nye CustomRadioButton.

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="CustomRadioButton">
        <attr name="value" format="string" />
    </declare-styleable>
</resources>
```
og whoosh, nå kan vi sette value på custom radio button via `app` namespace inne i `activity_main.xml`. 

```xml
<com.example.tiptime.CustomRadioButton
            android:id="@+id/option_20_percent"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/amazing_20"
            app:value="20"/>
```

Dette var gøy! Men, hvordan kan vi nå få tak i value på radio button fra koden? La oss teste direkte i `MainActivity.kt`: 

```kotlin
    val checkedId = findViewById<RadioGroup>(R.id.tip_options).checkedRadioButtonId
    val checked = findViewById<CustomRadioButton>(checkedId)
    val checkedValue = checked.value
```

Det gikk ikke, value er rødt og den banner på oss med `Unresolved reference: value`. Vi må eksponere `value` på noe vis. 

Tilbake i `CustomRadioButton.kt`, utvid klassen med

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

Her deklarerer vi value, henter alle styleables på init, og tilegner value til å være den som ble satt i xml deklarasjonen vår. Til slutt må array med styleables frigjøres siden det er et delt ressurs. Hvis vi nå sjekker koden vår i `MainActivity.kt`, så er ikke `value` rødt lenger.

## Hva har vi lært?
* At radio buttons i Android ikke har innebygd støtte for å sette verdi
* At det går an å gjøre noe med det
* At det blir alt i alt mer kode av det (med mindre du har en radio button group med en hel haug radiobuttons, men da bør man kanskje revurdere designet.. 😅)

## Konklusjon
Dette var et morsomt tanke- og kodeeksperiment. Kommer jeg til å bruke custom radio buttons kun for å fiske ut verdi istedenfor en brute-force id-sjekk? Sansynligvis ikke. Men dersom man uansett skal lage et eget komponent for radio button for å utvide med mer funksjonalitet, så er det fort gjort å slenge på en `value` som supplement. 


