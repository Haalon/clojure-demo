(ns crud.client.core
  (:require [reagent.dom :as rdom]
            [crud.client.state :as state]
            [crud.client.table :refer [table]]
            [crud.client.form :refer [form]]))

(defn app []
  [:div.container
   (if @state/data
     (table @state/data)
     [:h2 "Loading..."])
   (when @state/popup? (form @state/selected-entry))])

(defn ^:export main []
  (state/fetch-data)
  (rdom/render
   [app]
   (js/document.getElementById "app")))

(main)
