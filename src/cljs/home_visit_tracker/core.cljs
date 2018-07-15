(ns home-visit-tracker.core
  (:require [enfocus.core :as ef]
            [enfocus.events :as ev]))

(defn ^:export init []
  "Just testing out what ClojureScript interaction looks like"
  (ef/at "#button1"
         (ev/listen :click #(js/alert "bye!")))
  (ef/at "#button2"
         (ev/listen :click #(.log js/console "Button 2 was clicked!")))
  (ef/at "#button3"
         (ev/listen :click #(ef/at "#status" (ef/set-style :font-size "500%")))))
