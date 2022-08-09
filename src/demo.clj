(ns demo
  (:require [clojure.test :refer [deftest is]]))

(defrecord Thing [x])

(prn {:thing-class-id (System/identityHashCode Thing)})

(defn print-thing-class-id [klazz]  
  (prn {:thing-class-id-from-test (System/identityHashCode klazz)})
  true)

(deftest test-print-thing-class-id
  (is (print-thing-class-id Thing))
  (is (print-thing-class-id Thing)))