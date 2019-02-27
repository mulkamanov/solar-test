(ns solar-test.config
  (:gen-class)
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [mount.core :refer [defstate]]))

;; Base fields.
(s/def ::server-port int?)
(s/def ::max-connections int?)
(s/def ::search-timeout int?)
(s/def ::worker-timeout int?)

;; Target API fields.
(s/def ::endpoint string?)
(s/def ::pagesize int?)
(s/def ::sort #{"creation"})
(s/def ::order #{"asc" "desc"})
(s/def ::site string?)
(s/def ::target-api (s/keys :req-un [::endpoint ::pagesize]
                            :opt-un [::sort ::order ::site]))

(s/def ::config (s/keys :req-un [::server-port ::max-connections
                                 ::search-timeout ::worker-timeout
                                 ::target-api]))

(defn- read-conf []
  (->
    (io/resource "config.edn")
    slurp
    edn/read-string))

(defn- check-conf [conf]
  (s/conform ::config conf))

(defstate config
  :start
  (->
    (read-conf)
    check-conf))
