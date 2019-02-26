(ns solar-test.core
  (:gen-class)
  (:require [mount.core :as mount]
            [solar-test.server :as server]
            [solar-test.workers :as workers]))

(defn -main [& args]
  (mount/start)
  (server/run))
