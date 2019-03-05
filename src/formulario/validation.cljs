(ns formulario.validation
  (:require [cljs.spec.alpha :as s]
            [formulario.spec :as spec]))

(s/def ::validation-result-val (s/nilable string?))
(s/def ::validation-result-map (s/nilable (s/map-of keyword? ::validation-result-val)))
(s/def ::form-data ::spec/value)
(s/fdef validate
  :args (s/cat :form-data ::form-data
               :validation-map ::spec/validations)
  :ret ::validation-result-map)

(defn validate
  "Validates a map of data with a map of validations.
  Runs each function on the validation map by passing as a parameter the value
  of the associated key in the form data"
  [form-data validation-map]
  (->> validation-map
       (map (fn [[k f]]
              [k (f (get form-data k) form-data k)]))
       (into {})))

(s/fdef validations
  :args (s/cat :validations (s/coll-of fn?))
  :ret fn?)

(defn validations
  "Returns a function that composes the given validations.
  Validations are run one after the other, shortcutting when one returns a not nil value"
  [& validations]
  (fn [x]
    (->> validations
         (some (fn [validation] (validation x))))))

(s/fdef valid?
  :args (s/cat :validation-result ::validation-result-map)
  :ret boolean?)

(defn valid?
  "Returns true if the validation result values are nil, or is empty. False otherwise"
  [validation-result]
  (->> validation-result
       (not-any? (fn [[_ v]] (not (nil? v))))))

(defn on-submit [valid?])