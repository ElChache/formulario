(ns formulario.validation-test
  (:require [cljs.test :refer [deftest is testing]]
            [formulario.validations :as validations]
            [formulario.validation :as validation]))

(deftest validate
  (testing "Validating one required field with nil value"
    (let [form-data {:name nil}
          name-msg "Name is required"
          validation {:name (validations/required? name-msg)}
          res {:name name-msg}]
      (is (= res (validation/validate form-data validation)))))

  (testing "Validating one required field with empty value"
    (let [form-data {:name ""}
          name-msg "Name is required"
          validation {:name (validations/required? name-msg)}
          res {:name name-msg}]
      (is (= res (validation/validate form-data validation)))))

  (testing "Validating two invalid fields"
    (let [form-data {:name ""
                     :age -23}
          name-msg "Name is required"
          age-msg "Not a valid age"
          validation {:name (validations/required? name-msg)
                      :age (validations/pos-int? age-msg)}
          res {:name name-msg
               :age age-msg}]
      (is (= res (validation/validate form-data validation)))))

  (testing "Combining validations in an invalid form, failing first"
    (let [form-data {:id "a-string-id"}
          pos-int-message "Not a positive number"
          validation {:id (validation/validations
                            (validations/required?)
                            (validations/pos-int? pos-int-message))}
          res {:id pos-int-message}]
      (is (= res (validation/validate form-data validation)))))

  (testing "Combining validations in an invalid form, failing second"
    (let [form-data {:id ""}
          required-message "Id is required"
          validation {:id (validation/validations
                            (validations/required? required-message)
                            (validations/pos-int?))}
          res {:id required-message}]
      (is (= res (validation/validate form-data validation)))))

  (testing "Combining validations in a valid form"
    (let [form-data {:id 23}
          required-message "Id is required"
          validation {:id (validation/validations
                            (validations/required? required-message)
                            (validations/pos-int?))}
          res {:id nil}]
      (is (= res (validation/validate form-data validation)))))

  (testing "validating empty form"
    (let [form-data {}
          name-msg "Name is required"
          validation {:name (validations/required? name-msg)}
          res {:name name-msg}]
      (is (= res (validation/validate form-data validation)))))

  (testing "validating with empty validations"
    (let [form-data {:id 23}
          name-msg "Name is required"
          validation {}
          res {}]
      (is (= res (validation/validate form-data validation)))))
  )

(deftest valid?
  (testing "Combining validations in an invalid form, failing first"
    (let [form-data {:id "a-string-id"}
          pos-int-message "Not a positive number"
          validation {:id (validation/validations
                            (validations/required?)
                            (validations/pos-int? pos-int-message))}]
      (is (= false (validation/valid? (validation/validate form-data validation))))))

  (testing "Combining validations in an invalid form, failing second"
    (let [form-data {:id ""}
          required-message "Id is required"
          validation {:id (validation/validations
                            (validations/required? required-message)
                            (validations/pos-int?))}]
      (is (= false (validation/valid? (validation/validate form-data validation))))))

  (testing "Combining validations in a valid form"
    (let [form-data {:id 23}
          required-message "Id is required"
          validation {:id (validation/validations
                            (validations/required? required-message)
                            (validations/pos-int?))}]
      (is (= true (validation/valid? (validation/validate form-data validation)))))))