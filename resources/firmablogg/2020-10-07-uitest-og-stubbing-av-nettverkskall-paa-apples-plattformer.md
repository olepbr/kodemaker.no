:title UI-tester og stubbing av nettverkskall på Apples plattformer
:published 2020-10-07
:author andre
:tech [:swift :testing]

:blurb

UI-testing har fått dårlig omtale. Testene kritiseres for å være trege, de tar lang tid å skrive, de er ofte ikke spesielt lesbare, brukergrensesnittet endrer seg kontinuerlig og sist, men ikke minst så er inkonsistente testdata og mangel på kontroll over responsen fra server en stor utfordring. 

Det siste punktet mener jeg er den største utfordringen, men jeg mener å ha funnet en veldig bra løsning på dette. Les videre, så skal du få se hvordan.


:body

UI-testing har fått dårlig omtale. Testene kritiseres for å være trege, de tar lang tid å skrive, de er ofte ikke spesielt lesbare, brukergrensesnittet endrer seg kontinuerlig og sist, men ikke minst så er inkonsistente testdata og mangel på kontroll over responsen fra server en stor utfordring. 

Ved skriving av enhetstester som involverer nettverkskall, så ønsker man som oftest å stubbe ut selve kallet, og dermed simulere en respons som enten kan være vellykket eller ikke, og er den vellykket, hva denne responsen skal inneholde.

På Apple sine plattformer, i rammeverket Foundation, så finner man URLSession og relaterte klasser som gir et API for å laste opp og ned data. En av disse klassene er URLProtocol, som bl.a har denne klassefunksjonen:

```swift 
    class func registerClass(_ protocolClass: AnyClass) -> Bool
```

Dokumentasjonen sier: _Attempts to register a subclass of URLProtocol, making it visible to the URL loading system._ 

Dette er noe vi kan benytte oss av ved å skrive vår egen subklasse av URLProtocol som tillater oss å legge til funksjonalitet som gir oss full kontroll over hva som skal skje når man gjør nettverkskall. Vi kan for eksempel fange et kall til ett bestemt endepunkt, og overstyre hva som skal skje da. Vi kan da returnere hva vi ønsker, og rett og slett hoppe over å gjøre kallet til endepunktet.



## MyStubURLProtocol?

Her er et eksempel som viser hvordan man kan gjøre akkurat dette:

```swift 
import Foundation

struct StubbedResponse {
    let response: HTTPURLResponse
    let data: Data
}

class MyStubURLProtocol: URLProtocol {
    static var urls = [URL: StubbedResponse]() // 1

    override class func canInit(with request: URLRequest) -> Bool { // 2
        guard let url = request.url else { return false }
        return urls.keys.contains(url)
    }

    override class func canonicalRequest(for request: URLRequest) -> URLRequest {
        request
    }

    override class func requestIsCacheEquivalent(_: URLRequest, to _: URLRequest) -> Bool {
        false
    }

    override func startLoading() {
        guard let client = client, let url = request.url, let stub = MyStubURLProtocol.urls[url] else { // 3
            fatalError()
        }

        client.urlProtocol(self, didReceive: stub.response, cacheStoragePolicy: .notAllowed) // 4
        client.urlProtocol(self, didLoad: stub.data) // 5
        client.urlProtocolDidFinishLoading(self)
    }

    override func stopLoading() {}
}

```



1. Opprette et statisk dictionary hvor URL er `key`, og response er `value`.
2. Fra dokumentasjonen: _Determines whether the protocol subclass can handle the specified request._ Dersom URL i request er tilstede, så vet man at dette er en stubbet versjon, og man returnerer true. Hvis false, så vil kallet gå til neste registrerte URLProtocol om det finnes, ellers så vil standard funksjonalitet utføres, som er å utføre det reelle nettverkskallet.
3. Denne kalles når en request starter, og her vil vi hente ut URLen til request og bruke den til å slå opp i urls dictionary.
4. `HTTPURLResponse` som skal returneres.
5. `Data` som skal returneres.


