(ns jdbc-component.core
  (:require
   [com.stuartsierra.component :as component]
   [jdbc-component.db :refer [make-database with-connection with-transaction]]
   [jdbc-component.users :as users])
  (:gen-class))


(defn -main [& args]
  (let [{db :db, :as system}
        (-> (component/system-map :db (make-database))
            (component/start-system))]

    ;; Use query functions with the component:
    (println (users/read-all db))

    ;; Create a connection from the component and use it with the query functions:
    (with-connection [conn db]
      (users/create! conn {:username "alice", :email "alice@example.org", :password "top"})
      (println (users/read-all conn)))

    ;; Same with a transaction:
    (with-transaction [tx db]
      (users/create! tx {:username "bob", :email "bob@example.org", :password "secret"})
      (println (users/read-all tx)))

    (component/stop-system system)))
