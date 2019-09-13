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

(defn email?
  ([] (email? "Not a valid email"))
  ([error-message]
   (fn [x]
     (let [re #"^(([^<>()\[\]\\.,;:\s@\"]+(\.[^<>()\[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$"]
       (when-not (re-matches re (or x ""))
         error-message)))))

(defn string-length
  ([min-chars]
   (string-length min-chars (str "It must have a minimum of " min-chars " characters.")))
  ([min-chars message]
   (fn [x]
     (when-not (>= (count x) min-chars)
       message))))