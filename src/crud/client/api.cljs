(ns crud.client.api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(def url js/window.location.href) ;js/window.location.href
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
