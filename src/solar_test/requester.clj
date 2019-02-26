(ns solar-test.requester
  (:gen-class)
  (:require [clj-http.client :as client]
            [solar-test.config :refer [config]]))

(defn- prepare-res
  [{:keys [tags answer_count]}]
  {:tags tags
   :answered answer_count})

(defn request
  "Requests a json with search results."
  [tag]
  (->
    config
    :target-api
    (select-keys [:pagesize :sort :order :site])
    (assoc :tagged tag)
    (->>
     (assoc {:accept :json :as :json} :query-params)
     (client/get (get-in config [:target-api :endpoint]))
     :body
     :items
     (map prepare-res))))
