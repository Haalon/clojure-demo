(ns demo_clj.views.layout
  (:require [hiccup.page :as h]))

(defn common [title & body]
  (h/html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
    [:meta {:name "viewport" :content
            "width=device-width, initial-scale=1, maximum-scale=1"}]
    [:title title]
    (h/include-css "base.css")
    (h/include-js "app.js")]
   [:body
    [:div {:id "header"}
     [:h1 {:class "container"} title]]
    [:div {:id "content" :class "container"} body]]))


(defn line [elems header?]
  (h/html5 [:tr
    (for [e elems] [(if header? :th :td) e])]))

(defn table [arrmap]
  (when arrmap
    (let [fields (->> arrmap first keys (map name))
          values (map vals arrmap)]
      (h/html5 [:table
                (line fields true)
                (for [l values] (line l false))]))))

