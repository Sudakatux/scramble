(ns scramble.test.util
  (:use midje.sweet)
  (:use scramble.util)
  )

(fact "Returns true if chars provided can form the word, false if otherwise"
      (scramble? "aaaa" "a") => true
      (scramble? "rekqodlw" "world") => true
      (scramble? "cedewaraaossoqqyt" "codewars") => true
      (scramble? "katas" "steak") => false )

(fact "Should take amount of chars into account"
      (scramble? "lalaland" "aaand") => true
      (scramble? "pepe" "pepelepu") => false )

(fact "returns a message if it contains unknown chars"
      (validate-chars "ASDASD") => string?)

(fact "returns nil if the cars provided are ok"
      (validate-chars "thischarsareok") => nil?)

