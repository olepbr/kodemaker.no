:title JavaScript TDD
:meta [{:name "robots", :content "noindex,nofollow"}]
:lead
### TDD Mastermind

Implement the mastermind game feedback. 

```
"returns blank for no right answers": function () {
    var game = new Mastermind([1, 0, 2, 4]);
    var response = game.guess([3, 3, 3, 3]);

    assert.equals(response, "");
}
```

Think you are done? Try [these cases](/tdd-kurs/mastermind2).

:body

