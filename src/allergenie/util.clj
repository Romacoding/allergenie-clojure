(ns allergenie.util)

(defn calc-wind-dir 
  "Function to calculate a wind direction from degrees"
  [deg]
  (let [val (Math/floor (+ (/ deg 22.5) 0.5))
        arr ["&#8593 N"
             "&#8599 NNE"
             "&#8599 NE"
             "&#8599 ENE"
             "&#8594 E"
             "&#8600 ESE"
             "&#8600 SE"
             "&#8600 SSE"
             "&#8595 S"
             "&#8601 SSW"
             "&#8601 SW"
             "&#8601 WSW"
             "&#8592 W"
             "&#8598 WNW"
             "&#8598 NW"
             "&#8598 NNW"]]
    (nth arr (mod val 16))))

(defn calc-pollen-level 
  "Function to calculate a pollen level from the index value"
  [index]
  (cond
    (> index 9.6) "High"
    (> index 7.2) "Medium High"
    (> index 4.8) "Medium"
    (> index 2.4) "Low Medium"
    :else "Low"))

(defn calc-pollen-color 
  "Function to calculate a progress bar color from the index value"
  [index]
  (cond
    (> index 9.6) "is-danger"
    (> index 4.8) "is-warning"
    :else "is-success"))
