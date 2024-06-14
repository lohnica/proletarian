(ns example-a.enqueue-jobs
  (:require [examples]
            [next.jdbc :as jdbc]
            [proletarian.job :as job]
            [puget.printer :as puget])
  (:import (java.time Instant)))

(defn run
  [_]
  (let [ds (jdbc/get-datasource (:jdbc-url examples/config))]
    (examples/preamble ds)
    (println "Adding new job to :proletarian/default queue:")
    (let [conn (jdbc/get-connection ds)
          job-type ::echo
          company-id (random-uuid)
          payload {:message "Hello world!"
                   :timestamp (Instant/now)}
          job-id (job/enqueue! conn company-id job-type payload)]
      (puget/cprint {:job-id job-id
                     :company-id company-id
                     :job-type ::echo
                     :payload payload}))))

(defn handle-job!
  [job-type payload]
  (println (str "Running job " job-type ". Payload:"))
  (puget/cprint payload))
