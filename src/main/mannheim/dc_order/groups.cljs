(ns mannheim.dc-order.groups)

(def groups-ids
  [{:dc.warehouse.groups/group-id   "01"
    :dc.warehouse.groups/group-name "alkohole"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0101"
    :dc.warehouse.groups/group-name "wino"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010101"
    :dc.warehouse.groups/group-name "cydry"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010103"
    :dc.warehouse.groups/group-name "nalewki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010104"
    :dc.warehouse.groups/group-name "vermouthy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010105"
    :dc.warehouse.groups/group-name "wina musujące"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010106"
    :dc.warehouse.groups/group-name "wina owocowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010107"
    :dc.warehouse.groups/group-name "wina spokojne"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010108"
    :dc.warehouse.groups/group-name "wino migracja"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0103"
    :dc.warehouse.groups/group-name "piwo"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010301"
    :dc.warehouse.groups/group-name "piwo w butelce"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010302"
    :dc.warehouse.groups/group-name "piwo w puszce"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0104"
    :dc.warehouse.groups/group-name "alkohole mocne"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010401"
    :dc.warehouse.groups/group-name "brandy, gin, rum"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010402"
    :dc.warehouse.groups/group-name "drinki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010403"
    :dc.warehouse.groups/group-name "likiery"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010404"
    :dc.warehouse.groups/group-name "whisky"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010405"
    :dc.warehouse.groups/group-name "wódka"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "010406"
    :dc.warehouse.groups/group-name "alkohole migracja"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "02"
    :dc.warehouse.groups/group-name "chemia"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0202"
    :dc.warehouse.groups/group-name "kosmetyki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020201"
    :dc.warehouse.groups/group-name "artykuły sezonowe, kosmetyki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020202"
    :dc.warehouse.groups/group-name "dezodoranty i perfumy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020203"
    :dc.warehouse.groups/group-name "golenie"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020204"
    :dc.warehouse.groups/group-name "makijaż"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020205"
    :dc.warehouse.groups/group-name "mydła"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020206"
    :dc.warehouse.groups/group-name "pasty i higiena jamy ustnej"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020207"
    :dc.warehouse.groups/group-name "pielęgnacja ciała i twarzy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020208"
    :dc.warehouse.groups/group-name "płyny/żele do kąpieli"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020209"
    :dc.warehouse.groups/group-name "preparaty do farbowania włosów"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020210"
    :dc.warehouse.groups/group-name "preparaty do higieny intymnej"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020211"
    :dc.warehouse.groups/group-name "preparaty do układania włosów"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020212"
    :dc.warehouse.groups/group-name "szampony i odżywki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020213"
    :dc.warehouse.groups/group-name "kosmetyki migracja"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0203"
    :dc.warehouse.groups/group-name "odświeżacze"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020301"
    :dc.warehouse.groups/group-name "odświeżacze"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0204"
    :dc.warehouse.groups/group-name "sezonowe owady"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020401"
    :dc.warehouse.groups/group-name "sezonowe owady"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0205"
    :dc.warehouse.groups/group-name "środki do czyszczenia"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020501"
    :dc.warehouse.groups/group-name "środki do czyszczenia"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0206"
    :dc.warehouse.groups/group-name "detergenty do prania"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020601"
    :dc.warehouse.groups/group-name "detergenty do prania"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0207"
    :dc.warehouse.groups/group-name "detergenty do zmywania"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020701"
    :dc.warehouse.groups/group-name "detergenty do zmywania"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0208"
    :dc.warehouse.groups/group-name "higiena osobista"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020801"
    :dc.warehouse.groups/group-name "chusteczki i ręczniki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020802"
    :dc.warehouse.groups/group-name "higiena niemowląt"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020803"
    :dc.warehouse.groups/group-name "papier toaletowy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020804"
    :dc.warehouse.groups/group-name "pieluchy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020805"
    :dc.warehouse.groups/group-name "podpaski"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "020806"
    :dc.warehouse.groups/group-name "tampony"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "03"
    :dc.warehouse.groups/group-name "strefa przykasowa"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0301"
    :dc.warehouse.groups/group-name "papierosy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "030101"
    :dc.warehouse.groups/group-name "akcesoria do papierosów"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "030103"
    :dc.warehouse.groups/group-name "papierosy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "030104"
    :dc.warehouse.groups/group-name "tytoń"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0302"
    :dc.warehouse.groups/group-name "prasa"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "030201"
    :dc.warehouse.groups/group-name "prasa"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0304"
    :dc.warehouse.groups/group-name "farmacja"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "030401"
    :dc.warehouse.groups/group-name "farmacja"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0305"
    :dc.warehouse.groups/group-name "telefonia i usługi"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "030501"
    :dc.warehouse.groups/group-name "telefonia"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "04"
    :dc.warehouse.groups/group-name "mięso, wędliny i inne lada"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0401"
    :dc.warehouse.groups/group-name "mięso białe waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040101"
    :dc.warehouse.groups/group-name "gęś waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040102"
    :dc.warehouse.groups/group-name "indyk waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040103"
    :dc.warehouse.groups/group-name "kaczka waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040105"
    :dc.warehouse.groups/group-name "kurczak waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0402"
    :dc.warehouse.groups/group-name "ryby waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040201"
    :dc.warehouse.groups/group-name "marynaty rybne waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040202"
    :dc.warehouse.groups/group-name "owoce morza waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040203"
    :dc.warehouse.groups/group-name "ryby świeże waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040204"
    :dc.warehouse.groups/group-name "ryby wędzone waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0403"
    :dc.warehouse.groups/group-name "sery waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040301"
    :dc.warehouse.groups/group-name "sery pleśniowe waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040302"
    :dc.warehouse.groups/group-name "sery premium waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040303"
    :dc.warehouse.groups/group-name "sery żółte waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040304"
    :dc.warehouse.groups/group-name "twarogi waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040305"
    :dc.warehouse.groups/group-name "sery waga przecena"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0404"
    :dc.warehouse.groups/group-name "wędliny waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040401"
    :dc.warehouse.groups/group-name "kabanosy waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040403"
    :dc.warehouse.groups/group-name "kiełbasy cienkie waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040404"
    :dc.warehouse.groups/group-name "kiełbasy grube waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040405"
    :dc.warehouse.groups/group-name "mielonki, bloki waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040406"
    :dc.warehouse.groups/group-name "parówki, parówkowe i serdelki waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040407"
    :dc.warehouse.groups/group-name "salami waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040408"
    :dc.warehouse.groups/group-name "wędzonki waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040409"
    :dc.warehouse.groups/group-name "wyroby podrobowe i garmażeryjne waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040410"
    :dc.warehouse.groups/group-name "wędliny waga przecena"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0406"
    :dc.warehouse.groups/group-name "garmażerka waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040602"
    :dc.warehouse.groups/group-name "dania mięsne waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040603"
    :dc.warehouse.groups/group-name "dania rybne waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040604"
    :dc.warehouse.groups/group-name "dania warzywne waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0407"
    :dc.warehouse.groups/group-name "mięso czerwone waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040701"
    :dc.warehouse.groups/group-name "cielęcina waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040702"
    :dc.warehouse.groups/group-name "inne mięso czerwone waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040703"
    :dc.warehouse.groups/group-name "wieprzowina waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "040704"
    :dc.warehouse.groups/group-name "wołowina waga"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "05"
    :dc.warehouse.groups/group-name "mrożonki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0502"
    :dc.warehouse.groups/group-name "mrożonki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "050201"
    :dc.warehouse.groups/group-name "dania mrożone"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "050203"
    :dc.warehouse.groups/group-name "lody"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "050204"
    :dc.warehouse.groups/group-name "mięso mrożone"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "050205"
    :dc.warehouse.groups/group-name "owoce i warzywa mrożone"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "050206"
    :dc.warehouse.groups/group-name "ryby i owoce morza mrożone"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "050207"
    :dc.warehouse.groups/group-name "ryby i owoce morza mrożone - przecena"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "050208"
    :dc.warehouse.groups/group-name "mrożonki migracja"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "06"
    :dc.warehouse.groups/group-name "nabiał i chłodzone samoobsługa"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0601"
    :dc.warehouse.groups/group-name "jaja"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060101"
    :dc.warehouse.groups/group-name "jaja"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0602"
    :dc.warehouse.groups/group-name "sery samoobsługa"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060201"
    :dc.warehouse.groups/group-name "sery pleśniowe pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060202"
    :dc.warehouse.groups/group-name "sery topione pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060203"
    :dc.warehouse.groups/group-name "sery żółte pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060204"
    :dc.warehouse.groups/group-name "twarogi pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0603"
    :dc.warehouse.groups/group-name "tłuszcze"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060301"
    :dc.warehouse.groups/group-name "margaryna kulinarna"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060302"
    :dc.warehouse.groups/group-name "margaryna stołowa"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060303"
    :dc.warehouse.groups/group-name "masło"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060304"
    :dc.warehouse.groups/group-name "mixy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060305"
    :dc.warehouse.groups/group-name "olej"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060306"
    :dc.warehouse.groups/group-name "oliwa z oliwek"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060307"
    :dc.warehouse.groups/group-name "smalec"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0606"
    :dc.warehouse.groups/group-name "garmażerka samoobsługa"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060601"
    :dc.warehouse.groups/group-name "dania mączne pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060602"
    :dc.warehouse.groups/group-name "dania mięsne pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060603"
    :dc.warehouse.groups/group-name "dania rybne pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060604"
    :dc.warehouse.groups/group-name "marynaty i sałatki rybne pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060605"
    :dc.warehouse.groups/group-name "wędliny pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0608"
    :dc.warehouse.groups/group-name "produkty mleczne"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060801"
    :dc.warehouse.groups/group-name "desery, serki, kefiry"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060802"
    :dc.warehouse.groups/group-name "jogurty "
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060803"
    :dc.warehouse.groups/group-name "mleko świeże"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060804"
    :dc.warehouse.groups/group-name "mleko uht"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "060805"
    :dc.warehouse.groups/group-name "śmietany"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "07"
    :dc.warehouse.groups/group-name "napoje, soki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0701"
    :dc.warehouse.groups/group-name "woda"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "070101"
    :dc.warehouse.groups/group-name "woda"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0703"
    :dc.warehouse.groups/group-name "napoje, soki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "070301"
    :dc.warehouse.groups/group-name "napoje gazowane inne"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "070302"
    :dc.warehouse.groups/group-name "napoje typu cola"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "070303"
    :dc.warehouse.groups/group-name "napoje typu ice tea"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "070304"
    :dc.warehouse.groups/group-name "napoje typu izotoniki, energetyki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "070305"
    :dc.warehouse.groups/group-name "soki, nektary, inne napoje niegazowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "070306"
    :dc.warehouse.groups/group-name "syropy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "08"
    :dc.warehouse.groups/group-name "kosztowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0802"
    :dc.warehouse.groups/group-name "artykuły kosztowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "080201"
    :dc.warehouse.groups/group-name "artykuły kosztowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0803"
    :dc.warehouse.groups/group-name "opakowania"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "080301"
    :dc.warehouse.groups/group-name "opakowania"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "09"
    :dc.warehouse.groups/group-name "owoce i warzywa"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0902"
    :dc.warehouse.groups/group-name "kiszonki i koncentraty"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0903"
    :dc.warehouse.groups/group-name "kwiaty i rośliny"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090201"
    :dc.warehouse.groups/group-name "kiszonki i koncentraty"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090301"
    :dc.warehouse.groups/group-name "kwiaty i rośliny"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0904"
    :dc.warehouse.groups/group-name "owoce i warzywa pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090401"
    :dc.warehouse.groups/group-name "owoce pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090402"
    :dc.warehouse.groups/group-name "warzywa pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090403"
    :dc.warehouse.groups/group-name "grzyby suszone pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0905"
    :dc.warehouse.groups/group-name "owoce luz"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090501"
    :dc.warehouse.groups/group-name "bakalie"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090502"
    :dc.warehouse.groups/group-name "cytrusy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090503"
    :dc.warehouse.groups/group-name "jabłka i gruszki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090504"
    :dc.warehouse.groups/group-name "melony"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090505"
    :dc.warehouse.groups/group-name "owoce egzotyczne"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090506"
    :dc.warehouse.groups/group-name "owoce miękkie"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090507"
    :dc.warehouse.groups/group-name "owoce pestkowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090508"
    :dc.warehouse.groups/group-name "winogrona"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0906"
    :dc.warehouse.groups/group-name "sałaty i zioła"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090601"
    :dc.warehouse.groups/group-name "sałaty pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090602"
    :dc.warehouse.groups/group-name "zioła doniczka"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "0907"
    :dc.warehouse.groups/group-name "warzywa luz"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090701"
    :dc.warehouse.groups/group-name "grzyby"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090702"
    :dc.warehouse.groups/group-name "warzywa kapustne"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090703"
    :dc.warehouse.groups/group-name "warzywa okopowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090704"
    :dc.warehouse.groups/group-name "warzywa sałatkowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090705"
    :dc.warehouse.groups/group-name "warzywa sezonowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "090706"
    :dc.warehouse.groups/group-name "zieleniny"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "10"
    :dc.warehouse.groups/group-name "pieczywo"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "1002"
    :dc.warehouse.groups/group-name "pieczywo"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "100201"
    :dc.warehouse.groups/group-name "pieczywo  - pieczywo tostowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "100202"
    :dc.warehouse.groups/group-name "pieczywo - bułka tarta"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "100203"
    :dc.warehouse.groups/group-name "pieczywo - torilla"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "100204"
    :dc.warehouse.groups/group-name "pieczywo mrożone - bułki, bagietki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "100205"
    :dc.warehouse.groups/group-name "pieczywo mrożone - ciasta"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "100206"
    :dc.warehouse.groups/group-name "pieczywo mrożone - ciasta"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "100207"
    :dc.warehouse.groups/group-name "pieczywo mrożone - przekąski"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "100208"
    :dc.warehouse.groups/group-name "pieczywo regionalne - bułki i bagietki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "100209"
    :dc.warehouse.groups/group-name "pieczywo regionalne - chleb biały"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "100210"
    :dc.warehouse.groups/group-name "pieczywo regionalne - chleb ciemny"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "100211"
    :dc.warehouse.groups/group-name "pieczywo regionalne - ciasta luz"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "100212"
    :dc.warehouse.groups/group-name "pieczywo regionalne - przekąski słodkie i słone"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "11"
    :dc.warehouse.groups/group-name "przemysłowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "1102"
    :dc.warehouse.groups/group-name "artykuły przemysłowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "110201"
    :dc.warehouse.groups/group-name "akcesoria gospodarstwa domowego"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "110202"
    :dc.warehouse.groups/group-name "artykuły przemysłowe inne"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "110203"
    :dc.warehouse.groups/group-name "sezonowe grillowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "110204"
    :dc.warehouse.groups/group-name "sezonowe świąteczne"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "110205"
    :dc.warehouse.groups/group-name "szkoła"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "110206"
    :dc.warehouse.groups/group-name "znicze"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "12"
    :dc.warehouse.groups/group-name "spożywka pakowana"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "1201"
    :dc.warehouse.groups/group-name "przekąski"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120101"
    :dc.warehouse.groups/group-name "przekąski"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "1203"
    :dc.warehouse.groups/group-name "śniadania, zdrowa żywność"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120301"
    :dc.warehouse.groups/group-name "dżemy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120302"
    :dc.warehouse.groups/group-name "kakao i napoje czekoladowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120303"
    :dc.warehouse.groups/group-name "płatki śniadaniowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120304"
    :dc.warehouse.groups/group-name "zdrowa żywność"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "1206"
    :dc.warehouse.groups/group-name "herbata"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120601"
    :dc.warehouse.groups/group-name "herbaty czarne"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120602"
    :dc.warehouse.groups/group-name "herbaty pozostałe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "1207"
    :dc.warehouse.groups/group-name "kulinaria słona"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120701"
    :dc.warehouse.groups/group-name "buliony"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120702"
    :dc.warehouse.groups/group-name "dania gotowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120703"
    :dc.warehouse.groups/group-name "konserwy mięsne"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120704"
    :dc.warehouse.groups/group-name "konserwy rybne"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120705"
    :dc.warehouse.groups/group-name "ocet"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120706"
    :dc.warehouse.groups/group-name "pasztety"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120707"
    :dc.warehouse.groups/group-name "produkty z pomidorów"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120708"
    :dc.warehouse.groups/group-name "przyprawy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120709"
    :dc.warehouse.groups/group-name "sosy mokre"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120710"
    :dc.warehouse.groups/group-name "sosy w proszku"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120711"
    :dc.warehouse.groups/group-name "warzywa konserwowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120712"
    :dc.warehouse.groups/group-name "zupy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "1208"
    :dc.warehouse.groups/group-name "kawa"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120801"
    :dc.warehouse.groups/group-name "kawy mielone"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120802"
    :dc.warehouse.groups/group-name "kawy rozpuszczalne"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120803"
    :dc.warehouse.groups/group-name "kawy ziarniste"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "1209"
    :dc.warehouse.groups/group-name "kulinaria słodka"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120901"
    :dc.warehouse.groups/group-name "bakalie pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120902"
    :dc.warehouse.groups/group-name "desery w proszku"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120903"
    :dc.warehouse.groups/group-name "dodatki do ciast"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "120904"
    :dc.warehouse.groups/group-name "owoce w puszkach"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "1210"
    :dc.warehouse.groups/group-name "produkty spożywcze podstawowe"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121001"
    :dc.warehouse.groups/group-name "cukier"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121002"
    :dc.warehouse.groups/group-name "kasza"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121003"
    :dc.warehouse.groups/group-name "makaron"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121004"
    :dc.warehouse.groups/group-name "mąka"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121005"
    :dc.warehouse.groups/group-name "ryż"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121006"
    :dc.warehouse.groups/group-name "zabielacze do kawy"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "1211"
    :dc.warehouse.groups/group-name "słodycze"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121101"
    :dc.warehouse.groups/group-name "batony"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121102"
    :dc.warehouse.groups/group-name "bombonierki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121103"
    :dc.warehouse.groups/group-name "chałwa i sezamki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121104"
    :dc.warehouse.groups/group-name "ciastka"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121105"
    :dc.warehouse.groups/group-name "cukierki pakowane"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121106"
    :dc.warehouse.groups/group-name "czekolady"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121107"
    :dc.warehouse.groups/group-name "gumy do żucia"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121108"
    :dc.warehouse.groups/group-name "lizaki"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121109"
    :dc.warehouse.groups/group-name "słodycze luz"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121111"
    :dc.warehouse.groups/group-name "słodycze świąteczne"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "1212"
    :dc.warehouse.groups/group-name "dania dla niemowląt"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "121201"
    :dc.warehouse.groups/group-name "dania dla niemowląt"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "13"
    :dc.warehouse.groups/group-name "dla zwierząt"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "1301"
    :dc.warehouse.groups/group-name "karma i akcesoria dla zwierząt"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "130102"
    :dc.warehouse.groups/group-name "karma dla kotów"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "130103"
    :dc.warehouse.groups/group-name "karma dla psów"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "130105"
    :dc.warehouse.groups/group-name "piasek, żwirek"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "14"
    :dc.warehouse.groups/group-name "ogólna migracja"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "1401"
    :dc.warehouse.groups/group-name "ogólna migracja"
    :db/path                        [:mannheim/dc :dc/groups]},
   {:dc.warehouse.groups/group-id   "140101"
    :dc.warehouse.groups/group-name "ogólna migracja"
    :db/path                        [:mannheim/dc :dc/groups]}])
