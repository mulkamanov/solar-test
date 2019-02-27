(ns solar-test.core
  (:gen-class)
  (:require [mount.core :as mount]
            [solar-test.server :as server]))

(defn- start []
  (mount/start)
  :started)

(defn- stop []
  (mount/stop)
  :stoped)

(defn -main [& args]
  (mount/start)
  (server/run))
