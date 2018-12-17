(ns scrumble.components-test-runner
  (:require  [cljs.test :refer-macros [deftest testing is run-tests use-fixtures]]
             [reagent.core :as reagent]
             [scramble.form :as sform]
             [scramble.navbar :as navbar]))

(enable-console-print!)

(deftest can-we-render-button?
  (reagent/render [sform/submit-button
                   "label"
                   #("someaction")
                   {:form {:word "asdas"}}] js/document.body)
  (is (= "label"
         js/document.body.firstChild.innerHTML)))

(defn element-exists?
  "takes an element name and returns a true if it exists, false otherise"
  [element-name]
  (not= -1
        (-> js/document.body.firstChild.innerHTML
            (.indexOf element-name))))




(deftest can-we-render-input?
  (reagent/render [sform/input
                   "someId"
                   "label"
                   #("someaction")
                   {:form {:word "asdas"}}] js/document.body)
  (is (element-exists? "label"))
  (is (element-exists? "input")))

(deftest can-we-render-input-with-error?
  (reagent/render [sform/input
                   "someId"
                   "label"
                   #("someaction")
                   {:form {:field-errors "some error"}}] js/document.body)
  (is (element-exists? "label"))
  (is (element-exists? "input"))
  (is (element-exists? "small")))

(deftest can-we-render-errors-when-present?
  (let [error_message "some error"]
    (reagent/render [sform/error-message {:error error_message}] js/document.body)
    (is (= error_message js/document.body.firstChild.innerHTML))))

(deftest render-nothing-if-no-error-present?
   (reagent/render [sform/error-message ] js/document.body)
  (is (nil? js/document.body.firstChild)))

(deftest render-success-if-true?
  (let [result true]
    (reagent/render [sform/result-message {:result result}] js/document.body)
    (is (= "Scrumble!" js/document.body.firstChild.innerHTML))))

(deftest render-non-succes-if-false?
  (let [result false]
    (reagent/render [sform/result-message {:result result}] js/document.body)
    (is (= "Sorry no scamble" js/document.body.firstChild.innerHTML))))

(deftest handle-update-should-return-a-function-which-associates-errors
  (let [mock_state (reagent/atom {})
        error-msg "someError"
        validator (fn [a] error-msg)
        resulting-func (sform/handle-update mock_state validator)
        mock-id "someId"
        ]
    (do
      (resulting-func mock-id "some value")
      (is (= {:field-errors {mock-id error-msg}} @mock_state)))))

(deftest can-we-render-nav-bar-item?
  (let [label "some-label"
        link "http://somelink.com"
        ]
    (reagent/render [navbar/nav-item label link] js/document.body)
    (is (element-exists? "li"))
    (is (element-exists? "a"))))

(deftest can-we-render-nav-bar?
  (let [navitems [[navbar/nav-item "first" "http://first.com"]
                  [navbar/nav-item "second" "http://second.com"]]])
  (reagent/render [navbar/nav-bar navitems] js/document.body)
  (is (element-exists? "nav")))

(defn start []
  (use-fixtures
    :each (fn [test-fn]
            (test-fn)
            (.log js/console js/document.body)
            (reagent/unmount-component-at-node js/document.body)))


  (run-tests 'scrumble.components-test-runner))


(start)



