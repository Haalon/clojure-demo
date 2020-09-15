(ns crud.client.core
  (:require [crud.client.events]
            [crud.client.views]
            [crud.client.subs]
            [re-frame.core :as rf]
            [reagent.dom :as rdom]))

(defn ^:export main []
  ; (state/fetch-data)
  (rf/dispatch-sync [:init-db])
  (rf/dispatch [:fetch-data])
  (rdom/render
   [crud.client.views/app]
   (js/document.getElementById "app")))

(main)
