(ns crud.client.state
  (:require [reagent.core :as r]
            [crud.client.api :as api]
            [cljs.core.async :refer [take!]]))

(def data (r/atom nil))
(def popup? (r/atom nil))
(def selected-entry (r/atom nil))

(defn fetch-data []
  (take! (api/list) #(reset! data %)))

(defn reset-form []
  (reset! selected-entry nil)
  (reset! popup? nil))

(defn set-form [entry flag]
  (when @popup? ; reset the form inputs to default
    (-> "form" js/document.getElementById .reset))
  (reset! selected-entry entry)
  (reset! popup? flag))
