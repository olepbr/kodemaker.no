:title JavaScript TDD
:meta [{:name "robots", :content "noindex,nofollow"}]
:lead
### TDD Mastermind

Implement the Mastermind core logic. Try to write tests that require as few
changes to production code as possible at a time. A good strategy is to write
tests that require increasingly complex feedback (no characters, one character,
two characters etc).

#### Rules summarized

1. The game consists of four digits, 0-4
2. Digits may be repeated (e.g. `[1, 1, 0, 2]` is a valid game)
3. Every exact match should result in one `+` in the feedback.
4. Every digit that is not an exact match, but is found elsewhere in the
   solution should result in one `-` in the feedback.
5. The feedback should not reveal information about which digit resulted in what
   feedback. Sort the feedback: `+` first, then `-`.

#### 1. Create a new project

Create a new directory, install Buster.JS in it (`npm install buster`) and add a
`buster.js` configuration file. Feel free to take a look at the Bowling source
code for reference:

```sh
git clone http://kurs.kodemaker.no/tdd-kurs/bowling.git
```

#### 2. Create source and test files

Create the empty files `src/mastermind.js` and `test/mastermind-test.js`, make
sure they're referenced by the `buster.js` configuration file, and run the tests
to make sure the configuration is in order. It should say "no tests".

```sh
./node_modules/.bin/buster-test
```

If you want to run the tests in the browser, you must first start the server in
a separate shell:

```sh
./node_modules/.bin/buster-server
```

And capture a browser at [http://localhost:1111](http://localhost:1111).

If you want to run the tests in node.js, set `environment` in the `buster.js`
configuration file to `node`, and export and require the mastermind module
(refer to the bowling source again).

#### 3. Write the first test

Start with a "null case" - something that requires no or very little logic. A
case where the feedback is a blank string is a good start:

```js
"returns blank for no right answers": function () {
    var game = new Mastermind([1, 0, 2, 4]);
    var response = game.guess([3, 3, 3, 3]);

    assert.equals(response, "");
}
```

Now add tests of increasing difficulty until you can think of no more cases.

Think you are done? Try [these cases](/tdd-kurs/mastermind2).

Really done? Add (test-first, of course) a function that generates a random
game.

:body

