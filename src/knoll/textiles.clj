(ns knoll.textiles
  (:require [cheshire.core :refer [decode encode]] ; json encoding/decoding
            [clojure.java.io :refer [resource]]
            [org.httpkit.client :as http]
            #_[clj-http.client :as http]
            [com.rpl.specter :refer [ALL select transform] :as spctr] 
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as st]
            [clojure.spec.gen.alpha :as gen]
            #_[orchestra.spec.test :as st]
            [clojure.pprint :refer [pprint print-table]]))
            

;; Textiles Specs
#_(def unique-color-names (into (sorted-set)
                              (select [ALL :FabricColors ALL :ColorName] @fabrics)))

(def unique-color-names (-> (resource "color-names.json")
                            slurp
                            (decode true)))

(s/def ::Textile (s/keys :req-un [::FabricUses ::KnollGrade ::PatternVerticalFormatted
                                  ::PatternVertical ::CleaningCode ::FabricId ::NetPrice
                                  ::CuttingDirection ::AverageBoltsPerYard ::PatternHorizontal
                                  ::WeightFormatted ::UseName ::Designer ::CanadianPrice ::Finishes
                                  ::UpholsteryType ::Backings ::FabricColors ::TestingResults
                                  ::WidthFormatted ::UpholsteryGrade ::Nafta ::Contents ::CleaningCodeName
                                  ::MinimumColor ::NetPriceFormatted ::CanadianPriceFormatted
                                  ::Width ::Country ::PanelGrade ::CopyrightYear ::UseCode ::Version
                                  ::PatternHorizontalFormatted ::Weight]))
(s/def ::FabricId int?)
(s/def ::FabricUse #{{:Category "C", :UseName "Cubicle"}
                     {:Category "W", :UseName "Panel"}
                     {:Category "K", :UseName "Upholstery"}
                     {:Category "WC", :UseName "Wallcovering"}
                     {:Category "D", :UseName "Drapery"}
                     {:Category "WP", :UseName "Wrapped Panel"}})
(s/def ::FabricUses (s/coll-of ::FabricUse :distinct true :max-count 5))
(s/def ::KnollGrade #{"10" "20" "30" "40" "45" "A" "B" "B " "C" "Custom" "D" "E" "F" "G" "H" "I" "N/A"})
(s/def ::PatternVerticalFormatted (s/nilable string?))
(s/def ::PatternVertical (s/nilable (into #{"0.3" "0.66" "1.35" "2.4" "2.88"} (map str (range 0.0 125.0 0.25)))))
(s/def ::CleaningCode (s/nilable  #{""
                                    "Clean with water or solvent based cleaning agents, or diluted household bleach."
                                    "Only mild, pure water-free dry cleaning solvents may be used for cleaning this fabric."
                                    "This fabric should be vacuumed or brushed lightly to remove soil. WARNING: Do not use water-based foam or liquid cleaning agents of any type on this fabric"
                                    "Water-based cleaning agents and foam or mild, water-free solvents may be used for cleaning this fabric."
                                    "Water-based cleaning agents or foam may be used for cleaning this fabric."
                                    "Water-based or foam cleaning agents or diluted household bleach may be used for cleaning this fabric."}))
(s/def ::NetPrice (s/double-in :min 0.0))
(s/def ::CuttingDirection  (s/nilable #{""
                                        "ND - Non-directional"
                                        "NR - Non-railroaded"
                                        "NR* - Can be railroaded"
                                        "RR - Railroaded"}))
(s/def ::AverageBoltsPerYard  #{"N/A" "15" "21" "22" "23" "25" "250" "27" "28" "30" "31" "32" "33" "35" "36" "37"
                                "38" "39" "40" "41" "43" "44" "45" "50" "52" "54" "55" "60" "62" "65" "66"
                                "70" "75" "80" "88" "110"})
(s/def ::PatternHorizontal  #{"0.0" "0.09" "0.25" "0.3" "0.45" "0.5" "0.75" "1.0" "1.25" "1.5" "1.75" "10.0"
                              "10.5" "10.75" "11.0" "11.75" "12.0" "12.5" "13.0" "13.5" "13.75" "14.0" "14.25"
                              "14.5" "14.75" "15.0" "15.25" "16.5" "17.25" "17.5" "18.0" "18.5" "19.6" "2.0"
                              "2.25" "2.3" "2.38" "2.5" "2.75" "20.5" "24.25" "25.0" "25.5" "26.5" "27.0" "27.25"
                              "27.5" "27.75" "28.0" "28.25" "29.5" "3.0" "3.25" "3.5" "3.6" "3.75" "30.25" "36.0"
                              "37.0" "4.0" "4.25" "4.5" "4.75" "48.0" "5.0" "5.25" "5.75" "50.0" "51.0" "51.25"
                              "52.0" "53.0" "54.0" "54.75" "55.0" "56.0" "58.0" "6.0" "6.25" "6.5" "6.75" "7.0"
                              "7.25" "7.75" "71.75" "72.0" "8.0" "8.25" "8.5" "9.0" "9.25" "9.5" "99.0"})
