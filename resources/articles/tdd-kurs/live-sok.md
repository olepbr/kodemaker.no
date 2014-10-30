:title JavaScript TDD
:meta [{:name "robots", :content "noindex,nofollow"}]
:lead
### Live Search: Form requester

Extract the Ajax code from the live search implementation and build a proper
abstraction, a form requester. It's job is to serialize a form and turn it into
an HTTP request:

```
<form action="/search" method="get">
  <fieldset>
    <input type="text" name="q" id="q" value="User input">
    <input type="submit" value="Search">
  </fieldset>
</form>
```

```
var requester = new FormRequester(form);
requester.submit();
//=> GET /search?q=User%20input
```

Grab the code from here:

```
git clone http://kurs.kodemaker.no/tdd-kurs/live-search.git
```

Where do you start? A good place to start is to assert that when `submit` is
called, one HTTP request is made to the server. Try to keep the amount of
details in each test low, and they will be easier to pass.

:body
