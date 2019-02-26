(ns solar-test.server
  (:gen-class)
  (:require [solar-test.workers :refer [async-search!]]
            [solar-test.conversions :refer [tag-stats]]
            [solar-test.config :refer [config]]
            [compojure.core :refer [defroutes GET wrap-routes]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defn- handle
  [{:keys [tag] :as req}]
  (->
    tag
    list
    flatten
    async-search!
    tag-stats))

(defroutes api
  (GET "/search"
       {params :params}
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (handle params)}))

(defn run []
  (let [{:keys [server-port]} config]
    (run-jetty
     (->
       api
       (wrap-routes wrap-defaults api-defaults)
       (wrap-json-response))
     {:port server-port})))
