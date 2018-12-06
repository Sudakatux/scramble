(ns scramble.util
  (:require [clojure.set :as set]
            [clojure.string :refer [lower-case]]))

(defn scramble? [chars word]
  (let [c_freq (->> chars lower-case frequencies)
        w_freq (->> word lower-case frequencies)
        not_present_in_word  (set/difference (set (keys c_freq)) (set (keys w_freq)))
        not_present_in_chars  (set/difference (set (keys w_freq)) (set (keys c_freq)))
        filled_chars_word (reduce #(assoc %1 %2 0) w_freq not_present_in_word)
        filled_chars_chars (reduce #(assoc %1 %2 0) c_freq not_present_in_chars)
        ]
    (every? #(<= (get filled_chars_word %) (get filled_chars_chars %)) (keys filled_chars_word))))

(defn validate-chars
  "Takes a string. returns a message if sting does not comply with validation"
  [chars]
  (when-not (re-matches #"[a-z]*" chars) "Invalid char. only letters allowed"))
