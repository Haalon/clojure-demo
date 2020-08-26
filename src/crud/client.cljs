(ns crud.client
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

(defn line [mmap header?]
  ^{:key (:id mmap)} 
  [:tr
    (for [k (keys mmap)] 
      ^{:key (name k)}
      [(if header? :th :td) (get mmap k)])])

(defn table [arrmap]
  (when arrmap
    (let [fields (->> arrmap first keys (map name))
          fieldmap (zipmap fields fields)]
      [:table
        [:thead (line fieldmap true)]        
        [:tbody (for [m arrmap]  (line m false))]])))

(def data (r/atom nil))

(defn fetch-data []
  (take! (list) #(reset! data %)))

(defn app [] (if @data 
  (table @data)
  [:h2 "Loading..."]))

(defn ^:export main []
  (fetch-data)
  (rdom/render
    [app]
    (js/document.getElementById "app")))

(main)

; (prn (js/document.getElementById "app"))

; (update 3 {:name "Biggus Dickus"})