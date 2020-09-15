(ns crud.client.subs
  (:require [re-frame.core :as rf]))

;; subscriptions

(rf/reg-sub
 :data
 (fn [db _]
   (:data db)))

(rf/reg-sub
 :selected-entry
 (fn [db _]
   (:selected-entry db)))

(rf/reg-sub
 :show-form
 (fn [db _]
   (:show-form db)))

