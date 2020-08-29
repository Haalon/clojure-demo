(ns crud.client.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<! take!]]))

(def url js/window.location.href)
(def url-api (str url "api"))
(defn url-api-item [id] (str url "api/" id))

(defn request [method address body]
  (go (let [response (<! (method address {:json-params body}))
            status (:status response)
            body (:body response)]
        (when (not= status 200) (js/alert body))
        (prn body)
        body)))

(defn list [] (request http/get url-api {}))
(defn add [entry] (request http/post url-api entry))
(defn delete [id] (request http/delete (url-api-item id) {}))
(defn update [id entry] (request http/put (url-api-item id) entry))

(def data (r/atom nil))
(def popup (r/atom nil))
(def selected-entry (r/atom nil))

(def sexes '("male" "female" "not applicable"))
(def fields '("name" "insurance" "sex" "birth" "address"))

(defn fetch-data []
  (take! (list) #(reset! data %)))

(defn del-btn [id]
  [:button
   {:on-click #(go
                 (prn "deleting" id)
                 (<! (delete id))
                 (fetch-data))}
   "X"])

(defn edit-btn [entry]
  [:button
   {:on-click #(do
                 (reset! selected-entry entry)
                 (reset! popup true))}
   "edit"])

(defn refresh-btn [entry]
  [:button
   {:on-click #(fetch-data)}
   "refresh"])

(defn add-btn []
  [:button
   {:on-click #(do
                 (reset! selected-entry nil)
                 (reset! popup true))}
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

(defn table [arrmap]
  (when arrmap
    (let [fields (->> arrmap first keys (map name))
          fieldmap (zipmap fields fields)]
      [:table
       [:thead (line fieldmap true)]
       [:tbody (for [m arrmap]  (line m false))]])))

(defn send-data [event]
  (.preventDefault event)
  (let [elems (-> event .-target .-elements)
        values (map #(->> % (aget elems) .-value) fields)
        valmap (zipmap fields values)
        filtered (filter #(-> % second empty? not) valmap)
        valmap (into {} filtered)
        id (:id @selected-entry)] ; will be nil if entry is nil
    (go
      (<! (if id
            (update id valmap)
            (add valmap)))
      (fetch-data)
      (reset-form))))

(defn reset-form []
  (reset! popup nil)
  (reset! selected-entry nil))

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
     {:on-click reset-form
      :type "reset"}
     "cancel"]]])

(defn app []
  [:div.container
   [:div.row.container
    (add-btn)
    (refresh-btn)]
   (if @data
     (table @data)
     [:h2 "Loading..."])
   (when @popup (form @selected-entry))])

(defn ^:export main []
  (fetch-data)
  (rdom/render
   [app]
   (js/document.getElementById "app")))

(main)

; (prn (js/document.getElementById "app"))

; (update 8 {:name "Arthur"})