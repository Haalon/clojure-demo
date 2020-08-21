(ns user
  (:require [reloaded.repl :refer [system reset stop]]
            [demo_clj.core :as core]))

(reloaded.repl/set-init! #(core/create-system))
