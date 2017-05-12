(ns pit-plugin.core
  (:require [doo.runner :refer-macros [doo-tests]]
            [pit-plugin-test]))
(doo-tests 'pit-plugin-test)
