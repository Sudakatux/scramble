(ns ^:figwheel-no-load scramble.dev
  (:require
    [scramble.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
