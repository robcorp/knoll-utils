(ns knoll.textileapprovals
  (:require [dk.ative.docjure.spreadsheet :as djs]))


(defonce approvals-file "/Users/robcorp/dev/knoll/elf/resources/knolltextiles_approvals_web.xlsx")
(def workbook (djs/load-workbook approvals-file))

(def textiles-info (->> workbook
                        (djs/select-sheet "TextilesInfo")
                        (djs/select-columns {:A "Name"
                                             :B "PartNum"
                                             :C "Grade"
                                             :D "EssntlSKUs"
                                             :E "PrimarySKU"
                                             :F "Type"
                                             :H "LeatherImage"})
                        next
                        (filter #(not= "blank" (get % "Name")))))

(def textiles-approvals (->> workbook
                             (djs/select-sheet #"^Output")
                             (djs/row-seq)
                             (remove nil?)))
