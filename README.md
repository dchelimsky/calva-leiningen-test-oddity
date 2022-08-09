# calva + leiningen classloading oddity

This repo demonstrates behavior in which repeatedly running tests using the calva tooling + leiningen can cause two references to a class defined in the test namespace to refer to different instances of that class.

I observed this behavior with the following versions

- VSCode 1.70.0
- Calva v2.0.291
- nrepl/nrepl 0.9.0
- cider/cider-nrepl 0.28.5

To see this in action:

- open this project in VSCode with Calva
- open the src/demo.clj file in an editor
- init a repl with the commands
  - Calva: Start or Connect to a Clojure REPL
  - Start your project with a repl and connect (a.k.a. Jack-in)
  - Leiningen
- run the tests using
  - Calva: Run Tests for Current Namespace

You should see output similar to this:

```clojure
clj꞉user꞉> 
; Evaluating file: demo.clj
{:thing-class-id 1679922067}
#'demo/test-print-thing-class-id
; Running tests for demo...
{:thing-class-id-from-test 1679922067}
{:thing-class-id-from-test 1679922067}
; 2 tests finished, all passing 👍, ns: 1, vars: 1
```

The first map, with `:thing-class-id`, contains the id of the Thing class (record) defined in the `demo` namespace, and is printed immediately after declaring the record.

The second and third maps printed also contain the the id of the `Thing` class, but those are printed from a test.

- now, run the tests again using
  - Calva: Run Tests for Current Namespace

Now you should see output similar to this:

```clojure
clj꞉demo꞉> 
; Evaluating file: demo.clj
{:thing-class-id 1960823937}
#'demo/test-print-thing-class-id
; Running tests for demo...
{:thing-class-id-from-test 1679922067}
{:thing-class-id-from-test 1960823937}
; 2 tests finished, all passing 👍, ns: 1, vars: 1
```

Observe that
- the first map shows a new id for the class (since it was reevaluated)
- the second map, printed via a test, shows the original id for the class
- the third map, printed via a test, shows the new id for the class

If you run the tests again, you'll continue to see a new id in the first and third maps each time, but the second map will continue to present the id from the first test run.

If you disconnect from the REPL and start a new REPL using deps.edn, you'll see new IDs for each test run, but all 3 ids will align.

Other observations:

```
;; command generated when starting with tools.deps
;; clojure -Sdeps '{:deps {nrepl/nrepl {:mvn/version,"0.9.0"},cider/cider-nrepl {:mvn/version,"0.28.5"}}} '-M -m nrepl.cmdline --middleware "[cider.nrepl/cider-middleware]"

;; command generated when starting with leiningen
;; lein update-in :dependencies conj '[nrepl,"0.9.0"] '-- update-in :plugins conj '[cider/cider-nrepl,"0.28.5"] '-- update-in '[:repl-options,:nrepl-middleware] 'conj '["cider.nrepl/cider-middleware"] '-- repl :headless

;; cider-nrepl is a dep in tools.deps, but a plugin in leiningen
```

Running this same exercise in other tools (emacs, IntelliJ+Cursive) with deps.edn (tools.deps) or project.clj (leiningen) consistently produces output with all 3 ids aligned.

