(ns crud.client.views
  (:require [crud.client.db :as db]
            [re-frame.core :as rf]))

(defn del-btn [id]
  [:button
   {:on-click #(rf/dispatch [:delete-data id])}
   "X"])

(defn edit-btn [entry]
  [:button
   {:on-click #(rf/dispatch [:show-form entry])}
   "edit"])

(defn refresh-btn []
  [:button
   {:on-click #(rf/dispatch [:fetch-data])}
   "refresh"])

(defn add-btn []
  [:button
   {:on-click #(rf/dispatch [:show-form nil])}
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

;; form elements

(defn name-input [entry]
  [:div.row
   [:label {:for "name"} "Name surname"]
   [:input#name {:name "name"
                 :default-value (:name entry "")
                 :required (-> entry boolean not)}]])

(defn insurance-input [entry]
  [:div.row
   [:label {:for "insurance"} "Insurance id"]
   [:input#insurance {:name "insurance"
                      :pattern "[0-9]{16}"
                      :max-length "16"
                      :placeholder "0000000000000000"
                      :default-value (:insurance entry "")
                      :required (-> entry boolean not)
                      :title "16 number insurance id"}]])

(defn sex-input [entry]
  [:div.row
   [:label {:for "sex"} "Sex"]
   [:select#sex {:name "sex"
                 :required (-> entry boolean not)
                 :default-value (:sex entry "male")}
    (for [sex db/sexes] [:option {:default-value sex
                                  :key sex}
                         sex])]])

(defn birth-input [entry]
  [:div.row
   [:label {:for "birth"} "Date of birth"]
   [:input#birth {:type "date"
                  :name "birth"
                  :default-value (:birth entry "2000-01-01")
                  :required (-> entry boolean not)}]])

(defn address-input [entry]
  [:div.row
   [:label {:for "address"} "Address"]
   [:input#address {:name "address"
                    :default-value (:address entry "")
                    :required (-> entry boolean not)}]])

(defn form [entry]
  [:form#form.floating.container
   {:on-submit (fn [e] (.preventDefault e)
                 (rf/dispatch [:submit-form (.-target e)]))}
   (name-input entry)
   (insurance-input entry)
   (sex-input entry)
   (birth-input entry)
   (address-input entry)
   [:div.row
    [:input
     {:type "submit"
      :value "submit"}]
    [:button
     {:on-click #(rf/dispatch [:reset-form])
      :type "reset"}
     "cancel"]]])

(defn app []
  (let [data @(rf/subscribe [:data])
        popup? @(rf/subscribe [:show-form])
        entry @(rf/subscribe [:selected-entry])]
    [:div.container
     (if data
       (table data)
       [:h2 "Loading..."])
     (when popup? (form entry))]))

