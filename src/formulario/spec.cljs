(ns formulario.spec
  (:require [cljs.spec.alpha :as s]))

;; The validations are a map of functions.
;; The signature of each function is:
;; (fn [input-value form-value input-key] ...)
(s/def ::validations (s/map-of keyword? fn?))


;; An input is not pristine when it has been modified once
(s/def ::not-pristine? boolean)
(s/def ::input (s/keys :opt-un [::not-pristine?]))
(s/def ::inputs (s/map-of keyword? (s/or :input ::input
                                         :map ::inputs)))

;; A form and all its inputs can be forced to become not pristine
(s/def ::force-not-pristine? boolean)
(s/def :formulario.spec.meta/form
  (s/keys :opt-un [::not-pristine?
                   ::force-not-pristine?]))

;; Meta holds information for the inputs and for the form itself
(s/def ::meta (s/keys :opt-un [::inputs
                               :formulario.spec.meta/form]))

(s/def ::value (s/map-of keyword? (s/nilable (s/or :string string?
                                                   :number number?
                                                   :boolean boolean?
                                                   :collection coll?
                                                   :map ::value))))

(s/def ::form  (s/keys :req-un [::validations ::value]
                       :opt-un [::meta]))