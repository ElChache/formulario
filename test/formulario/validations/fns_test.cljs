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