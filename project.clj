(defproject formulario "0.0.6-SNAPSHOT"
  :description "A clojurescript library to ease the pain of forms in re-frame"
  :license "MIT"
  :url ""
  :source-paths ["src"]
  :test-paths []
  :resource-paths []
  :compile-path nil
  :target-path nil
  :plugins [[lein-tools-deps "0.4.1"]]
  :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
  :lein-tools-deps/config {:config-files [:install :user :project]
                           :aliases [:dev :test]})