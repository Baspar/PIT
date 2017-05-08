(ns {{name}}.components.app
  (:require [rum.core :refer [defc]]
            [cljs.core.match :refer-macros [match]]))

(defc user-button [state user-num]
      [:div {:style {:display "flex"
                     :flex-direction "row"
                     :justify-content "center"
                     :width "50px"
                     :color "white"
                     :background-color "transparent"
                     :cursor "pointer"}
             :on-click #(swap! state assoc :page [:user user-num])} (str "User " user-num)] )

(defc user-page [state user-num]
      [:div {:style {:key (str "user" user-num)
                     :height "100vh"
                     :width "100vw"
                     :color "black"
                     :background-color "lightgrey"
                     :display "flex"
                     :flex-direction "column"
                     :justify-content "center"
                     :align-items "center"
                     }}
       (str "User : " user-num)
       [:div  [:button {:on-click #(swap! state assoc :page [:home])} "GO BACK HOME"]]])

(defc home-page [state]
      [:div {:key "main"
             :style {:height "100vh" 
                     :width "100vw"
                     :background-color "#F5E6BF" 
                     :display "flex"
                     :flex-direction "column"
                     :justify-content "center"
                     :align-items "center"}}
       [:div {:key "sub-div"
              :style {:height "300px"
                      :width "300px"
                      :background-color "#7E0227"
                      :border-radius "10px"
                      :display "flex"
                      :flex-direction "column"
                      :align-items "center"
                      :justify-content "center"}} 
        (map #(user-button state %) (range 1 4))]])

(defc app [state]
      (let [page-num (@state :page [])]
      [:div  {:key "main"
              :style {:height "500px"
                      :width "500px"}}]
       (match page-num
              [:home] (home-page state)
              [:user (b :guard number?)] (user-page state b)
              :else (home-page state))))
