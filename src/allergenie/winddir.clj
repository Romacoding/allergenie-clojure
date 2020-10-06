(ns allergenie.winddir)
(defn calc-wind-dir [deg]
  (let [val (Math/floor (+ (/ deg 22.5) 0.5))
        arr ["N"
             "NNE"
             "NE"
             "ENE"
             "E"
             "ESE"
             "SE"
             "SSE"
             "S"
             "SSW"
             "SW"
             "WSW"
             "W"
             "WNW"
             "NW"
             "NNW"]]
    (nth arr (mod val 16))))