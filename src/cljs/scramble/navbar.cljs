(ns scramble.navbar)

;; NavBar Component
(defn nav-item
  "Navigation item component"
  [label link]
  [:li {:class "nav-item"}
   [:a {:class "nav-link" :href link}
    label]])

(defn nav-bar
  "Navigation bar component. Takes an array of nav items"
  [& navitems]
  [:nav {:class "navbar navbar-expand-lg navbar-light bg-light"}
   [:div {:class "collapse navbar-collapse"}
    [:ul {:class "navbar-nav"}
     navitems]]])
