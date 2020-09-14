(ns crud.client.events
  (:require [day8.re-frame.http-fx]
            [ajax.core :refer [json-request-format json-response-format]]
            [re-frame.core :as rf]))

(def url js/window.location.href)
(def url-api (str url "api"))
(defn url-api-item [id] (str url "api/" id))

(rf/reg-event-db
 :init-db
 (fn [_ _]
   {:data {}
    :show-form false
    :selected-entry nil}))

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

;; event handlers

(rf/reg-event-db
 :show-form
 (fn [db [_ entry]]
   (assoc db :show-form true :selected-entry entry)))

(rf/reg-event-db
 :reset-form
 (fn [db _]
   (assoc db :show-form false :selected-entry nil)))

(def fields '("name" "insurance" "sex" "birth" "address"))

(rf/reg-event-fx
 :submit-form
 (fn [{db :db} [_ event]]
   (let [elems (-> event .-elements) ;all form elements
         values (map #(->> % (aget elems) .-value) fields)
         valmap (zipmap fields values)
         filtered (filter #(-> % second empty? not) valmap)
         valmap (into {} filtered)
         id (-> db :selected-entry :id)]
     {:http-xhrio
      {:method (if id :put :post)
       :uri (if id (url-api-item id) url-api)
       :params valmap
       :format (json-request-format)
       :response-format (json-response-format {:keywords? true})
       :on-success [:submit-success]
       :on-failure [:request-failure]}})))

(rf/reg-event-fx
 :submit-success
 (fn [_ _]
   {:dispatch-n [[:reset-form] [:fetch-data]]}))

(rf/reg-event-fx
 :fetch-data
 (fn [_ _]
   {:http-xhrio {:method :get
                 :uri url-api
                 :response-format (json-response-format {:keywords? true})
                 :on-success [:success-fetch-data]
                 :on-failure [:request-failure]}}))

(rf/reg-event-db
 :success-fetch-data
 (fn [db [_ result]]
   (assoc db :data result)))

(rf/reg-event-fx
 :delete-data
 (fn [_ [_ id]]
   {:http-xhrio {:method :delete
                 :uri (url-api-item id)
                 :format (json-request-format)
                 :response-format (json-response-format {:keywords? true})
                 :on-success [:fetch-data]
                 :on-failure [:request-failure]}}))

(rf/reg-event-fx
 :request-failure
 (fn [_ [_ res]]
   (js/alert res)))
