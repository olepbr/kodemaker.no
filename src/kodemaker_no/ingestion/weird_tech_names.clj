(ns kodemaker-no.ingestion.weird-tech-names)

(defn create-tx [id->name]
  (for [[id tech-name] id->name]
    {:db/ident (keyword "tech" (name id))
     :tech/name tech-name}))
