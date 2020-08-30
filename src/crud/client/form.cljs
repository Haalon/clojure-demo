(ns crud.client.form
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [crud.client.api :as api]
            [crud.client.state :as state]))


(def sexes '("male" "female" "not applicable"))
(def fields '("name" "insurance" "sex" "birth" "address"))

(defn send-data [event]
  (.preventDefault event)
  (let [elems (-> event .-target .-elements)
        values (map #(->> % (aget elems) .-value) fields)
        valmap (zipmap fields values)
        filtered (filter #(-> % second empty? not) valmap)
        valmap (into {} filtered)
        id (:id @state/selected-entry)] ; will be nil if entry is nil
    (go
      (<! (if id
            (api/update id valmap)
            (api/add valmap)))
      (state/fetch-data)
      (state/reset-form))))

(defn form [entry]
  (prn entry (-> entry boolean not))
  [:form.floating.container {:on-submit send-data}
    ; [:h1 "fuck css"]
   [:div.row
    [:label {:for "name"} "Name surname"]
    [:input#name {:name "name"
                  :default-value (:name entry "")
                  :required (-> entry boolean not)}]]
   [:div.row
    [:label {:for "insurance"} "Insurance ID"]
    [:input#insurance {:name "insurance"
                       :pattern "[0-9]{16}"
                       :max-length "16"
                       :placeholder "0000000000000000"
                       :default-value (:insurance entry "")
                       :required (-> entry boolean not)
                       :title "16 number insurance ID"}]]
   [:div.row
    [:label {:for "sex"} "Sex"]
    [:select#sex {:name "sex"
                  :required (-> entry boolean not)
                  :default-value (:sex entry "male")}
     (for [sex sexes] [:option {:default-value sex
                                :key sex}
                       sex])]]
   [:div.row
    [:label {:for "birth"} "Date of birth"]
    [:input#birth {:type "date"
                   :name "birth"
                   :default-value (:birth entry "2000-01-01")
                   :required (-> entry boolean not)}]]
   [:div.row
    [:label {:for "address"} "Address"]
    [:input#address {:name "address"
                     :default-value (:address entry "")
                     :required (-> entry boolean not)}]]
   [:div.row
    [:input
     {:type "submit"
      :default-value "submit"}]
    [:button
     {:on-click state/reset-form
      :type "reset"}
     "cancel"]]])