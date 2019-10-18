(ns formulario.validations.fns
  (:refer-clojure :exclude [empty?]))

(defn empty?
  "Returns true if the given value is null or an empty string"
  [x]
  (or (nil? x)
      (and (string? x) (clojure.core/empty? x))))

(defn email?
  "Returns true if the given string is a valid email"
  [x]
  (let [re #"^(([^<>()\[\]\\.,;:\s@\"]+(\.[^<>()\[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$"]
    (some? (re-matches re (or x "")))))

(defn is-url?
  "Returns true if the given string is a url"
  [x]
  (let [p (js/RegExp. "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")]
    (.test p x)))