(ns solar-test.conversions
  (:gen-class))

(defn- calc-total
  [tag items]
  (->>
    items
    (mapcat :tags)
    (filter #{tag})
    count))

(defn- calc-ans
  [items]
  (->>
    items
    (map :answered)
    (reduce +)))

(defn- prepare-tag
  [{:keys [items tag]}]
  [tag
   {:total (calc-total tag items)
    :answered (calc-ans items)}])

(defn tag-stats
  "Calculate statistics separately for each tag."
  [res]
  (->>
    res
    (map prepare-tag)
    (into {})))
