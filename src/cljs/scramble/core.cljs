(ns scramble.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [reitit.frontend :as reitit]
              [accountant.core :as accountant]
              [scramble.form :refer [form]]
              [scramble.navbar :refer [nav-item nav-bar]]))

;; -------------------------
;; Routes

(def router
  (reitit/router
   [["/" :index]
    ["/about" :about]]))

(defn path-for [route & [params]]
  (if params
    (:path (reitit/match-by-name router route params))
    (:path (reitit/match-by-name router route))))


;; -------------------------
;; Page components

(defn home-page []
  (fn []
    [:div
    [:span.main
     [:h1 "Welcome to scramble"]
    ]
     [:div {:class "alert alert-primary"}
      "Hi welcome select chars and words to test. I can tell if they scramble or not"]
    [:div.container
     [form {:submit-to "/api/scramble"}]]]))

(defn about-page []
  (fn [] [:span.main
          [:h1 "About scramble"]
           "Simple scramble created by James Stuart Milne"]))

;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
  (case route
    :index #'home-page
    :about #'about-page))


;; -------------------------
;; Page mounting component

(defn current-page []
  (fn []
    (let [page (:current-page (session/get :route))]
      [:div
       [:header
        [nav-bar
         [nav-item "Home" (path-for :index)]
         [nav-item "About" (path-for :about)]]
       ]
       [page]])))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (let [match (reitit/match-by-path router path)
            current-page (:name (:data  match))
            route-params (:path-params match)]
        (session/put! :route {:current-page (page-for current-page)
                              :route-params route-params})))
    :path-exists?
    (fn [path]
      (boolean (reitit/match-by-path router path)))})
  (accountant/dispatch-current!)
  (mount-root))
