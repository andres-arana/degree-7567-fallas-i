(ns app.core
  (:require [clara.rules :refer :all]
            [clara.tools.inspect :as inspect]))

(defrecord Bait [kind])

(defrecord Line [size])

(defrecord Depth [level])

(defrecord ExpectedCatch [kind])

(defrecord RequiredEquipment [kind value])

(defrule catch-pejerrey
  [Bait (= :mojarra kind)]
  [Depth (= :surface level)]
  [Line (= 0.2 size)]
  =>
  (insert! (->ExpectedCatch :pejerrey)
           (->RequiredEquipment :weights :none)
           (->RequiredEquipment :bait :mojarra)
           (->RequiredEquipment :line 0.2)))

(defrule catch-boga
  [Bait (= ?kind kind)]
  [Line (= 0.2 size)]
  [Depth (= :underwater level)]
  [:test (contains? #{:masa :decoy} ?kind)]
  =>
  (insert! (->ExpectedCatch :boga)
           (->RequiredEquipment :weights :heavy)
           (->RequiredEquipment :bait ?kind)
           (->RequiredEquipment :line 0.2)))

(defquery expected-catch []
  [?catch <- ExpectedCatch])

(defquery required-equipment []
  [?equipment <- RequiredEquipment])

(def empty-session (mk-session 'app.core))

(defn evaluate-query
  [q facts]
  (-> empty-session
      (insert-all facts)
      (fire-rules)
      (query q)
      (distinct)))

