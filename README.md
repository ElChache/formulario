# Formulario

[![Clojars Project](https://img.shields.io/clojars/v/formulario.svg)](https://clojars.org/formulario)

Formulario is a library to ease the pain of forms in re-frame.

Formulario allows to register forms and access them through events and subscriptions.

## Library initialization
First you need to initialize the library, optionally passing a database spec if you want to validate
the database state on every event. That will register the events with re-frame.

``` clojure
(ns my-app.core
  (:require [formulario.events :as formulario
            [my-app.db :as db]]))
           
(formulario/init ::db/db-spec)
```

## Forms initialization
Forms `:validations` and `:value` need to be placed on the db initial value on key `:forms`. 

`:validations` is a map of functions with the signature `[input-value form-value]` 
and `:value` is a map with the value of the form

``` clojure
;;;;;;;;;;;;;;;;;;;;;;;
;; MY FORM NAMESPACE ;;
;;;;;;;;;;;;;;;;;;;;;;;
(ns my-app.my-form)

(def id ::my-form)

(def validations
  {:name       #(when (clojure.string/blank? %)
                 "The name can't be empty")
   :start-date (fn [date {:keys [end-date]}]
                 (when (> (.getTime date) (.getTime end-date))
                   "Start date must be lower than end date"))
   :end-date   (fn [date {:keys [start-date]}]
                 (when (< (.getTime date) (.getTime start-date))
                    "End date must be greater than end date"))
   :age         #(when (< % 18)
                  "You need to be overage")}

(def initial-value
  {:age 18})

(def form {:validations validations
           :value intial-value}

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; RE-FRAME DB NAMESPACE ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns my-app.db
  :require [my-app.my-form :as my-form])
  
(def db
  {:forms {my-form/id my-form/form}
   ...
  }          
```

## Usage
After initializing the library and setting the initial value and validations for each form on the
re-frame db, we can start using the events, subscriptions, and helpers

## Quirks
Because re-frame triggers events, and those events are not immediately triggered, it is very recommended
to duplicate the state of the form in a local atom, which will re-render the form with no delay

## Example
The following example shows a very simple form that shows errors when the form is not pristine.
``` clojure
;;;;;;;;;;;;;;;;;;;;;;;;;
;; Form view namespace ;;
;;;;;;;;;;;;;;;;;;;;;;;;;
(ns my-app.form-view
  (:require [my-app.my-form :as form]
            [my-app.events :as events]
            [re-frame.core :as re]))
  
(reagent/with-let
    [item (reagent/atom @(re/subscribe [::form-subs/form-value form/id]))
     update-item (fn [value path]
                        (swap! item assoc-in path value)
                        (re/dispatch [::form-events/set-input-val
                                      form/id
                                      path
                                      (get-in @item path)]))
     form-valid? @(re/subscribe [::form-subs/form-not-pristine-valid? form/id])]

    [:form.ui.form
     {:on-submit #(re/dispatch [::events/submit-my-form])
      :class     (when (not form-valid?) "error")}

     (let [error @(re/subscribe [::form-subs/input-not-pristine-error form/id [:code]])]
       [:div.field
        {:class (when error "error")}
        [:label "item code"]
        [:input {:type      "text"
                 :on-change (fn [e]
                              (update-item (-> e .-target .-value) [:code]))
                 :value     (get-in @item [:code])
                 :disabled (contains? disabled-inputs :code)}]
        [:label error]])

     (let [error @(re/subscribe [::form-subs/input-not-pristine-error form/id [:name]])]
       [:div.field
        {:class (when error "error")}
        [:label "item name"]
        [:input {:type      "text"
                 :on-change (fn [e]
                              (update-item (-> e .-target .-value) [:name]))
                 :value     (get-in @item [:name])
                 :disabled (contains? disabled-inputs :name)}]
        [:label error]])

     (let [error @(re/subscribe [::form-subs/input-not-pristine-error form/id [:description]])]
       [:div.field
        {:class (when error "error")}
        [:label "item description"]
        [:input {:type      "text"
                 :on-change (fn [e]
                              (update-item (-> e .-target .-value) [:description]))
                 :value     (get-in @item [:description])
                 :disabled (contains? disabled-inputs :description)}]
        [:label error]])

     [:div.two.fields
      (let [error @(re/subscribe [::form-subs/input-not-pristine-error form/id [:start-date]])]
        [:div.field
         {:class (when error "error")}
         [:label "Start date"]
         [ui/date-input {:dateFormat "YYYY-MM-DD"
                         :maxDate    (get-in @item [:end-date])
                         :value      (get-in @item [:start-date] "")
                         :onChange   (fn [e data]
                                       (update-item (gobj/get data "value") [:start-date]))
                         :disabled (contains? disabled-inputs :start-date)}]
         [:label error]])

      (let [error @(re/subscribe [::form-subs/input-not-pristine-error form/id [:end-date]])]
        [:div.field
         {:class (when error "error")}
         [:label "End date"]
         [ui/date-input {:dateFormat "YYYY-MM-DD"
                         :minDate    (get-in @item [:start-date])
                         :value      (get-in @item [:end-date] "")
                         :onChange   (fn [e data]
                                       (update-item (gobj/get data "value") [:end-date]))
                         :disabled (contains? disabled-inputs :end-date)}]
         [:label error]])]

     (when error
       [:div.ui.error.message
        [:div.header "Error creating the item"]
        [:p error]])

     ;; Hidden submit button to allow submitting by pressing Enter
     [:input {:type  "submit"
              :style {:display "none"}}]
     ])
     
;;;;;;;;;;;;;;;;;;;;;;
;; Events namespace ;;
;;;;;;;;;;;;;;;;;;;;;;
(ns my-app.events
  (:require [formulario.events :as form-events]
            [formulario.subs :as form-subs]
            [my-app.my-form :as my-form]
            [re-frame.core :as re])

(re/reg-event-fx
  ::submit-my-form
  (fn [{:keys [db]} _]
    (let [item @(re/subscribe [::form-subs/form-value my-form/id])
          valid? @(re/subscribe [::form-subs/form-valid? my-form/id])]

      (if valid?
        {:db          (assoc-in db [:item :creation :creating-state] ::state/sending-request)
         ::fx/create! {:item item}}
        {:dispatch [::form-events/unset-pristine my-form/id]]}))))
```

## License

Copyright © 2019

Distributed under the MIT License