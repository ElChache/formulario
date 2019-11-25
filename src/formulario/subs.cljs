(ns formulario.subs
  (:require [re-frame.core :as re]
            [formulario.validation :as validation]))

(re/reg-sub
  ::forms
  (fn [db _]
    (:forms db)))

(re/reg-sub
  ::form
  :<- [::forms]
  (fn [forms [_ form-id]]
    (get forms form-id)))

(re/reg-sub
  ::form-value
  (fn [[_ form-id _]]
    (re/subscribe [::form form-id]))
  (fn [form _]
    (:value form)))

(re/reg-sub
  ::form-validations
  (fn [[_ form-id _]]
    (re/subscribe [::form form-id]))
  (fn [form _]
    (:validations form)))

(re/reg-sub
  ::form-meta
  (fn [[_ form-id _]]
    (re/subscribe [::form form-id]))
  (fn [form _]
    (:meta form)))

(re/reg-sub
  ::input-val
  (fn [[_ form-id _]]
    (re/subscribe [::form-value form-id]))
  (fn [form-value [_ _ input-k]]
    (or (get-in form-value (if (vector? input-k) input-k [input-k])) "")))

(re/reg-sub
  ::form-errors
  (fn [[_ form-id]]
    [(re/subscribe [::form-value form-id])
     (re/subscribe [::form-validations form-id])])
  (fn [[value validations] _]
    (validation/validate value validations)))

(re/reg-sub
  ::form-pristine?
  (fn [[_ form-id]]
    (re/subscribe [::form-meta form-id]))
  (fn [form-meta _]
    (not (or (get form-meta :not-pristine?)
             (get form-meta :force-not-pristine?)))))

(re/reg-sub
  ::input-error
  (fn [[_ form-id]]
    (re/subscribe [::form-errors form-id]))
  (fn [form-errors [_ _ input-k]]
    (get-in form-errors (if (vector? input-k) input-k [input-k]))))

(re/reg-sub
  ::input-valid?
  (fn [[_ form-id input-k]]
    (re/subscribe [::input-error form-id input-k]))
  (fn [input-error _]
    (nil? input-error)))

(re/reg-sub
  ::input-not-pristine-valid?
  (fn [[_ form-id input-k]]
    (re/subscribe [::input-not-pristine-error form-id input-k]))
  (fn [input-error _]
    (nil? input-error)))

(re/reg-sub
  ::input-pristine?
  (fn [[_ form-id _]]
    (re/subscribe [::form-meta form-id]))
  (fn [form-meta [_ _ input-k]]
    (let [path (concat [:inputs]
                       (if (vector? input-k) input-k [input-k]))]
      (not (or (get-in form-meta path)
                (get-in form-meta [:form :force-not-pristine?]))))))

;; Returns an error only if the input is not pristine, and it has an error
(re/reg-sub
  ::input-not-pristine-error
  (fn [[_ form-id input-k]]
    [(re/subscribe [::input-pristine? form-id input-k])
     (re/subscribe [::input-error form-id input-k])])
  (fn [[pristine? error] [_ _ input-k]]
    (when-not pristine?
      error)))

(re/reg-sub
  ::form-valid?
  (fn [[_ form-id]]
    (re/subscribe [::form-errors form-id]))
  (fn [form-errors _]
    (validation/valid? form-errors)))

;; The form is valid if it is pristine. Or if it is valid
(re/reg-sub
  ::form-not-pristine-valid?
  (fn [[_ form-id]]
    [(re/subscribe [::form-valid? form-id])
     (re/subscribe [::form-pristine? form-id])])
  (fn [[form-valid? form-pristine?] _]
    (or form-pristine? form-valid?)))