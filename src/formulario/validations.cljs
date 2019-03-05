(ns formulario.validations
  (:refer-clojure :exclude [pos-int?]))

(defn required?
  ([] (required? "This field is required"))
  ([error-message]
   (fn [x]
     (when (or (nil? x)
               (and (string? x) (empty? x)))
       error-message))))

(defn pos-int?
  ([] (pos-int? "Not a positive number"))
  ([error-message]
    (fn [x]
      (when (and (not (nil? x))
                 (not (clojure.core/pos-int? x)))
        error-message))))