(ns crud.core-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [crud.server.core :refer :all]
            [crud.server.model :as model]
            [crud.server.web :as web]
            [ring.mock.request :as mock]))

(defn db-fixture [f]
  (with-redefs [model/db-name "test"]
    (model/migrate)
    (f)
    (model/drop-table)))

(use-fixtures :each db-fixture)

(defn respond-to [method url body]
  (let [req_empty (mock/request method url)
        req (if body
              (mock/json-body req_empty body)
              req_empty)]
    (web/app req)))

(def test-person {"name" "bob"
                  "birth" "2000-01-01"
                  "insurance" "0000000000000000"
                  "sex" "male"
                  "address" "nowhere"})

(def test-person2 {"name" "alice"
                   "birth" "2000-01-01"
                   "insurance" "0000000000000001"
                   "sex" "female"
                   "address" "somewhere"})

(deftest empty-list-test
  (testing "Read empty database"
    (let [res (respond-to :get "/api" nil)]
      (is (= (:body res) "[]"))
      (is (= (:status res) 200)))))

(deftest success-test
  (testing "Create resource"
    (let [res (respond-to :post "/api" test-person)]
      (is (= (:status res) 200)))
    (let [res (respond-to :get "/api" nil)]
      (is (= (:status res) 200))
      (is (= (-> res :body json/read-str
                 first (dissoc "id"))
             test-person))))

  (testing "Update resource"
    (let [res (respond-to :put "/api/1" test-person2)]
      (is (= (:status res) 200)) "Update success")
    (let [res (respond-to :get "/api" nil)]
      (is (= (:status res) 200))
      (is (= (-> res :body json/read-str
                 first (dissoc "id"))
             test-person2))))

  (testing "Delete resource"
    (let [res (respond-to :delete "/api/1" nil)]
      (is (= (:status res) 200) "Delete success"))
    (empty-list-test)))

(deftest fail-test
  (testing "Exception"
    (respond-to :post "/api" test-person)
    (respond-to :post "/api" test-person2)
    ;; try to put same insurance id
    (let [res (respond-to :put "/api/1" (select-keys test-person2 ["insurance"]))]
      (is (= (:status res) 400)) "Failed updated due unique field dupe")))


