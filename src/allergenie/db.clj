(ns allergenie.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer [$set]]
            [config.core :refer [env]])
  (:import
    [org.bson.types ObjectId]))

(def db-connection-uri (or (System/getenv "MONGO_URI")
                           (:mongo-uri env)))

(def db (-> db-connection-uri
            mg/connect-via-uri
            :db))

(def record-coll "journal")

(defn create-record [title body location pollen-index weather air-index triggers]
  (mc/insert db record-coll
             {:title   title
              :body    body
              :created (new java.util.Date)
              :location location
              :pollen-index pollen-index
              :air-index air-index
              :weather weather
              :triggers triggers}))

(defn update-record [rec-id title body]
  (mc/update-by-id db record-coll (ObjectId.^String rec-id)
                   {$set
                    {:title title
                     :body body}}))

(defn delete-record [rec-id]
  (mc/remove-by-id db record-coll (ObjectId.^String rec-id)))

(defn list-records []
  (mc/find-maps db record-coll))

(defn get-record-by-id [rec-id]
  (mc/find-map-by-id db record-coll (ObjectId.^String rec-id)))

