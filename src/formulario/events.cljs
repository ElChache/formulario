(ns formulario.events
  (:require [re-frame.core :as re]
            [formulario.interceptors :refer [get-interceptors]]
            [cljs.spec.alpha :as s]))

(s/def ::any any?)

(defn init! [db-spec]
  (let [db-spec (or db-spec ::any)
        mw (get-interceptors db-spec)]

    (re/reg-event-db
      ::set-input-val mw
      (fn [db [form-id input-k value]]
        (let [input-k (if (vector? input-k) input-k [input-k])
              p (concat [:forms] [form-id :value] input-k)
              input-not-pristine-p (concat [:forms] [form-id :meta :inputs] input-k [:not-pristine?])
              form-not-pristine-p (concat [:forms] [form-id :meta :not-pristine?])]
          (-> db
              (assoc-in p value)
              (assoc-in input-not-pristine-p true)
              (assoc-in form-not-pristine-p true)))))

    ;; Sets the form an all its inputs as not pristine, so errors can be shown
    (re/reg-event-db
      ::unset-pristine mw
      (fn [db [form-id]]
        (assoc-in db [:forms form-id :meta :form :force-not-pristine?] true)))

    (re/reg-event-db
      ::reset-form mw
      (fn [db [form-id & [value]]]
        (-> db
            (update-in [:forms form-id] dissoc :meta)
            (assoc-in [:forms form-id :value] (or value {})))))))