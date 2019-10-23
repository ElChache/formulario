(ns formulario.validations.fns-test
  (:require [clojure.test :refer [deftest is testing]]
            [formulario.validations.fns :as fns]))

(deftest is-url-test
  (is (true? (fns/is-url? "http://www.example.com"))
      "Url with www and http")

  (is (true? (fns/is-url? "https://www.example.com"))
      "Url with www and https")

  (is (true? (fns/is-url? "https://example.com"))
      "Url with no www")

  (is (true? (fns/is-url? "https://sub.example.com"))
      "Url with no subdomain")

  (is (true? (fns/is-url? "https://example.com/path"))
      "Url with path")

  (is (true? (fns/is-url? "https://example.com/path?something=value&somethingelse=othervalue"))
      "Url with path and query parameters")

  (is (true? (fns/is-url? "https://sub.example.com:8444/path?something=value&somethingelse=othervalue"))
      "Url with a subdomain and port and path and query parameters"))

(deftest empty-test
  (is (true? (fns/empty? nil))
      "Nil is empty")

  (is (true? (fns/empty? ""))
      "Empty string is empty")

  (is (false? (fns/empty? "Not empty"))
      "Not empty string is not empty")

  (is (false? (fns/empty? 34))
      "A number is not empty")

  (is (false? (fns/empty? 0))
      "0 is not empty)")

  (is (false? (fns/empty? "0"))
      "0 is not empty)")

  (is (false? (fns/empty? '()))
      "Any other type is not empty"))

(deftest email-test
  (is (true? (fns/email? "avalidemail@gmail.com"))
      "Valid email")

  (is (false? (fns/email? "invalid email@gmail.com"))
      "Email with spaces is not valid")

  (is (false? (fns/email? "emailwithinvalidsymbols~!@#$%^&*()_+@gmail.com"))
      "not-allowed email characters")

  (is (false? (fns/email? ""))
      "Empty string is not a valid email")

  (is (false? (fns/email? "notdomainemail"))
      "Email with no domain is not valid")

  (is (false? (fns/email? "notdomainemail@"))
      "Email with @ but no domain is not valid")

  (is (false? (fns/email? "@gmail.com"))
      "Email with no account name is no valid")

  (is (false? (fns/email? "myemail@gmail"))
      "Email with invalid domain is not valid")

  (is (true? (fns/email? "myemail@gmail.net.ca"))
      "Domain with multiple subdomains should be allowed")

  (is (true? (fns/email? "myemail.with.dots@gmail.net.ca"))
      "Dots are allowed on emails"))

(deftest pos?-test
  (is (true? (fns/pos? 42))
      "Should return true with positive integers")

  (is (true? (fns/pos? 5.5))
      "Should return true for floats")

  (is (false? (fns/pos? 0))
      "Should return false with zero")

  (is (false? (fns/pos? -4))
      "Should return false with negative integer numbers")

  (is (false? (fns/pos? -4.5))
      "Should return false with negative float numbers")

  (is (false? (fns/pos? "4"))
      "Should return false with number strings")

  (is (false? (fns/pos? "0"))
      "Should return false with zero as a string")

  (is (false? (fns/pos? "abc"))
      "Should return false with any string")

  (is (false? (fns/pos? "0"))
      "Should return false with zero as a string")

  (is (false? (fns/pos? nil))
      "Should return false with nils")

  (is (false? (fns/pos? {}))
      "Should return false with maps")

  (is (false? (fns/pos? '()))
      "Should return false with lists"))

(ns formulario.validations.fns-test
  (:require [clojure.test :refer [deftest is testing]]
            [formulario.validations.fns :as fns]))

(deftest is-url-test
  (is (true? (fns/is-url? "http://www.example.com"))
      "Url with www and http")

  (is (true? (fns/is-url? "https://www.example.com"))
      "Url with www and https")

  (is (true? (fns/is-url? "https://example.com"))
      "Url with no www")

  (is (true? (fns/is-url? "https://sub.example.com"))
      "Url with no subdomain")

  (is (true? (fns/is-url? "https://example.com/path"))
      "Url with path")

  (is (true? (fns/is-url? "https://example.com/path?something=value&somethingelse=othervalue"))
      "Url with path and query parameters")

  (is (true? (fns/is-url? "https://sub.example.com:8444/path?something=value&somethingelse=othervalue"))
      "Url with a subdomain and port and path and query parameters"))

(deftest empty-test
  (is (true? (fns/empty? nil))
      "Nil is empty")

  (is (true? (fns/empty? ""))
      "Empty string is empty")

  (is (false? (fns/empty? "Not empty"))
      "Not empty string is not empty")

  (is (false? (fns/empty? 34))
      "A number is not empty")

  (is (false? (fns/empty? 0))
      "0 is not empty)")

  (is (false? (fns/empty? "0"))
      "0 is not empty)")

  (is (false? (fns/empty? '()))
      "Any other type is not empty"))

(deftest email-test
  (is (true? (fns/email? "avalidemail@gmail.com"))
      "Valid email")

  (is (false? (fns/email? "invalid email@gmail.com"))
      "Email with spaces is not valid")

  (is (false? (fns/email? "emailwithinvalidsymbols~!@#$%^&*()_+@gmail.com"))
      "not-allowed email characters")

  (is (false? (fns/email? ""))
      "Empty string is not a valid email")

  (is (false? (fns/email? "notdomainemail"))
      "Email with no domain is not valid")

  (is (false? (fns/email? "notdomainemail@"))
      "Email with @ but no domain is not valid")

  (is (false? (fns/email? "@gmail.com"))
      "Email with no account name is no valid")

  (is (false? (fns/email? "myemail@gmail"))
      "Email with invalid domain is not valid")

  (is (true? (fns/email? "myemail@gmail.net.ca"))
      "Domain with multiple subdomains should be allowed")

  (is (true? (fns/email? "myemail.with.dots@gmail.net.ca"))
      "Dots are allowed on emails"))

(deftest pos?-test
  (is (true? (fns/pos? 42))
      "Should return true with positive integers")

  (is (true? (fns/pos? 5.5))
      "Should return true for floats")

  (is (false? (fns/pos? 0))
      "Should return false with zero")

  (is (false? (fns/pos? -4))
      "Should return false with negative integer numbers")

  (is (false? (fns/pos? -4.5))
      "Should return false with negative float numbers")

  (is (false? (fns/pos? "4"))
      "Should return false with number strings")

  (is (false? (fns/pos? "0"))
      "Should return false with zero as a string")

  (is (false? (fns/pos? "abc"))
      "Should return false with any string")

  (is (false? (fns/pos? "0"))
      "Should return false with zero as a string")

  (is (false? (fns/pos? nil))
      "Should return false with nils")

  (is (false? (fns/pos? {}))
      "Should return false with maps")

  (is (false? (fns/pos? '()))
      "Should return false with lists"))

(deftest not-neg-number?-test
  (is (true? (fns/not-neg-number? 42))
      "Should return true with positive integers")

  (is (true? (fns/not-neg-number? 5.5))
      "Should return true for floats")

  (is (true? (fns/not-neg-number? 0))
      "Should return true with zero")

  (is (false? (fns/not-neg-number? -4))
      "Should return false with negative integer numbers")

  (is (false? (fns/not-neg-number? -4.5))
      "Should return false with negative float numbers")

  (is (false? (fns/not-neg-number? "4"))
      "Should return false with number strings")

  (is (false? (fns/not-neg-number? "0"))
      "Should return false with zero as a string")

  (is (false? (fns/not-neg-number? "abc"))
      "Should return false with any string")

  (is (false? (fns/not-neg-number? "0"))
      "Should return false with zero as a string")

  (is (false? (fns/not-neg-number? nil))
      "Should return false with nils")

  (is (false? (fns/not-neg-number? {}))
      "Should return false with maps")

  (is (false? (fns/not-neg-number? '()))
      "Should return false with lists"))