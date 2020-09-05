(ns crud.core-test
  (:require [clojure.test :refer :all]
            [crud.server.core :refer :all]
            [crud.server.web :as web]
            [ring.mock.request :as mock]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 0))))

(deftest list-test
  (testing "List api route"
    (let [req (mock/request :get "/api")
          res (web/app req)]
      (is res))))
