(ns solar-test.config
  (:gen-class)
  (:require [clojure.edn :as edn]
            [mount.core :refer [defstate]]))

(defstate config
  :start (edn/read-string (slurp (clojure.java.io/resource "config.edn"))))