## Så hvordan bruker man MyStubURLProtocol?

Nedenfor ser vi et eksempel der vi ønsker å teste en tjeneste som utfører to nettverkskall for å løse oppgaven sin. Dette består av å først hente en public key, for deretter sette en kode hvor denne public key benyttes. Det er altså først et `GET` kall og deretter en `POST`. 



```Swift
import XCTest
import Foundation
@testable import MyProject

class CodeServiceTests: XCTestCase {
    let publicKeyUrl = URL(string: "https://example.com/lock/888/publickey")!
    let codeUrl = URL(string: "https://example.com/lock/888/code")!
   
    override func setUp() {
        super.setUp()
        URLProtocol.registerClass(MyStubURLProtocol.self) // 1
    }

    override func tearDown() {
        super.tearDown()
        URLProtocol.unregisterClass(MyStubURLProtocol.self) // 2
    }

    func testSetCode() {

        MyStubURLProtocol.urls[publicKeyUrl] = StubbedResponse(response: HTTPURLResponse(
            url: publicKeyUrl,
            statusCode: 201,
            httpVersion: nil,
            headerFields: nil)!, data: TestFixtures.getPublicKeyWithSuccess.data(using: .utf8)!) // 3


        MyStubURLProtocol.urls[codeUrl] = StubbedResponse(response: HTTPURLResponse(
            url: codeUrl,
            statusCode: 201,
            httpVersion: nil,
            headerFields: nil)!, data: "".data(using: .utf8)!) // 4


        let expectation = self.expectation(description: "Stubbed network call")
        CodeService.setCode(orderId: 999, orderLockId: 888, code: "123456") { result in
            XCTAssertTrue(result)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 1)
    }
 }   

```

1. Registrer MyStubURLProtocol.
2. Avregistrer MyStubURLProtocol.
3. Registrer URL med forventet/ønsket respons for GET.
4. Registrer URL med forventet/ønsket status for POST.


Det neste eksempelet viser en service som gjør et nettverkskall, hvor man verifiserer at resultatet inneholder forventede verdier.

```swift
 func testInitiateOrder() {
        let url = URL(string: "\(baseUrl)/order/initiate")!
        simpleStubURLProtocol(url: url, data: TestFixtures.initiateWithSuccess)
        let expectation = self.expectation(description: "Stubbed network call")

        OrderService().initiateOrder(order) { result in
            XCTAssertEqual(try? result.get().transactionId!, "9bc2374849ab446986c525ddc6d366ac")
            XCTAssertEqual(try? result.get().redirectUrl!.absoluteString, 
              "https://example.com/api/payment/transactions/9bc2374849ab446986c525ddc6d366ac/pay")
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 1)
    }


```

Det vi har sett her er hvordan jeg som oftest gjør når jeg skriver enhetstester som tester funksjonalitet som ellers ville ha involvert nettverkskall. Jeg har derfor lurt på om jeg kunne benytte samme teknikk ved skriving en UI-tester.

## Hva med UI-tester?

UI-tester har absolutt sine bruksområder dersom man bruker de riktig. Forskjellen mellom enhetstester og UI-tester (på iOS og iPadOS) er at testen starter appen, for så å teste den fra "utsiden". Testing av apper som henter data fra en ekstern datakilde blir avhengig av at riktig testdata er tilgjengelig til enhver tid når en test eller en samling av tester skal kjøres. Det er flere måter man kan løse dette på, men jeg har erfart at dette fort kan bli komplisert. 

La oss si man ønsker å benytte en sentral test server. Man vil da måtte "nullstille" denne før man kjører tester som oppdaterer data. Man kan forsøke å gjøre dette i setUp() og tearDown(), men det kan fort bli arbeidskrevende og mye kåling. 

Dersom du deler test server-instansen med andre utviklere så må man avtale at "nå skal jeg kjøre testene mine, kan du vente litt"... skalerer dårlig. Jeg antar at man har satt opp en CI-server og en build pipeline som kjører testene automatisk når endringer pushes? Skal dere da avtale når dere skal pushe så testkjøringene ikke går i beina på hverandre? 

