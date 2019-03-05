(ns formulario.interceptors
  (:require [re-frame.core :refer [trim-v]]
            [re-frame.core :as re :refer [console]]
            [cljs.spec.alpha :as s]))

(defn validate-db [spec]
  (re/->interceptor
    :id :validate-db
    :after (fn [{{:keys [:event :re-frame.std-interceptors/untrimmed-event]} :coeffects
                 {:keys [:db]} :effects :as context}]
             (when (and (s/check-asserts?) db (not (s/valid? spec db)))
               (console :log db)
               (throw (js/Error. (str "DB is invalid after event"
                                      (or untrimmed-event event) "\n"
                                      (subs (s/explain-str spec db) 0 1000)))))
             context)))

(def debug?
  ^boolean goog.DEBUG)

(def default-interceptors
  [trim-v])

(defn debug-interceptors
  [db-spec]
  [(validate-db db-spec)])

(defn get-interceptors
  "Appends to the default re-frame interceptors the given ones.
  Also appends the debug interceptors if on debug mode"
  [db-spec & interceptors]
  (concat default-interceptors
          (when debug? (debug-interceptors db-spec))
          interceptors))