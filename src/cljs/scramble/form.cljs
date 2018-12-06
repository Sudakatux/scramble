(ns scramble.form
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET]]
            [scramble.util :refer [validate-chars]]
            [clojure.string :refer [blank?]])
  )

(defn input
  "Creates an input label group"
  [id label action {:keys [form field-errors]}]
  [:div {:class "form-group row"}
   [:label {:for id :class "col-sm-2 col-form-label"}
    (str label ":")]
   [:div {:class "col-sm-10"}
    [:input {:type "text"
             :id id
             :class "form-control"
             :value (get form id)
             :on-change #(action id (-> % .-target .-value))}]
    [:small.error (get field-errors id)]]])

(defn submit-button
  "Submit button component"
  [label action {form :form}]
  [:button {:class "btn btn-primary"
            :type "submit"
            :disabled (->> (vals form) (some blank?))
            :on-click #(do (.preventDefault %)
                            (action form))}
    label])

(defn handle-update
  "Takes a state and a validator, returns a function that will change state if validator passes"
  [state validator]
  (fn [id value]
    (let [error (validator value)]
    (if error
      (swap! state assoc-in [:field-errors id] error)
      (do (swap! state dissoc :result :field-errors :error)
          (swap! state assoc-in [:form id] value))))))

(defn handle-submit
  "Takes a state and a submit-to url, returns a funcion that will update the state after hitting the endpoint"
  [state submit-to]
  (fn [form]
    (GET submit-to
         {:params form
          :handler #(swap! state merge %)
          :error-handler #(swap! state assoc :error (:status-text %))})))

(defn error-message
  "Error message component"
  [{message :error}]
  (when message
    [:h3.error message]))

(defn result-message
  "Result message component"
  [{result :result}]
  (case result
    true [:h2.success "Scrumble!"]
    false [:h2 "Sorry no scamble"]
    nil))

(defn form
  "takes a submit-to. and returns a component that will submit the values to the submit-to endpoint"
  [{:keys [submit-to]}]
  (let [state (atom {:form {:chars "" :word ""}})
        update! (handle-update state validate-chars)
        submit! (handle-submit state submit-to)
        ]
    (fn []
      [:form
       [input :chars "Charecters" update! @state]
       [input :word "Word"  update! @state]
       [submit-button "Ask" submit! @state]
       [result-message @state]
       [error-message @state]])))


;; End Form Stuff