Man kan forsøke å la testene kjøre i en bestemt rekkefølge, slik at den neste testen kan basere seg på resultatet av den forrige testen. Man blir nødt til å navngi testene slik at de er satt opp i riktig alfabetisk rekkefølge. Lykke til med å legge inn en ny test mellom to eksisterende.


Det finnes nok mange andre kreative og bedre fremgangsmåter enn dette man kan velge, og ett alternativt er at man bestemmer man seg for at det er så mye arbeid at det ikke er verdt det. Jeg håper  det ikke er tilfellet etter du har lest denne bloggposten :)

Siden erfaringen med å subklasse og registrere URLProtocol for enhetstester var så positiv, kan man ikke få til det samme for UI-tester?

### Hva er forskjellen mellom UI-tester og enhetstester og hva må vi tenke på her? 

1. Som allerede nevnt så tester man en UI-test fra utsiden av appen. Appen må på ett eller annet vis få tak i de forventede testdataene til de kallene som forventes å bli utført, men man ønsker ikke å legge testdata inn i selve app prosjektet. De må ligge i test prosjektet.
2. Man vil unngå UI-test spesifikk kode i selve app projektet så mye det er mulig.
3. Det er stor sannsynlighet for at en UI-test kaller samme endepunkt flere ganger, og det kan hende at det forventede resultatet er forskjellig for hver av disse kallene.
4. En del kall som autentisering og refresh av tokens osv vil utføres ofte, og vil være utenfor kontroll for testen og er heller ikke av interesse for testen som kjøres.
 

### Kode i appen

Etter å klødd meg litt i hodet, så kom jeg på en idé om hvordan dette kunne løses. Den største utfordringen slik jeg så det var hvordan man kan sette opp nødvendige stubber og tilhørende data i test prosjektet, for så å gjøre dette tilgjengelig i appen. Appen må også trigge stubbene i stedet for å utføre nettverkskall.


Nedenfor følger en del kode som jeg håper skal forklare hva jeg kom frem til.

#### AppDelegate 

```swift

func application(_: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
    #if DEBUG
        if Env.isUITesting {
            executeUITestWithStubs(ProcessInfo.processInfo.environment)
        }
    #endif
    ...
    ...

```

I AppDelegate så la jeg inn følgende kodesnutt, som sjekker om appen kjøres under en UI-test, og gjør man det, så kalles `executeUITestWithStubs`. Jeg har lagt denne sjekken inne i en `#if DEBUG` conditional compile block, som betyr at denne koden ikke vil komme med i Release-bygget. For å sjekke om vi kjører under en UI-test så sjekker jeg om argumentet `--UITests` er satt i launchArguments på UI-test siden. Vi vil se dette senere. 


```swift 
#if DEBUG
static var isUITesting : Bool {
    CommandLine.arguments.contains("--UITests")
}
#endif
```



`ProcessInfo.processInfo.environment` er definert slik: 
```swift
public protocol ProcessInfoType {
    var environment: [String: String] { get }
}
```

Det er denne typen vi benytter for å overføre data fra testen til applikasjonen. Vi putter inn dataene vi ønsker inn i `environment` på test-siden, og så henter vi ut `environment` inne i Appen, og sender den til `executeUITestWithStubs`.


Formatet på denne dictionary er som følger:


`
["xctest-arg-0"] = "https://example.com/lock/888/publickey > 200 > { 'publicKey': 'M28...IDAQAB' }"
`

`
["xctest-arg-1"] = "https://example.com/lock/888/code > 200 > "
`


