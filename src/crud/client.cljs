(ns crud.client
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(def url js/window.location.href)
(def url-api (str url "api"))
(defn url-api-item [id] (str url "api/" id))


(defn list []
  (go (let [response (<! (http/get url-api))
            status (:status response)
            body (:body response)]
    (prn body))))

(defn add [entry]
  (go (let [response (<! (http/post url-api {:json-params entry}))
            status (:status response)
            body (:body response)]
    (prn response))))

(list)
(prn (js/JSON.stringify (clj->js {:name "Ivan"
      :sex "male"
      :insurance "1234567890098765"
      :birth "1990-11-02"
      :address "Moscow"})))
; (add {:name "Ivan"
;       :sex "male"
;       :insurance "1234567890098766"
;       :birth "1990-11-02"
;       :address "Moscow"})
; (list)