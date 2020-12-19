(ns knoll.uitest
     (:require [etaoin.api :refer :all])
  (:require [etaoin.keys :as k]))

#_(def driver (firefox))

(def home "https://www.knoll.com/")
#_(def home "https://www.knoll.com/")
(def shop (str home "shop"))
(def design-plan (str home "design-plan"))
(def discover-knoll (str home "discover-knoll"))
(def dp-seating (str design-plan "/products/by-category/seating"))
(def login-form {:tag :form :id "login_dealer"})

(defn test-myaccount-signin-signout []
  (with-chrome {} driver
    (doto driver
      (set-window-size 1440 900)
      #_(maximize)
      (go shop)
      (screenshot "screen1.png")
      (click [{:id "userSignInNav"} {:tag :a :id "myaccount-signin-link"}])
      (fill [login-form {:tag :input :name "username"}] "rhouser")
      #_(fill [login-form {:tag :input :name "password"}] "test1234")
      (fill [login-form {:css "input[name='password'"}] "byte529[clienT")
      (wait 3)
      (click [login-form {:tag :input :name "submit"}])
      (go design-plan)
      #_(wait 5)
      #_(screenshot-element {:css ".account_info"} "account_info.png") ; only Firefox supports this
      (screenshot "screen2.png")    ; Firefox and Chrome support this
      #_(click {:id "myaccount-signout-link"})
      (click "//*[@id='userSignInNav']/a")
      (go shop)
      (wait 5))))

#_(quit driver)

(defn test-featuredsubcat-links []
  (with-chrome {} driver
    (doto driver
      #_(set-window-size 1440 900)
      (go shop)
      (click "//*[@id='1347382602231']/section/ul/li[1]/a")
      (wait 20)
      #_(click "//*[@id='page']/div/section[2]/div/div[1]/a"))))
