(ns crud.client.state
  (:require [reagent.core :as r]
            [crud.client.api :as api]
            [cljs.core.async :refer [<! take!]]))

(def data (r/atom nil))
(def popup? (r/atom nil))
(def selected-entry (r/atom nil))

(defn fetch-data []
  (take! (api/list) #(reset! data %)))

(defn set-form [entry flag]
  (reset! selected-entry entry)
  (reset! popup? flag))

(defn reset-form [] (set-form nil nil))