Som du ser så er key xctest-arg-n. Nummereringen i xctest-arg-n er for å kunne utføre kallene i den rekkefølgen man setter opp stubbene. Value er en '>' separarert streng, hvor det første elementet inneholder selve URL-strengen som skal stubbes, det neste elementet inneholder HTTP statuskoden som stubben skal returnere, mens det siste inneholder body. Her både kan og burde man bruke et annet format, men når jeg skrev dette så var fokuset å se om konseptet fungerte, fremfor elegansen i koden. Det finnes en Ticket på å gjøre denne endringen, men så lenge det funker ...



Det finnes også en UITestHelper.swift som kun bygges under DEBUG slik:

```swift
#if DEBUG

import Foundation
import UIKit

func executeUITestWithStubs(_ stubs: [String: String]) {
    UIView.setAnimationsEnabled(false)
    URLProtocol.registerClass(URLProtocolStubber.self)
    executeTests(stubs)
}

private func executeTests(_ stubs: [String: String]) {
    let sortedStubKeys = stubs.filter {         // 1
        $0.key.starts(with: "xctest-arg-")
    }.keys.sorted()


    sortedStubs.forEach {                       // 2
        let stubInfo = ProcessInfo.processInfo.environment[$0]! as String
        let stub = stubInfo.split(separator: ">", maxSplits: 2) // 3
        let url = URL(string: String(stub[0]).trim())!
        let statusCode = Int(String(stub[1]).trim()) ?? 500
        let data = String(stub[2]).trim()
        stubURLProtocol(url: url, data: data, statusCode: statusCode)
    }
}

#endif

```

1. Her plukker man ut alt som ligger i environment som starter på xctest-arg- og bygger opp en liste som sorteres slik at de kommer i samme rekkefølge som de ble satt opp i testen.
2. Basert på listen over, så stubber man kallene på samme måte som vi gjorde det for enhetstestene, basert på innholdet i strengene vi har plukket ut fra `ProcessInfo.processInfo.environment`.
3. På grunn av det litt naive dataformatet som er benyttet, så er maxSplits satt til 2 for ikke å splitte på selve inneholdet i data.



Dette er faktisk alt som er på App siden, og siden all UI-test spesifikk koder ligger inne i `#if DEBUG`, så vil ikke noe av koden være med i Release-bygget. 


### Kode i UI-test prosjektet


Det meste av koden i Test prosjektet er hovedsaklig "helpers" og "utils" for å bygge opp en dictionary av URL, httpStatusCode og Body. For helt enkle stubdata så opprettet jeg noen multiline strenger med innhold, men etterhvert som det ble skrevet flere tester, og testdatamengden begynte å bli stor så valgte jeg å trekke det ut i egne JSON-filer. Disse JSON-filene ble i mange tilfeller veldig like, med bare minimale variasjoner. For å slippe å ha så mange nesten like filer så innførte jeg Stencil, som er et enkel templating bibliotek.

TestFixtures inneholder path/uri til kallet som stubbes, innholdet som skal returneres samt HTTP statuskode. `arguments` er til bruk for Stencil.


```swift
import Foundation
import Stencil

struct TestFixtures {
    let path: String
    let content: String
    let httpStatusCode: Int

    static func fetchFixture(
        fileName: String, 
        path: String, 
        httpStatusCode: Int = 200, 
        arguments: [String: Any] = [:]) -> TestFixtures {

        let fileContent = loadFile(fileName)
        let template = Template(templateString: fileContent)
        guard let result = try? template.render(arguments) else {
            return TestFixtures(path: path, content: "Invalid test data", httpStatusCode: 500)
        }
        return TestFixtures(path: path, content: result, httpStatusCode: httpStatusCode)
    }

    private static func loadFile(_ filename: String) -> String {
        FileLoader().loadFile(filename)
    }
}


class FileLoader {
    func loadFile(_ filename: String) -> String {
        if let path = Bundle(for: type(of: self)).path(forResource: filename, ofType: "json") {
            do {
                let data = try String(contentsOf: URL(fileURLWithPath: path), encoding: String.Encoding.utf8)
                return data
            } catch {
                return ""
            }
        }
        return ""
    }
}
```



