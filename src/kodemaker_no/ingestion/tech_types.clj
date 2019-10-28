(ns kodemaker-no.ingestion.tech-types)

(defn create-tx [file-name id->type]
  (for [[id type] id->type]
    {:db/ident (keyword "tech" (name id))
     :tech/type type}))
