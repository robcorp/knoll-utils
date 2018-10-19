(ns knoll.uitest
     (:require [etaoin.api :refer :all])
  (:require [etaoin.keys :as k]))

#_(def driver (firefox))

(def home "https://knlprdwcsmgt.knoll.com/")
#_(def home "https://www.knoll.com/")
(def shop (str home "shop"))
(def design-plan (str home "design-plan"))
(def discover-knoll (str home "discover-knoll"))
(def dp-seating (str design-plan "/products/by-category/seating"))
(def login-form {:tag :form :id "login"})

(defn test-myaccount-signin-signout []
  (with-chrome {} driver
    (doto driver
      (set-window-size 1440 900)
      (maximize)
      (go shop)
      (click [{:id "userSignInNav"} {:tag :a :id "myaccount-signin-link"}])
      (fill [login-form {:tag :input :name "username"}] "kgutta")
      #_(fill [login-form {:tag :input :name "password"}] "test1234")
      (fill [login-form {:css "input[name='password'"}] "test1234")
      (click [login-form {:tag :input :name "submit"}])
      (go design-plan)
      #_(wait 5)
      #_(screenshot-element {:css ".account_info"} "account_info.png") ; only Firefox supports this
      (screenshot "screen.png")    ; Firefox and Chrome support this
      (click {:id "myaccount-signout-link"})
      (go shop)
      #_(wait 5))))

#_(quit driver)
