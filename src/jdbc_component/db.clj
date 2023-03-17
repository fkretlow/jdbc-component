(ns jdbc-component.db
  (:require
   [com.stuartsierra.component :as component]
   [environ.core :refer [env]]
   [hugsql.adapter.next-jdbc]
   [hugsql.core :as hugsql]
   [next.jdbc]
   [next.jdbc.protocols]
   [ragtime.jdbc]
   [ragtime.repl]))


(defn extract-db-spec
  ([] (extract-db-spec env))
  ([env] {:dbtype   "postgresql"
          :dbname   (env :postgres-database)
          :host     (env :postgres-host)
          :port     (or (parse-long (env :postgres-port)) 5432)
          :user     (env :postgres-user)
          :password (env :postgres-password)
          :ssl      false}))


(defn- ->ragtime-config [db-spec]
  {:datastore (ragtime.jdbc/sql-database db-spec)
   :migrations (ragtime.jdbc/load-resources "migrations")})

;; The 0-arity versions can also be used as leiningen aliases.
(defn migrate!
  ([] (migrate! (extract-db-spec)))
  ([db-spec] (ragtime.repl/migrate (->ragtime-config db-spec))))

(defn rollback!
  ([] (rollback! (extract-db-spec)))
  ([db-spec] (ragtime.repl/rollback (->ragtime-config db-spec))))


;; The database component has no state of its own, because it contains only
;; the db-spec. Use the component lifecycle hooks to initialize hugsql with
;; the correct adapter and perform migrations.
(defrecord Database [db-spec options]

  component/Lifecycle
  (start [this]
    (hugsql/set-adapter! (hugsql.adapter.next-jdbc/hugsql-adapter-next-jdbc))
    (when (:migrate? options) (migrate! db-spec))
    this)
  (stop [this] this)

  ;; This allows the component itself to be passed to next-jdbc functions and macros,
  ;; either directly or via hugsql.
  next.jdbc.protocols/Sourceable
  (get-datasource [_] (next.jdbc/get-datasource db-spec)))


(defn make-database
  ([] (make-database (extract-db-spec) {:migrate? true}))
  ([db-spec options] (map->Database {:db-spec db-spec, :options options})))


(defmacro with-transaction [[sym connectable] & body]
  `(next.jdbc/with-transaction [~sym ~connectable]
     ~@body))

(defmacro with-connection [[sym connectable] & body]
  `(with-open [~sym (next.jdbc/get-connection ~connectable)]
     ~@body))
