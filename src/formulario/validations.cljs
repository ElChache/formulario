(ns formulario.validations
  (:require [formulario.validations.fns :as fns])
  (:refer-clojure :exclude [pos-int?]))

(defn required?
  ([] (required? "This field is required"))
  ([error-message]
   (fn [x] (when (fns/empty? x) error-message))))

(defn pos-int?
  ([] (pos-int? "Not a positive number"))
  ([error-message]
    (fn [x]
      (when-not (clojure.core/pos-int? x)
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