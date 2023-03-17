(defproject jdbc-component "0.1.0-SNAPSHOT"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[com.github.seancorfield/next.jdbc "1.3.862"]
                 [com.layerware/hugsql "0.5.3"]
                 [com.layerware/hugsql-adapter-next-jdbc "0.5.3"]
                 [com.stuartsierra/component "1.1.0"]
                 [dev.weavejester/ragtime "0.9.3"]
                 [environ "1.2.0"]
                 [org.clojure/clojure "1.11.1"]
                 [org.postgresql/postgresql "42.5.4"]]
  :plugins [[lein-environ "1.2.0"]]
  :main ^:skip-aot jdbc-component.core
  :target-path "target/%s"
  :profiles {:dev {:env {:postgres-host "localhost"
                         :postgres-password "password"
                         :postgres-port "5432"
                         :postgres-database "jdbc-component"
                         :postgres-user "user"}}
             :uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
