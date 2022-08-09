(ns demo
  (:require [clojure.test :refer [deftest is]]))

(defrecord Thing [x])

(prn (System/identityHashCode Thing))

(defn print-stuff [klazz]  
  (prn {:class-id (System/identityHashCode klazz)})
  true)

(deftest do-stuff
  (is (print-stuff Thing))
  (is (print-stuff Thing)))

;; command generated when starting with tools.deps
;; clojure -Sdeps '{:deps {nrepl/nrepl {:mvn/version,"0.9.0"},cider/cider-nrepl {:mvn/version,"0.28.5"}}} '-M -m nrepl.cmdline --middleware "[cider.nrepl/cider-middleware]"

;; command generated when starting with leiningen
;; lein update-in :dependencies conj '[nrepl,"0.9.0"] '-- update-in :plugins conj '[cider/cider-nrepl,"0.28.5"] '-- update-in '[:repl-options,:nrepl-middleware] 'conj '["cider.nrepl/cider-middleware"] '-- repl :headless

;; observations: cider-nrepl is a dep in tools.deps, but a plugin in leiningen