Flere av endepunktene som inngår i testene har en tendens til å gå igjen ofte. Jeg har derfor puttet disse inn i en TestFixtures extension for funksjoner som enkelt kan kalles ved behov.


```swift
extension TestFixtures {
    static func serverStatus(path: String = "https://example.com/server-status-error?os=ios",
                             fileName: String = "serverStatus", someArg: Bool = true) -> TestFixtures {
        fetchFixture(
            fileName: fileName,
            path: path,
            arguments: ["someArg": someArg])
    }
}
```

Noen av testdataene er så små at de er like enkle å ha som Swift strenger:


```swift
static let accessToken: String =
        """
        {
            "access_token": "test_token_aaaabbbcccdddeeefffggg",
            "refresh_token": "test_refresh_token_hhhiiijjjkkklllmmmnnnooo",
            "token_type": "Bearer",
            "expires_in": 180
        }
        """
```

Så har vi Stubber-klassen. Den holder sort sett bare på en dictionary av stubber, samt funksjoner for å bygge opp denne samt å generere en unik key slik at flere kall til samme endepunkt skal kunne forekomme. Init tar ett argument, som når er satt til true vil føre til at en rekke stubber bli satt opp automatisk. Dette er typiske endepunkter som kall til refresh tokens, server status kall etc.


```swift
import Foundation

class Stubber {
    private var stubsDict = [String: String]()
    
    init(withPrelude: Bool = true) {
        if withPrelude {
            stub("https://example.com/oauth/accesstoken?refresh_token=2tjgsxmgbl1u&grant_type=refresh_token", 
                withContent: Prelude.accessToken)        
            stub("https://example.com/profile", 
                withContent: Prelude.profile)        
            ...
            ...
        }
    }
    
    func stub(_ urlString: String, httpStatusCode: Int = 200, withContent content: String) {
        self.stubsDict["xctest-arg-\(stubsDict.count)"] = "\(urlString) > \(httpStatusCode) > \(content)"
    }

    func stub(testFixture: TestFixtures) {
        self.stubsDict["xctest-arg-\(stubsDict.count)"] 
            = "\(testFixture.path) > \(testFixture.httpStatusCode) > \(testFixture.content)"
    }
    
    var stubs: [String: String] {
        stubsDict
    }
}
```


På App-siden, så sjekket vi om man kjører under en UI-test ved å sjekke om argumentet `--UITests` var satt i `launchArguments`. I en extension til `XCUIApplication` så er følgende kode lagt til. Her setter man `--UITests`, putter stubbene inn i `launchEnvironment`, og så kjører vi testen.

```swift
extension XCUIApplication {
    func stubAndLaunch(stubber: Stubber) {
        launchArguments.append("--UITests")
        launchEnvironment = stubber.stubs
        launch()
    }
}
```



En typisk UI-test vil kunne se slik ut hvor vi benytter oss av Stubber:

```swift
    func testDisplayServerStatusBanner() {
        let stubber = Stubber()
        stubber.stub(testFixture: .myService())
        stubber.stub(testFixture: .mySecondService())
        app.stubAndLaunch(stubber: stubber)
        app.buttons["myList.add"].tap()

        let errorMessageLabel = 
            self.app.staticTexts["A fatal error has occured. Please restart your computer."].firstMatch
        XCTAssert(errorMessageLabel.exists)
        let linkToMoreInfo = self.app.staticTexts[localized("serverStatusError.readMore")].firstMatch
        XCTAssert(linkToMoreInfo.exists)
    }

```


## Er det verdt det?
Vel, enhetstester er etter min mening de viktigste testene vi har. Det UI-tester tilfører er å minske behovet for manuell testing av brukergrensesnittet samt flyten i appen etter man har gjort endringer i koden. Den største ulempen med UI-tester er at de har en tendens til å råtne på rot, og mangel på riktige testdata er en viktig årsak til at dette skjer. Jeg håper derfor løsningen jeg har beskrevet her kan gi en ide om hvordan dette kan unngås. 


