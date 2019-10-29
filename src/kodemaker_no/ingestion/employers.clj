(ns kodemaker-no.ingestion.employers)

(defn create-tx [file-name id->name]
  (for [[id employer-name] id->name]
    {:db/ident (keyword "employer" (name id))
     :employer/name employer-name}))
