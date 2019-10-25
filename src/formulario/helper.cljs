(ns formulario.helper
  (:require [re-frame.core :as rf]
            [formulario.subs :as form-s]
            [formulario.events :as form-e]))

(defn input-value
  "Returns the value of an input.
  The output should be used as the value attribute of the controlled input"
  [id k]
  @(rf/subscribe [::form-s/input-val id k]))

(defn input-error
  "Returns the error of the input if it didn't pass the validation as a string, nil if no errors.
  The output could be used to display the error in the input"
  [id k]
  @(rf/subscribe [::form-s/input-error id k]))

(defn input-not-pristine-error
  "Same as input-error but only returns the error if the field is not pristine"
  [id k]
  @(rf/subscribe [::form-s/input-not-pristine-error id k]))

(defn input-valid?
  "Returns a boolean indicating if the input passes the validation
  The output could be used to hide or show the error on the input,
  and to assign the error class to the field."
  [id k]
  @(rf/subscribe [::form-s/input-valid? id k]))

(defn form-valid?
  "Returns true if all the inputs on the form pass the validations.
  The output could be used to show an error on the form, or hide or disable the submit button"
  [id]
  @(rf/subscribe [::form-s/form-valid? id]))

(defn form-value
  "Returns the value of all the inputs in the form as a map, where the keys are the keys used on the
  on-change function or the :lib.form.events/set-input-val event"
  [id]
  @(rf/subscribe [::form-s/form-value id]))

(defn on-change
  "Syntactic sugar function for the form inputs.
  The output should be used as the value for the on-change event listener of the inputs."
  ([id path]
   (on-change id path nil))
  ([id path form-value-atom]
   (fn [e]
     (let [target (.-target e)
           value (.-value target)
           value* (case (.-type target)
                    "number" (js/Number value)
                    value)]
       (when form-value-atom (swap! form-value-atom assoc-in path value))
       (rf/dispatch [::form-e/set-input-val id path value*])))))

(defn on-submit
  "Syntactic sugar function for submitting the forms.
  Calls the given function f with the form value, if the form is valid"
  [id f]
  (fn [e]
    (.preventDefault e)
    (js/console.log "form-valid?:" (form-valid? id) ", id:" id)
    (when (form-valid? id)
      (let [val @(rf/subscribe [::form-s/form-value id])]
        (f val e)))))

(defn init!
  "Initializes the reframe events with the given state spec on the interceptors"
  [state-spec]
  (form-e/init! state-spec))