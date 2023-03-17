(ns jdbc-component.users
  (:require
   [hugsql.core :as hugsql]))

(declare db-create
         db-read-all
         db-read-one-by-username)
(hugsql/def-db-fns "sql/users.sql")


(defn create! [connectable user]
  (db-create connectable user))


(defn read-one-by-username [connectable username]
  (db-read-one-by-username connectable {:username username}))


(defn read-all [connectable]
  (db-read-all connectable))
