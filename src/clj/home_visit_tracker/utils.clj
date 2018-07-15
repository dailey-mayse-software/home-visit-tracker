(ns home-visit-tracker.utils)

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn reflect-fn-name
  "Get the name of a function:
    (reflect-fn-name #'foo)"
  [fn]
  (-> fn meta :name str))
