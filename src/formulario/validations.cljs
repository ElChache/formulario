(ns formulario.validations
  (:require [formulario.validations.fns :as fns])
  (:refer-clojure :exclude [pos-int? pos? nat-int?]))

(defn required?
  ([] (required? "This field is required"))
  ([error-message]
   (fn [x] (when (fns/empty? x) error-message))))

(defn pos-int?
  ([] (pos-int? "Not a positive integer"))
  ([error-message]
    (fn [x]
      (when-not (clojure.core/pos-int? x)
        error-message))))

(defn pos?
  ([] (pos? "Not a positive number"))
  ([error-message]
   (fn [x]
     (when-not (fns/pos? x)
       error-message))))

(defn nat-int?
  ([] (nat-int? "Not a natural integer"))
  ([error-message]
   (fn [x]
     (when-not (clojure.core/nat-int? x)
       error-message))))

(defn not-neg-number?
  ([] (not-neg-number? "The number must be positive, or zero"))
  ([error-message]
   (fn [x]
     (when-not (fns/not-neg-number? x)
       error-message))))

(defn email?
  ([] (email? "Not a valid email"))
  ([error-message]
   (fn [x] (when-not (fns/email? x) error-message))))

(defn string-length
  ([min-chars]
   (string-length min-chars (str "It must have a minimum of " min-chars " characters.")))
  ([min-chars message]
   (fn [x] (when-not (>= (count x) min-chars) message))))

(defn is-url?
  ([] (is-url? "Not a valid url"))
  ([error-message]
   (fn [x]
     (when-not (fns/is-url? x) error-message))))