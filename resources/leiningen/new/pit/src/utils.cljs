(ns {{name}}.utils
  (:require [rum.core :refer [defc]]))

(defc css-transition-group [child name enter-timeout leave-timeout]
  "Create CSSTransitionGroup component
   enter/leave-timeout shoudl negative if you want to disable the animation"
  (.createElement js/React js/React.addons.CSSTransitionGroup
                  #js {:component "div"
                       :transitionName name
                       :transitionEnter (< 0 enter-timeout)
                       :transitionEnterTimeout enter-timeout
                       :transitionLeave (< 0 leave-timeout)
                       :transitionLeaveTimeout leave-timeout})
  child)

