(ns crud.client.table
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [crud.client.api :as api]
            [crud.client.state :as state]))

(defn del-btn [id]
  [:button
   {:on-click #(go
                 (<! (api/delete id))
                 (state/fetch-data))}
   "X"])

(defn edit-btn [entry]
  [:button
   {:on-click #(state/set-form entry true)}
   "edit"])

(defn refresh-btn []
  [:button
   {:on-click #(state/fetch-data)}
   "refresh"])

(defn add-btn []
  [:button
   {:on-click #(state/set-form nil true)}
   "add"])

(defn line [mmap header?]
  ^{:key (:id mmap)}
  [:tr
   (for [k (keys mmap)]
     ^{:key (name k)}
     [(if header? :th :td) (get mmap k)])
   ^{:key "control"}
   (if header?
     [:th ""]
     [:td
      (del-btn (:id mmap))
      (edit-btn mmap)])])

(defn table-body [arrmap]
  (when arrmap
    (let [fields (->> arrmap first keys (map name))
          fieldmap (zipmap fields fields)]
      [:table
       [:thead (line fieldmap true)]
       [:tbody (for [m arrmap]  (line m false))]])))

(defn table [arrmap]
  [:div.container
   [:div.row.container
    (add-btn)
    (refresh-btn)]
   (table-body arrmap)])