(ns {{name}}.utils.ui)

(defn css-transition-gorup [transition-name entering-timeout leaving-timeout children]
  {:pre [(number? entering-timeout) (number? leaving-timeout) (string? transition-name)]}
  "CSS Transition Group helper

   Parameters:
     - transition-name: string
     - entering-timeout: int (in ms)
     - leaving-timeout: int (in ms)
     - children: **React Components** (Not Hiccups)"
  (let [entering? (> entering-timeout 0)
        leaving? (> leaving-timeout 0)]
    (.createElement js/React js/React.addons.CSSTransitionGroup
                    #js {:transitionName transition-name
                         :transitionEnter entering?
                         :transitionLeave leaving?
                         :transitionEnterTimeout entering-timeout
                         :transitionLeaveTimeout leaving-timeout}
                    children)))
