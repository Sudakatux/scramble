(ns scramble.handler
  (:require [reitit.ring :as reitit-ring]
            [reitit.coercion.spec]
            [muuntaja.core :as m]
            [scramble.middleware :refer [middleware api-middleware]]
            [scramble.util :refer [scramble?]]
            [hiccup.page :refer [include-js include-css html5]]
            [config.core :refer [env]]))

(def mount-target
  [:div#app
   [:h2 "Welcome to scramble"]
   [:p "please wait while Figwheel is waking up ..."]
   [:p "(Check the js console for hints if nothing exсiting happens.)"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css "/css/bootstrap.min.css")])

(defn loading-page []
  (html5
   (head)
   [:body {:class "body-container"}
    mount-target
    (include-js "/js/app.js")]))

(defn index-handler
  [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (loading-page)})

(defn scramble-handler
  [{{{:keys [chars word]} :query} :parameters}]
   {:status 200
     :body {:result (scramble? chars word)} })

(def app
  (reitit-ring/ring-handler
   (reitit-ring/router
    [
     ["/" {:get {:handler index-handler}}]
     ["/about" {:get {:handler index-handler}}]
     ["/api/scramble" {:get {:parameters {:query {:chars string?, :word string?}}
                         :responses {200 {:body {:result boolean?}}}
                         :handler scramble-handler
                         }}]
     ]

    {:data {:muuntaja m/instance
            :coercion reitit.coercion.spec/coercion
            :middleware api-middleware}}
    )
   (reitit-ring/routes
    (reitit-ring/create-resource-handler {:path "/" :root "/public"})
    (reitit-ring/create-default-handler))))
