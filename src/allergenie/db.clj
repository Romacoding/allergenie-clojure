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

(defn create-record 
  "Function to create a new record in DB"
  [title body location pollen-index weather air-index triggers]
  (mc/insert db record-coll
             {:title   title
              :body    body
              :created (new java.util.Date)
              :location location
              :pollen-index pollen-index
              :air-index air-index
              :weather weather
              :triggers triggers}))

(defn update-record 
  "Function to update a record in DB"
  [rec-id title body]
  (mc/update-by-id db record-coll (ObjectId.^String rec-id)
                   {$set
                    {:title title
                     :body body}}))

(defn delete-record 
  "Function to delete a record from DB"
  [rec-id]
  (mc/remove-by-id db record-coll (ObjectId.^String rec-id)))

(defn list-records 
  "Function to list records from DB"
  []
  (mc/find-maps db record-coll))

(defn get-record-by-id 
  "Function to get a record by id from DB"
  [rec-id]
  (mc/find-map-by-id db record-coll (ObjectId.^String rec-id)))
