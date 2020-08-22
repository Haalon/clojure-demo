(ns user
  (:require [reloaded.repl :refer [system reset stop]]
            [crud.core :as core]))

(reloaded.repl/set-init! #(core/create-system))
