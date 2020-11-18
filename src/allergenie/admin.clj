(ns allergenie.admin
  (:require [config.core :refer [env]]))

(def admin-login (or (System/getenv "ADMIN_LOGIN")
                     (:admin-login env)))
(def admin-password (or (System/getenv "ADMIN_PASS")
                        (:admin-pass env)))

(defn check-login [login password]
  (and (= login admin-login)
       (= password admin-password)))
