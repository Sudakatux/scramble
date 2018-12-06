(ns scramble.middleware
    (:require [ring.middleware.content-type :refer [wrap-content-type]]
              [ring.middleware.params :refer [wrap-params]]
              [prone.middleware :refer [wrap-exceptions]]
              [ring.middleware.reload :refer [wrap-reload]]
              [reitit.ring.coercion :as rrc]
              [muuntaja.core :as m]
              [reitit.ring.middleware.muuntaja :as muuntaja]
              [ring.middleware.defaults :refer [site-defaults wrap-defaults]]))

(def middleware
  [
   #(wrap-defaults % site-defaults)
   wrap-exceptions
   wrap-reload
   ])

(def api-middleware
  [
     wrap-params
     muuntaja/format-middleware
     rrc/coerce-exceptions-middleware
     rrc/coerce-request-middleware
     rrc/coerce-response-middleware
   ]
  )
