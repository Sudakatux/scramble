# scramble

This is the scramble project.

## Running

To run the application just type `lein run`
To run the frontend with autocompile run `lein figwheel`

## Running Tests
Tests depend on node to be able to test with React components.
To run tests first run `npm install` this will install the jsDom dependency
then to run the frontend tests type `lein cljsbuild auto js-dom-test`

To run backend and clj/cljc tests run `lein midje`