(s/def ::WeightFormatted (s/nilable (into #{"N/A" "7.1 oz." "2.6 oz." "16.8 oz."} (map #(str % " oz." ) (range 1.0 44.0 0.25)))))
(s/def ::UseName #{"" "Cubicle" "Drapery" "HC" "Panel" "Upholstery" "Wallcovering" "Luxe"})
(s/def ::UseCode #{"" "C" "D" "HC" "K" "KL" "QK" "W" "WC"})
(s/def ::Designer  #{"2 x 4" "Abbott Miller" "Alejandro Cardenas" "Anni Albers 1974" "David Adjaye"
                     "Dorothy Cosonas" "Eszter Haraszty 1953" "Irma Boom" "Jhane Barnes" "Kari Pei"
                     "KnollTextiles" "KnollTextiles 1961" "KnollTextiles 1972" "LTL" "Maria Cornejo" "N/A"
                     "Proenza Schouler" "Rodarte" "Ruth Adler  Schnee" "SUNO" "Suzanne  Huguenin   1963"
                     "Suzanne Tick" "Trove" "Wolf Bauer 1969"})
(s/def ::CanadianPrice (s/double-in :min 0.0))
(s/def ::Finish #{{:Id 1, :Type "Antimicrobial"}
                  {:Id 2, :Type "Crypton"}
                  {:Id 3, :Type "Crease, Stain, and Soil Repellent"}
                  {:Id 4, :Type "Flame Retardant"}
                  {:Id 6, :Type "Mothproof"}
                  {:Id 9, :Type "Teflon"}
                  {:Id 11, :Type "Nano-tex"}
                  {:Id 12, :Type "Polyester Binder"}
                  {:Id 13, :Type "Stain & Soil Rep."}
                  {:Id 40, :Type "Nanotex"}
                  {:Id 45, :Type "Stain Release"}
                  {:Id 46, :Type "Crypton Green"}
                  {:Id 69, :Type "Stain Repellant"}
                  {:Id 100, :Type "Greenshield"}
                  {:Id 109, :Type "Incase"}
                  {:Id 111, :Type "Microban"}
                  {:Id 112, :Type "Stain Repellant & Microban"}
                  {:Id 115, :Type "Crypton Wall"}
                  {:Id 119, :Type "Nanotex & ES4"}
                  {:Id 120, :Type "Blockaide repellent"}
                  {:Id 148, :Type "Crypton & Microban"}
                  {:Id 156, :Type "Ink & Stain Resistant"}
                  {:Id 161, :Type "Water and Stain Repellent + Defiance Antimicrobial"}
                  {:Id 162, :Type "Stain Repellent"}
                  {:Id 191, :Type "Water and Stain Repellent"}
                  {:Id 194, :Type "Thermofix"}})
(s/def ::Finishes (s/coll-of ::Finish :max-count 1 :distinct true))
(s/def ::Backing #{{:Id 1, :Type "Polyester Cellulose"}
                   {:Id 2, :Type "Osnaburg"}
                   {:Id 4, :Type "Acrylic"}
                   {:Id 7, :Type "Metallic"}
                   {:Id 9, :Type "None"}
                   {:Id 10, :Type "Paper"}
                   {:Id 12, :Type "Polyester Knit"}
                   {:Id 14, :Type "Recycled Paper"}
                   {:Id 15, :Type "Polyester Cotton"}
                   {:Id 16, :Type "Cotton"}
                   {:Id 17, :Type "Polyester"}
                   {:Id 23, :Type "Cotton Scrim"}
                   {:Id 34, :Type "Recycled Polyester"}
                   {:Id 44, :Type "Rayon - 85"}
                   {:Id 45, :Type "Polyester - 15"}
                   {:Id 46, :Type "Latex"}
                   {:Id 50, :Type "Polyester Osnaburg"}
                   {:Id 51, :Type "Crypton Wall"}
                   {:Id 52, :Type "Polyester/Cotton Woven"}
                   {:Id 53, :Type "Non-Woven"}
                   {:Id 54, :Type "Polyester and Rayon Knit"}
                   {:Id 55, :Type "Non-Woven Cellulose & Polyester"}
                   {:Id 56, :Type "Crypton"}
                   {:Id 57, :Type "Knit Back"}
                   {:Id 58, :Type "Osnaburg (Polyester/Cotton)"}
                   {:Id 59, :Type "Acrylic FR"}
                   {:Id 61, :Type "NanoGuard"}
                   {:Id 62, :Type "Rayon"}})
(s/def ::Backings (s/coll-of ::Backing :distinct true :max-count 2))
(s/def ::UpholsteryType  #{"N/A" "Heavy duty" "Light duty" "Light to Medium" "Medium duty" "Medium to Heavy" "Outdoor Heavy Duty"})
(s/def ::FabricColor (s/keys :req-un [::SkuNumber ::ColorName ::ColorCategory1 ::ColorCategory2 ::ColorCategory3]))
(s/def ::SkuNumber #{"01" "02" "03" "04" "05" "06" "07" "08" "09" "1" "10" "10A" "10B"
                     "11" "110" "114" "11A" "12" "120" "12A" "12B" "13" "130" "13A" "14"
                     "140" "14A" "15" "15 " "150" "152" "155" "15A " "16" "160 " "16A"
                     "17" "170" "172" "174" "175" "18" "180" "19" "1A" "1B" "2" "20" "21"
                     "22" "23" "2323" "2328" "2329" "2330" "24" "25" "2530" "2539" "26"
                     "2679" "2680" "27" "2754" "2755" "2756" "28" "29" "2A" "2B" "3" "3 "
                     "30" "3092" "3093" "31" "3147" "32" "3274" "3279" "3280" "3281" "3282" "33"
                     "3366" "3367" "34" "3424" "3498" "35" "3581" "3582" "3583" "3584" "3586"
                     "3588" "36" "3694" "3697" "37" "38" "3888" "3890" "39" "3912" "3913"
                     "3916" "3918" "3A" "3B" "4" "4 " "40" "41" "4147" "42" "43" "4341" "4342"
                     "4396" "4397" "44" "4483" "4484" "4488" "45" "46" "4658" "4659" "47" "48"
                     "49" "4A" "4B" "5" "50" "51" "52" "5283" "53" "54" "55" "5535" "5566"
                     "56" "57" "5788" "58" "5810" "59" "5912" "5969" "5970" "5971" "5A"
                     "5B" "6" "60" "61" "62" "6232" "63" "64" "65" "66" "6628" "6660" "67"
                     "68" "6A" "6B" "7" "70" "71" "7142" "72" "7389" "7397" "74" "75" "76"
                     "79" "7A" "7B" "8" "80" "81" "82" "8223" "83" "8316" "84" "85" "86"
                     "8623279" "8623584" "8624341" "8624659" "8628316" "87" "8A" "8B" "9"
                     "9391" "9480" "9A" "9B"})
(s/def ::ColorName (into #{} unique-color-names))
(s/def ::ColorCategory (s/nilable #{"" "Beige" "Black" "Blue" "Brown" "Cool Neutral" "Gold  " "Gray" "Green"
                                    "Neutral" "Orange" "Pink" "Purple" "Red" "Violet" "Warm Neutral" "White"
                                    "Yellow"}))
(s/def ::ColorCategory1 ::ColorCategory)
(s/def ::ColorCategory2 ::ColorCategory)
(s/def ::ColorCategory3 ::ColorCategory)
(s/def ::FabricColors (s/coll-of ::FabricColor))
(s/def ::TestingResult (s/keys :req-un [::TestName ::TestResultName ::TestResult ::DateTested ::Status]))
(s/def ::TestName #{"AATCC  TM 30 anti-microbial & mildew resistant" "AATCC TM 107 colorfastness to water" "AATCC TM 147 anti-bacterial"
                    "ASTM 751-06 Seam Strength" "ASTM C423 Acoustical " "ASTM C423 Acoustical Drapery" "ASTM D 3884 - Taber Abrasion"
                    "ASTM D1203 Method A Volatile Loss of Plasticizer" "ASTM D2097/CFFA 10 Flex Resistance" "ASTM D3389 Taber Abrasion Resistance"
                    "ASTM D751 Initial Adhesion of Coating to Substrate" "ASTM D751 Sect 45-48 Adhesion of Coating to Substrate"
                    "ASTM D751 Section 45-48 Adheasion of Coating to Substrate" "ASTM D751 Tack Tear Strength" "ASTM D751-06 Breaking Strength"
                    "ASTM D751-06 Breaking Strength Proc A" "ASTM D751-06 Initial Adhesion of Coating to Substrate" "ASTM D751-06 Puncture Resistance"
                    "ASTM D751-06 Seam Strength" "ASTM D751-06 Tongue Tear Strength" "ASTM E 84 Adhered, as stocked" "ASTM E 84 Adhered, with acrylic backing"
                    "ASTM E 84 Adhered, with paper backing" "ASTM E 84 Unadhered, as stocked" "ASTM E96 Wet Cup Method" "ASTM F793 Type II Wallcovering"
                    "ASTM G154, 1200hrs. Colorfastness to Light" "ASTM G21-Anti Fungal" "BS 5852: 1990 Crib 5 as stocked" "BS 5867: Part 2: 1980" "Brush Pill"
                    "Brush Pill (Reverse)" "Brush Pill 2" "Brush Pill 3" "CAL 117 Section E, Part I" "CAL Sect. IAQ 01350" "CAN/ULC-S102 Wallcovering"
                    "CAN/ULC-S109 Vertical Flame " "CAN/ULC-S109 Vertical Flame w/FR" "CFFA 141 method II - Stain Resistance" "CFFA 15 Stretch & Set"
                    "CFFA 16 B Tongue Tear" "CFFA 16c Trap Tear" "CFFA 4 Blocking Resistance" "CFFA 6a Cold Crack" "Cal 117 - 2013 Section I" "Cal 117 - 2013 Section I 2"
                    "Cal. Title 19 " "Colorfastness Dry" "Colorfastness Dry 2" "Colorfastness Dry 3" "Colorfastness Wet" "Colorfastness Wet 2" "Colorfastness Wet 3"
                    "Dry clean - Warp" "Dry clean - weft" "FAA 25.853B, with treatment" "FMVSS 302, as stocked" "Fabric Thickness" "G-21 Anti-Fungal Test"
                    "Greenguard Certification" "IMO 2010 FTP Code, Part 8 for Upholstery" "IMO A652 (16) 8.2, as stocked" "IMO A652 (16) 8.2, with FR treatment"
                    "IMO A652 (16) 8.3, as stocked" "IMO A652 (16) 8.3, with FR treatment" "IMO for Drapery, as stocked" "IMO 2010 FTP Part 8 for Upholstery with Finish"
                    "Jungle Test - ISO1419 Method C " "Launderability Warp" "Launderability Warp 5" "Launderability Weft" "Launderability Weft 5" "Lightfastness 1000 hrs"
                    "Lightfastness 40 Hours 3" "Lightfastness 40 Hrs. 4" "Lightfastness 40 hour 2" "Lightfastness 40 hrs" "Lightfastness 60 hrs" "Lightfastness, 200 hours"
                    "Mace Snag" "Martindale Abrasion" "Martindale Abrasion 2" "Martindale Abrasion Published" "Martindale Brush Pill" "Martindale Pill" "Martindale Pill 2"
                    "NFPA 260 (UFAC)" "NFPA 260 (UFAC) 2" "NFPA 260 (UFAC) 3" "NFPA 701 '96 Test 1, as stocked" "NFPA 701 '96 Test 1, with topical treatment"
                    "NFPA 701 - 2015 TM#1, as stocked" "NFPA 701 2010 TM#1 with FR treatment" "NFPA 701 2010 TM1, as stocked" "NFPA 701 2015"
                    "NFPA 701 Small Scale '89, as stocked" "NFPA 701 Small Scale '89, with topical treatment" "NFPA 701 Test 1 - 1999- Fabric unbacked"
                    "NFPA 701-2004 TM#1 with Topical Treatment" "NFPA 701-2004 TM#1, as stocked" "NFPA 701-2004 TM#1 with FR Treatment" "REACH" "Seam Slippage Warp"
                    "Seam Slippage Warp 2" "Seam Slippage Warp 4" "Seam Slippage Weft" "Seam Slippage Weft 2" "Seam Slippage Weft 4" "Tensile Strength Warp"
                    "Tensile Strength Warp 2" "Tensile Strength Warp 3" "Tensile Strength Weft" "Tensile Strength Weft 2" "Tensile Strength Weft 3" "Tongue Tear ASTM D2261"
                    "Tongue Tear D2261" "Wyzenbeek Published" "Wyzenbeek Warp"})
(s/def ::TestResultName (s/nilable #{"100,000" "4.5" "5" "Class 1" "Class 5" "Class A" "Class I" "Class II" "Compliant" "Fail" "Pass" "Pass with FR treatment"}))
(s/def ::TestResult (s/nilable (s/or :testresult #{"\tCLASS A, FS:5, SD:50" " 4 " " 4.5" " 5" "-.05 NRC; SAA .98 " "-.1 NRC;SAA1.03" "-0.3" "-0.5" "-0.7%" "-1.0" "-1.0%" "-1.5" "-1.5%" "-1.7" ".15 NRC ;SAA .81" "0 Fungal Activity" "0-Activity" "0.0" "0.5% after 5 cycles" "0.72mm" "1.5% after 5 cycles" "100" "100 SB" "100 lbf" "100,000" "100,000+" "100.4 SB" "100000" "101" "101 SB" "101.4" "101.5" "101.5 SB" "101.5 SS" "101.6" "102" "102 lbf" "102.2 SB" "102.5" "102.5 SB" "102.5 lbf." "102.9" "103" "103 FBS" "103 SB" "103.4" "103.6" "103.8 FB" "104.2 SB" "104.5 SS" "104.8 SB" "105" "105 SB" "105.2" "105.5" "105.5 FB" "105.5 SB" "105.6" "106" "106 lbf" "106.0" "106.5 SB" "107" "107 SB" "107 lbs" "107.2" "107.5" "107.5 SB" "108" "108 SB" "108.2" "108.5 " "108.5 FB" "109" "109 lbs." "109.0 lbf" "109.5" "109.5 lbf" "110" "110 SB" "110.2 SB" "110.9" "111" "111 SB" "111.5 SB" "111.5 lbf" "112" "112 SB" "112 lbf" "112 lbs." "112.4 sb" "112.5 SB" "113" "113 SB" "113 lbs." "113.3" "113.5 SB" "114 lbf" "114.5 SB" "115" "115.4 lbs" "115.5" "115.5 SB" "116" "116.5" "116.5 lbf" "117" "117 SB" "117 lbf" "117 lbf." "117.5 SB" "118" "118 lbs" "118.5 SB" "118.6" "119" "119 SB" "119 lbs" "119.0 lbf" "119.5 lbf" "120" "120 lbs" "120.5 FBS" "120.5 SB" "121" "121 lbf. warp; 125 lbf. fill" "122" "122 lbs" "123" "123 SB" "123 lbf" "123.5" "124" "124 lbf" "124.5 SB" "124.5 lbf" "125" "126" "126.5" "127" "127.5 SB" "127.71 lbs." "128 SB" "128 lbs." "128.8" "129" "129 lbf" "129 lbs." "129.5 SB" "130 SB" "130.5 lbf" "131" "131 SB" "131 lbs" "132" "133" "133 SB" "134 SB" "134 lbs" "135" "135.7" "136" "136 lbf" "137" "137.5 lbf" "138" "138.5 SB" "139" "139 lbs." "140" "140.5 SB" "141" "141.3 lbs" "141.5 lbf" "142" "143" "144" "144 SB" "145" "145.5" "145.5 SB" "146" "146 lbf." "146.0 lbf" "147" "148" "148 SB" "149" "15,000" "15,000 Cotton" "150" "150 lbs." "151" "152" "152 lbs." "153" "154" "155" "156" "156 lbs" "157 lbs" "158" "158 lbs" "159 lbs" "16 SB" "16 SS" "160" "162" "163" "164" "164 lbs" "165" "166" "166.2" "167" "168" "169" "169 lbs" "170" "170 lbs" "171" "172" "172 lbs" "173" "174" "175" "175 lbs" "176" "177" "178" "179" "179 lbs" "18.5" "181" "181 lbs" "182" "183" "184" "185" "186" "187" "188" "189" "189 lbs" "190" "190.5" "192" "193" "194" "196" "197" "197 lbs." "198" "198 lbs" "199" "2" "2 " "2 - No blocking: Slight adhesion" "20,000" "20,000+" "200" "200 lbs" "200,000+" "201" "202" "203" "204" "205" "205 lbs" "205 lbs." "206" "207" "208" "209" "210" "211" "212" "213" "213 lbs" "214" "215" "215.29" "216" "217" "218 lbs" "219" "219 lbs" "22" "22,000" "221" "222" "223 lbs." "224" "225" "226" "226.83" "227" "228" "229"}
                                     :other string?)))
(def datetested-gen (gen/large-integer* {:min 345186000000 :max 1535515200000}))
(s/def ::DateTested (s/with-gen (s/nilable (s/and int? #(<= 345186000000 % 1535515200000))) (fn [] datetested-gen)))
(s/def ::Status #{"" "Tested"})
(s/def ::TestingResults (s/coll-of ::TestingResult))
(s/def ::WidthFormatted (s/nilable #{"102 in." "110 in." "115.7 in." "118 in." "36 in." "48 in." "50 (approx.) in." "50 in." "51 in." "52 in." "53 in." "54 in." "54* in." "55 in." "56 in." "57 in." "58 in." "59 in." "60 in." "61 in." "62 in." "65 in." "66 in." "67 in." "68 in." "71 in." "72 in." "74 in." "78 in."}))
(s/def ::UpholsteryGrade (s/nilable #{"" "A" "B" "B " "C" "Custom" "D" "E" "F" "G" "H" "I"}))
(s/def ::Nafta (s/or :true true? :false false?))
(s/def ::ContentItem (s/keys :req-un [::Content ::Percentage]))
(def content-set #{"Acrylic" "Acrylic (Pile)" "Alpaca" "Bella Dura solution dyed olefin"
                   "Bleach Cleanable Polyester" "Bleach Cleanable Post Consumer Recycled Polyester"
                   "Cellulose & Polyester" "Ceramic (face)" "Cotton" "Cotton (Backing)" "Cotton (Ground)"
                   "Cotton Chenille" "Cotton Ground" "Eco-Intelligent Polyester" "FR  Polyester"
                   "Flame Resistant Polyester" "Lambswool" "Linen" "Merino Wool" "Metallic Fiber" "Mohair"
                   "Mohair (Pile)" "Non-Phthalate Vinyl" "Non-Phthalate Vinyl (Face)"
                   "Non-Phthalate Vinyl Coated Polyester" "Nylon" "Olefin" "PCR/FR Polyester"
                   "PES Trevira CS Polyester" "PU (Elasthan)" "Plant Based Polyester" "Polyacrylic" "Polyamid"
                   "Polyester" "Polyester (Backing)" "Polyester (Ground)" "Polyethylene" "Polyurethane"
                   "Polyurethane (Face)" "Post Consumer Recycled Acrylic" "Post Consumer Recycled Cotton"
                   "Post Consumer Recycled Polyester" "Post Consumer Recycled Polyester with Agion®"
                   "Post Industrial Recycled Polyester" "Pure New Wool" "Ramie" "Rayon" "Rayon (Face)"
                   "Recycled  Polyester" "Recycled Cotton" "Recycled Glass" "Recycled Solution Dyed Nylon"
                   "Silk" "Solution Dyed Nylon" "Solution Dyed Polyester" "Sunbrella® Acrylic"
                   "Sunbrella® Polyester" "Sunbrella® Post-Industrial Recycled Acrylic"
                   "Terratex post-consumer recycled polyester" "Terratex post-industrial recycled polyester"
                   "Thermoplastic Olefin" "Trevira CS  Polyester" "Trevira FR Polyester" "Vicose" "Vinyl"
                   "Vinyl (Face)" "Vinyl Coated Polyester" "Virgin Polyester" "Virgin Solution Dyed Nylon"
                   "Viscose" "Wool" "Wool pile" "Zeftron Nylon" "polyester" "Polyester Microfiber"})
(s/def ::Content content-set)
(s/def ::Percentage (s/double-in :min 0.0 :max 100.0))

(defn- list-of-n-totalling-t [n t]
  (let [cnt (inc (rand-int n))]
    (if (= cnt 1)
      (list t)
      (loop [i cnt
             num (- t n)
             res '()]
        (if (= i 1)
          (conj res (+ num n))
          (let [r (inc (rand-int (dec num)))]
            (recur (dec i)
                   (- num r)
                   (conj res (double r)))))))))

(defn- gen-n-contents-totalling-t [n t]
  (let [percents (list-of-n-totalling-t 5 100)]
    (mapv (fn [c p] {:Content c :Percentage (* 1.0 p)})
          (take (count percents) (shuffle content-set))
          percents)))

(s/def ::Contents (s/and
                   (s/coll-of ::ContentItem :max-count 5)
                   (fn [c] (let [sum (reduce + (map :Percentage c))] (or (= sum 100.0)
                                                                         (= sum 0))))))
(s/def ::CleaningCodeName (s/nilable #{"" "S" "W" "W Bleach" "W-S" "W-S Bleach" "X"}))
(s/def ::MinimumColor #{"" "*198 " "100" "100 yds" "1000" "1000 yards" "105 yards" "110" "115" "115 yards"
                        "118 yards/color" "120" "120 YDS" "120 yards" "120 yds" "1200" "128" "130" "130 yards"
                        "130 yds - 2 pieces" "130 yds, upcharge" "132" "135 yards" "1400" "150" "1500"
                        "156 yards" "160" "165" "165 yards" "165 yds" "175" "175 yards" "175 yds" "176"
                        "176 solids" "180" "185" "195" "198" "200" "210" "215" "218" "220" "220 yds"
                        "220-240" "240" "240-260" "240-260 (4 pcs)" "240-480" "250" "260" "260 yards approx."
                        "260 yds" "262" "265" "275" "280" "290" "30" "300" "300 yds " "310" "315" "320" "325"
                        "328" "33" "330" "330 yards" "340/680" "350" "350 yards" "360" "390" "40" "40 yd" "400"
                        "400/800" "410" "420" "425/850" "430" "44" "440" "440/880" "45" "450" "460" "480" "50"
                        "50 yds" "500" "500 lbs" "500 w/ upcharge" "500/1000" "54" "55" "550" "60" "600"
                        "600 yards" "625" "65" "66" "66 yards" "700" "71" "750" "80" "800" "88" "90" "Inquire"
                        "N/A" "See Notes Tab for Dye set/ordering increments" "approx 130 yds" "contact Customs"
                        "inquire" "not available"})
;; specs for staging textiles service
(def US-locale (java.util.Locale. "en" "US"))
(def CA-locale (java.util.Locale. "en" "CA"))
(s/def ::NetPriceFormatted (into #{} (map #(str (.format (java.text.NumberFormat/getCurrencyInstance US-locale) %) " USD") (range 0 370 0.5))))
(s/def ::CanadianPriceFormatted (into #{} (map #(str (.format (java.text.NumberFormat/getCurrencyInstance CA-locale) %) " CAD") (range 0 480 0.5))))

(s/def ::Width (s/nilable #{"102" "110" "115.7" "118" "36" "48" "50" "50 (approx.)" "51" "52" "53" "54" "54*"
                            "55" "56" "57" "58" "59" "60" "61" "62" "65" "66" "67" "68" "71" "72" "74" "78"}))
(s/def ::Country (s/nilable #{"Austria" "Belgium" "Brazil" "Canada" "China" "France" "Germany" "Holland" "India"
                              "Israel" "Italy" "Japan" "Malaysia" "Mexico" "Netherlands" "New Zealand" "Scotland"
                              "South Korea" "Switzerland" "Taiwan" "Turkey" "United Kingdom" "United States"}))
(s/def ::PanelGrade (s/nilable #{"" "10" "20" "30" "40" "45" "50" "55"}))
(s/def ::CopyrightYear (into #{"N/A"} (map str (range 1960 2021))))
(s/def ::Version #{"" "C" "D" "H" "HC" "K" "QK" "W" "WC"})
(s/def ::PatternHorizontalFormatted (s/nilable (into #{"0.09" "0.3" "0.45" "2.3" "2.38" "3.6" "19.6"} (map str (range 0.0 100.0 0.25)))))
(s/def ::Weight (into #{"N/A" "7.1" "2.6" "16.8"} (map str (range 0.0 44.0 0.25))))
(s/def ::Textiles (s/coll-of ::Textile))


;;;; Textiles defs and miscellaneous utils

#_(def textile-service-url "http://localhost:8080/textiles/")
#_(def textile-service-url "http://knlecomdev1.knoll.com:9082/textiles/")
#_(def textile-service-url "http://knldev2wcsapp1a.knoll.com/textiles/")
#_(def textile-service-url "http://knlecomprd2.knoll.com:9082/textiles/") ; def'd once because the staging service is SLOW!
(def textile-service-url "http://knlecomprd1.knoll.com:9082/textiles/")
#_(def textile-service-url "https://www.knoll.com/textiles/")

(def http-options {:timeout 36000000})

(def fabrics (future (decode
                      (:body @(http/get (str textile-service-url "fabrics") http-options)) true)))

(def fabrics' (-> (resource "fabrics.json")
                  slurp
                  (decode true)))

(defn ^:private -get-fabric [fabid]
  (let [resp @(http/get (str textile-service-url fabid))]
    (if (= 200 (:status resp))
      (decode (:body resp) true)
      {})))

(def get-fabric (memoize -get-fabric))

(defn color-names [fab]
  (map :ColorName (:FabricColors fab)))

(defn uses
  "Returns list of uses for fabric fab where each use is a map consisting of :UseCode and :UseName"
  [fab]
  (:FabricUses fab))

(defn sort-contents-by-percentage [fabrics]
  (transform [ALL :Contents] #(sort-by :Percentage %) fabrics))

(defn get-textile-attribute-sorted-set [path]
  (into (sorted-set) (select path @fabrics)))

(def crossroad (get-fabric 2085)) ; good for testing Fabric Uses, Multiple Content % and Minimum Custom Color yardage
(def bollywood (get-fabric 1015))
(def beacon (get-fabric 1597))
(def shima (get-fabric 1468))
(def abacus (get-fabric 715))
(def stripemania (get-fabric 2225))
(def brigadoon (get-fabric 2167))
(def sideline (get-fabric 2210))
(def swoosh (get-fabric 1583))
(def modern-tweed (get-fabric 2155))
(def acme-backed (get-fabric 992189))
(def arabella (get-fabric 1475))
(def kamani (get-fabric 1580))


#_(s/valid? ::Textile (first (gen/sample (s/gen ::Textile) 1)))

;; validate all Textiles returned from the webservice
(defn validate-all-textiles []
  (if-not (s/valid? ::Textiles @fabrics)
    (do
      (println "Textiles validation failed due to the following:")
      (s/explain ::Textiles @fabrics))
    (println "All Textiles are valid.")))

(validate-all-textiles)

