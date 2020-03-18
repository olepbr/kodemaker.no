:title Profilering av kode i Go
:author nils
:tech [:go]
:published 2020-03-18

:blurb

Noen ganger er ytelse kritisk for at ting skal fungere ordentlig. Et bibliotek for parsing av binære værdata er en av de gangene. Med Go har vi mulighet til å kunne si akkurat hvilken kodelinje som tar tid eller som allokerer minne. 

:body

[Griblib](https://github.com/nilsmagnus/grib) er et bibliotek for å parse binære filer for værdata. Disse filene inneholder komprimerte data om prediksjoner og observasjoner av meteorologiske data. I denne bloggposten vil jeg vise hvordan jeg brukte de innebygde verktøyene til Go for å nøyaktig profilere biblioteket og forbedre ytelsen til biblioteket. 

## Profilering av testene

I vårt tilfelle gjør vi profilering på testene til griblib. Det er ca 17 tester der som eksekverer den samme koden men som verifiserer ulike deler av resultatet. 

For å kjøre testene som normalt bruker vi kommandoen `go test - v`. For å kjøre testene med profilering legger vi på noen flagg for cpu- og minne-profilering:

    go test -memprofile memprofile.out -cpuprofile profile.out

Dette kjører testene med profilering og lagrer resultatene i filene `memprofile.out` og `cpuprofile.out`. 

Vi kan inspisere disse med kommandoen `go tool pprof <filnavn>`. Først inspiserer vi cpu-bruken:

    go tool pprof -web cpuprofile.out
	
Denne kommandoen åpner nettleseren din og viser en svg med detaljert info om hvordan tidsbruken fordeler seg. Dette kan være litt overveldende siden den inkluderer absolutt alle funksjoner som blir kjørt av testene. Ved å bruke kommandolinjen får vi enklere oversikt:

	go tool pprof cpuprofile.out
	
    File: gribtest.test
	Type: cpu
	Time: Mar 10, 2020 at 9:21pm (CET)
	Duration: 5.22s, Total samples = 5.35s (102.40%)
	Entering interactive mode (type "help" for commands, "o" for options)

Dette åpner pprof som gir oss mulighet til å blant annet se topp 10 tidstyver med kommandoen `top10`:

	(pprof) top10
	Showing nodes accounting for 3740ms, 69.91% of 5350ms total
	Dropped 104 nodes (cum <= 26.75ms)
	Showing top 10 nodes out of 89
      flat  flat%   sum%        cum   cum%
     880ms 16.45% 16.45%     1390ms 25.98%  github.com/nilsmagnus/grib/griblib.(*BitReader).readBit
     590ms 11.03% 27.48%     1980ms 37.01%  github.com/nilsmagnus/grib/griblib.(*BitReader).readUint
     530ms  9.91% 37.38%      530ms  9.91%  runtime.memmove
     470ms  8.79% 46.17%      470ms  8.79%  runtime.memclrNoHeapPointers
     290ms  5.42% 51.59%      290ms  5.42%  github.com/nilsmagnus/grib/griblib.(*BitReader).currentBit
     290ms  5.42% 57.01%     2690ms 50.28%  github.com/nilsmagnus/grib/griblib.(*BitReader).readIntsBlock
     210ms  3.93% 60.93%      500ms  9.35%  encoding/binary.(*decoder).value
     190ms  3.55% 64.49%      210ms  3.93%  bytes.(*Buffer).ReadByte
     160ms  2.99% 67.48%      690ms 12.90%  runtime.mallocgc
     130ms  2.43% 69.91%      460ms  8.60%  github.com/nilsmagnus/grib/griblib.(*Data2).scaleValues
	 
Hvis vi vil kan vi ytterligere inspisere de ulike funksjonene med `list <navn-på-funksjon>`: 

     (pprof) list readBit
	 
	 Total: 5.35s
     ROUTINE ======================== github.com/nilsmagnus/grib/griblib.(*BitReader).readBit in /home/larsgard/go/src/github.com/nilsmagnus/grib/griblib/bitreader.go
     880ms      1.39s (flat, cum) 25.98% of Total
         .          .     31:	dataReader.Read(rawData)
         .          .     32:	buffer := bytes.NewBuffer(rawData)
         .          .     33:	return newReader(buffer)
         .          .     34:}
         .          .     35:
     220ms      220ms     36:func (r *BitReader) readBit() (uint, error) {
     140ms      140ms     37:	if r.offset == 8 {
      50ms       60ms     38:		r.resetOffset()
         .          .     39:	}
     160ms      160ms     40:	if r.offset == 0 {
         .          .     41:		var err error
      50ms      260ms     42:		if r.byte, err = r.reader.ReadByte(); err != nil {
         .          .     43:			return 0, err
         .          .     44:		}
         .          .     45:	}
      20ms      310ms     46:	bit := uint(r.currentBit())
      10ms       10ms     47:	r.offset++
     230ms      230ms     48:	return bit, nil
         .          .     49:}
         .          .     50:
         .          .     51:func (r *BitReader) readUint(nbits int) (uint64, error) {
         .          .     52:	var result uint64
         .          .     53:	for i := nbits - 1; i >= 0; i-- {


## Mye tidsbruk på minnehåndtering

Naturlig nok er det mange funksjoner fra biblioteket vårt som ligger på topp, men det er bekymringsverdig at funksjoner som `memmove` og `memclrNoHeapPointers` dukker opp. Dette betyr at mye av tiden går med til garbage-collection og minnehåndtering, noe som er rart med en så effektiv garbage-collector som følger med i Go-runtimen. 

Vi inspiserer derfor minnebruken på samme måte for å finne topp 10 minnetyver:

	go tool pprof memprofile.out
	(pprof) top10
	Showing nodes accounting for 8.32GB, 99.82% of 8.33GB total
	Dropped 29 nodes (cum <= 0.04GB)
	Showing top 10 nodes out of 22
      flat  flat%   sum%        cum   cum%
    4.39GB 52.72% 52.72%     5.48GB 65.74%  github.com/nilsmagnus/grib/griblib.(*Data2).extractData
    2.13GB 25.61% 78.33%     2.13GB 25.61%  github.com/nilsmagnus/grib/griblib.(*Data2).scaleValues
    0.97GB 11.63% 89.96%     0.97GB 11.63%  github.com/nilsmagnus/grib/griblib.(*BitReader).readIntsBlock
    0.25GB  3.04% 93.00%     0.50GB  5.96%  github.com/nilsmagnus/grib/griblib.(*Data2).extractBitGroupParameters
    0.25GB  2.99% 95.99%     0.25GB  2.99%  github.com/nilsmagnus/grib/griblib.(*BitReader).readUintsBlock
    0.12GB  1.39% 97.38%     0.12GB  1.39%  github.com/nilsmagnus/grib/griblib.bitGroupParameter.zeroGroup
    0.05GB  0.65% 98.02%     8.27GB 99.25%  github.com/nilsmagnus/grib/griblib.readMessage
    0.05GB  0.63% 98.66%     8.32GB 99.89%  github.com/nilsmagnus/grib/griblib.ReadMessage
    0.05GB  0.59% 99.25%     0.05GB  0.59%  github.com/nilsmagnus/grib/griblib.makeBitReader
    0.05GB  0.57% 99.82%     0.05GB  0.57%  encoding/binary.Read
	
Den verste funksjonen allokerer 4.39GB minne under testene!! Ved bruk av `list <navn-på-funksjon>` finner vi syndebukken med kirurgisk presisjon:

    (pprof) list extractData
    Total: 8.33GB
    ROUTINE ======================== github.com/nilsmagnus/grib/griblib.(*Data2).extractData in /home/larsgard/go/src/github.com/nilsmagnus/grib/griblib/data2.go
    4.39GB     5.48GB (flat, cum) 65.74% of Total
         .          .    105:func (template *Data2) extractData(bitReader *BitReader, bitGroups []bitGroupParameter) ([]int64, []int64, error) {
         .          .    106:	section7Data := []int64{}
         .          .    107:	ifldmiss := []int64{}
         .          .    108:
         .          .    109:	for _, bitGroup := range bitGroups {
         .     1.08GB    110:		tmp, err := bitGroup.readData(bitReader)
         .          .    111:		if err != nil {
         .          .    112:			return section7Data, ifldmiss, fmt.Errorf("bitGroup read: %s", err.Error())
         .          .    113:		}
         .          .    114:		switch template.MissingValue {
         .          .    115:		case 0:
         .          .    116:			for _, elt := range tmp {
    2.10GB     2.10GB    117:				section7Data = append(section7Data, elt+int64(bitGroup.Reference))
    2.12GB     2.12GB    118:				ifldmiss = append(ifldmiss, 0)
         .          .    119:			}
    ....
	
Det er altså 2 kodelinjer(linje 117 og 118) som står for 4GB av minnebruken og begge bruker den innebygde funksjonen `append`. 

Append legger til et element i en slice. Men hvis det ikke er nok kapasitet i den underliggende arrayen til slicen så blir det opprettet en ny slice med dobbel kapasitet for å sikre "amortisert lineær kompleksitet" (1). Dvs at dette er en god måte å håndeter for liten kapasitet i en slice i de fleste tilfeller. For oss er det en katastrofe fordi vi opererer på ganske store slicer som blir utvidet mange ganger og fører til mye arbeid for GC.

## Endringer i koden

Endringene i koden blir som følger: istedenfor å opprette en tom slice for å appende på den, så oppretter vi en slice med preallokert størrelse siden vi egentlig vet hvor stor slicen skal bli. I tillegg må vi manuelt håndtere indekser når vi allokerer verdier til slicene. Altså, istedenfor 

	minSlice := []int64{} // opprette slice med 0 i kapasitet
	minSlice = append(minSlice, nyverdi) // bruke append for å legge til en verdi i slutten av slicen
	
så blir det

	size := calculateSizeSomehow()
	minSlice := make([]int64,size)
	minSlice[indeks] = nyverdi
	indeks++

Vi ofrer altså lesbarhet for ytelse. De første testene etter å ha gjort dette viste forbedringer.

## Ytelsesforbedringer!

Så, etter en del optimaliseringer kjører vi testene på nytt og ser at tidsbruken har gått fra 5.4 sekunder til ca 3.5 sekunder. Og vi bruker ikke så mye tid på GC lenger:

	22:24 $ go tool pprof cpuprofile.out 
	File: gribtest.test
	Type: cpu
	Time: Mar 10, 2020 at 10:24pm (CET)
	Duration: 3.76s, Total samples = 3.59s (95.36%)
	Entering interactive mode (type "help" for commands, "o" for options)
	(pprof) top10
	Showing nodes accounting for 2980ms, 83.01% of 3590ms total
	Dropped 60 nodes (cum <= 17.95ms)
	Showing top 10 nodes out of 66
      flat  flat%   sum%        cum   cum%
    1190ms 33.15% 33.15%     1320ms 36.77%  github.com/nilsmagnus/grib/griblib.(*BitReader).readBit
     760ms 21.17% 54.32%     2080ms 57.94%  github.com/nilsmagnus/grib/griblib.(*BitReader).readUint
     230ms  6.41% 60.72%      230ms  6.41%  runtime.memclrNoHeapPointers
     140ms  3.90% 64.62%     1950ms 54.32%  github.com/nilsmagnus/grib/griblib.(*BitReader).readIntsBlock
     130ms  3.62% 68.25%      500ms 13.93%  encoding/binary.(*decoder).value
     120ms  3.34% 71.59%      190ms  5.29%  reflect.Value.Index
     110ms  3.06% 74.65%     2250ms 62.67%  github.com/nilsmagnus/grib/griblib.(*Data2).extractData
     100ms  2.79% 77.44%      130ms  3.62%  bytes.(*Buffer).ReadByte
     100ms  2.79% 80.22%      200ms  5.57%  github.com/nilsmagnus/grib/griblib.(*Data2).scaleValues
     100ms  2.79% 83.01%      100ms  2.79%  github.com/nilsmagnus/grib/griblib.(*Data3).applySpacialDifferencing

Vi ser at funksjonen `memclrNoHeapPointers` forstatt bruker en del cpu, så det er sikkert flere steder koden kan optimaliseres. 


Minneforbruket har også gått ned fra 8.33GB til 2.2GB :

	22:25 $ go tool pprof memprofile.out 
	File: gribtest.test
	Type: alloc_space
	Time: Mar 10, 2020 at 10:24pm (CET)
	Entering interactive mode (type "help" for commands, "o" for options)
	(pprof) top10
	Showing nodes accounting for 2152.67MB, 99.30% of 2167.80MB total
	Dropped 33 nodes (cum <= 10.84MB)
	Showing top 10 nodes out of 22
      flat  flat%   sum%        cum   cum%
    801.65MB 36.98% 36.98%  1220.81MB 56.32%  github.com/nilsmagnus/grib/griblib.(*Data2).extractData
    420.50MB 19.40% 56.38%   420.50MB 19.40%  github.com/nilsmagnus/grib/griblib.(*Data2).scaleValues
    374.09MB 17.26% 73.63%   374.09MB 17.26%  github.com/nilsmagnus/grib/griblib.(*BitReader).readIntsBlock
    233.18MB 10.76% 84.39%   305.45MB 14.09%  github.com/nilsmagnus/grib/griblib.(*Data2).extractBitGroupParameters
    72.27MB  3.33% 87.72%    72.27MB  3.33%  github.com/nilsmagnus/grib/griblib.(*BitReader).readUintsBlock
    55.79MB  2.57% 90.30%    55.79MB  2.57%  encoding/binary.Read
    54.67MB  2.52% 92.82%  2156.54MB 99.48%  github.com/nilsmagnus/grib/griblib.ReadMessage
    47.80MB  2.21% 95.03%  2101.87MB 96.96%  github.com/nilsmagnus/grib/griblib.readMessage
    47.64MB  2.20% 97.22%    47.64MB  2.20%  github.com/nilsmagnus/grib/griblib.makeBitReader
    45.07MB  2.08% 99.30%    45.07MB  2.08%  github.com/nilsmagnus/grib/griblib.(*bitGroupParameter).zeroGroup (inline)


## Go tooling rocker

Go er et enkelt programmeringsspråk med enkle verktøy. Med profileringsverktøyene som er innebygd i Go er det lett å pinpointe akkurat hvor vi kan optimalisere koden uten å måtte ty til komplekse analyseprogram og 3.parts verktøy. Det enkle er ofte det beste :)

(Hvis du vil kjøre testene selv, så kan du sjekke ut koden fra github og git-taggen `before-optimize` for å kjøre testene før koden ble optimialisert og master/HEAD for å kjøre testene etter optimaliseringen.)


(1)  Donovan and Kernighan, "The Go Programming Language", Kap. 4.2.1.
