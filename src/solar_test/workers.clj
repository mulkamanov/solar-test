(ns solar-test.workers
  (:gen-class)
  (:require [clojure.core.async :refer [chan timeout go-loop pub sub unsub
                                        onto-chan alt!! close! <! >!]
                                :as async]
            [mount.core :refer [defstate]]
            [solar-test.config :refer [config]]
            [solar-test.requester :refer [request]]))

(defn- search-with-timeout!
  [tag]
  (->
    tag
    request
    future
    (deref (:search-timeout config) [])))

(defn- search!
  [{:keys [tag] :as req}]
  (->>
    tag
    search-with-timeout!
    (assoc req :items)))

(defn- start-pool!
  "Start max-connections threads that will consume work
  from the in-chan and put the results into the out-chan."
  [f]
  (let [in-chan (chan)
        out-chan (chan)]
    (dotimes [_ (:max-connections config)]
      (go-loop [req (<! in-chan)]
        (if (nil? req)
          (close! out-chan)
          (do
            (->>
              req
              f
              (>! out-chan))
            (recur (<! in-chan))))))
    {:in-chan in-chan
     :out-chan out-chan
     :out-pub (pub out-chan :uuid)}))

(defstate pool-of-workers
  :start (start-pool! search!)
  :stop (-> pool-of-workers
            :in-chan
            (close!)))

(defn async-search!
  "Performs an async search for all tags in the list."
  [tags]
  (let [{:keys [in-chan out-chan out-pub]} pool-of-workers
        uuid (java.util.UUID/randomUUID)
        mid-chan (chan)
        timeout-chan (timeout (:worker-timeout config))]
    (sub out-pub uuid mid-chan)
    (onto-chan in-chan
               (map #(hash-map :uuid uuid :tag %) tags)
               false)
    (loop [res []]
      (->
        tags
        count
        (async/take mid-chan)
        (alt!! ([r] r) timeout-chan nil)
        (as->
          $
          (if $
            (recur (conj res $))
            (do
              (unsub out-pub uuid mid-chan)
              (close! mid-chan)
              res)))))))
