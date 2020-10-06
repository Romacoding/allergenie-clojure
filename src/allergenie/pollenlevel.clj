(ns allergenie.pollenlevel)
(defn calc-pollen-level [index]
  (cond
    (> index 9.6) "High"
    (> index 7.2) "Medium High"
    (> index 4.8) "Medium"
    (> index 2.4) "Low Medium"
    :else "Low"))