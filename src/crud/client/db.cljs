(ns crud.client.db)

(def db-default {:data {}
                 :show-form false
                 :selected-entry nil})

(def sexes '("male" "female" "not applicable"))
(def fields '("name" "insurance" "sex" "birth" "address"))
