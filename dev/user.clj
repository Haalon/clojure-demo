(ns user
  (:require [reloaded.repl :refer [system reset stop start]]
            [crud.core :as core]))

(reloaded.repl/set-init! #(core/create-system))
