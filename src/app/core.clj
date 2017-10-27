(ns app.core
  (:require [clara.rules :refer :all]
            [clara.tools.inspect :as inspect]))

(defrecord ExpectedCatch [kind])

(defrecord RequiredEquipment [kind value])

(defrecord RequiredDepth [depth])

(defrecord FishSize [size])

(defrecord FishAgressiveness [level])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Generic facts
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defrule surface-depth
  [RequiredDepth (= :surface depth)]
  =>
  (insert! (->RequiredEquipment :weights :no)))

(defrule middle-depth
  [RequiredDepth (= :middle depth)]
  =>
  (insert! (->RequiredEquipment :weights :light)))

(defrule deep-depth
  [RequiredDepth (= :deep depth)]
  =>
  (insert! (->RequiredEquipment :weights :heavy)))

(defrule reel-size
  [FishSize (= ?size size)]
  =>
  (insert! (->RequiredEquipment :reel ?size)))

(defrule pole-size-small-fish
  [FishSize (= :small size)]
  =>
  (insert! (->RequiredEquipment :pole 4.0)))

(defrule pole-size-medium-quiet-fish
  [FishSize (= :medium size)]
  [FishAgressiveness (= :low level)]
  =>
  (insert! (->RequiredEquipment :pole 2.5)))

(defrule pole-size-medium-aggresive-fish
  [FishSize (= :medium size)]
  [FishAgressiveness (= :high level)]
  =>
  (insert! (->RequiredEquipment :pole 1.5)))

(defrule pole-size-large-fish
  [FishSize (= :large size)]
  =>
  (insert! (->RequiredEquipment :pole 1.5)))

(defrule pole-telescopic
  [RequiredEquipment (= :pole kind) (= ?length value)]
  =>
  (insert! (->RequiredEquipment :telescopic-pole (> ?length 3))))

(defrule hook-small
  [FishSize (= :small size)]
  =>
  (insert! (->RequiredEquipment :hook 3)))

(defrule hook-medium
  [FishSize (= :medium size)]
  =>
  (insert! (->RequiredEquipment :hook 7)))

(defrule hook-large
  [FishSize (= :large size)]
  =>
  (insert! (->RequiredEquipment :hook 10)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Pejerrey
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defrule required-depth-pejerrey
  [ExpectedCatch (= :pejerrey kind)]
  =>
  (insert! (->RequiredDepth :surface)))

(defrule required-equipment-pejerrey
  [ExpectedCatch (= :pejerrey kind)]
  =>
  (insert! (->RequiredEquipment :bait :mojarra)
           (->RequiredEquipment :line 0.2)))

(defrule size-pejerrey
  [ExpectedCatch (= :pejerrey kind)]
  =>
  (insert! (->FishSize :small)))

(defrule agressiveness-pejerrey
  [ExpectedCatch (= :pejerrey kind)]
  =>
  (insert! (->FishAgressiveness :high)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Boga
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defrule required-depth-boga
  [ExpectedCatch (= :boga kind)]
  =>
  (insert! (->RequiredDepth :deep)))

(defrule required-equipment-boga
  [ExpectedCatch (= :boga kind)]
  =>
  (insert! (->RequiredEquipment :bait :masa)
           (->RequiredEquipment :line 0.2)))

(defrule size-boga
  [ExpectedCatch (= :boga kind)]
  =>
  (insert! (->FishSize :medium)))

(defrule agressiveness-boga
  [ExpectedCatch (= :boga kind)]
  =>
  (insert! (->FishAgressiveness :low)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Surubi
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defrule required-depth-surubi
  [ExpectedCatch (= :surubi kind)]
  =>
  (insert! (->RequiredDepth :deep)))

(defrule required-equipment-surubi
  [ExpectedCatch (= :surubi kind)]
  =>
  (insert! (->RequiredEquipment :bait :anguila)
           (->RequiredEquipment :line 0.4)))

(defrule size-surubi
  [ExpectedCatch (= :surubi kind)]
  =>
  (insert! (->FishSize :large)))

(defrule agressiveness-surubi
  [ExpectedCatch (= :surubi kind)]
  =>
  (insert! (->FishAgressiveness :high)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Tararira
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defrule required-depth-tararira
  [ExpectedCatch (= :tararira kind)]
  =>
  (insert! (->RequiredDepth :middle)))

(defrule required-equipment-tararira
  [ExpectedCatch (= :tararira kind)]
  =>
  (insert! (->RequiredEquipment :bait :mojarra)
           (->RequiredEquipment :line 0.3)))

(defrule size-tararira
  [ExpectedCatch (= :tararira kind)]
  =>
  (insert! (->FishSize :medium)))

(defrule agressiveness-tararira
  [ExpectedCatch (= :tararira kind)]
  =>
  (insert! (->FishAgressiveness :high)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Query interface
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
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

(defn required-equipment?
  [expected-catch]
  (evaluate-query required-equipment [(->ExpectedCatch expected-catch)]))


