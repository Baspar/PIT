# pit

pit Is a template based on our previous experiences.

## Usage

The template is not *yet* deployed on Clojars.
In order to use it, clone it and type:
```bash
cd pit
lein new pit {{project-name}}
```

## Default Dependencies
1. `[org.clojure/core.match      "0.3.0-alpha4"]`	
2. `[org.clojure/clojure         "1.8.0"]`
3. `[org.clojure/clojurescript   "1.9.229"]`
4. `[devcards                    "0.2.2"]`
5. `[rum                         "0.10.7"]`
6. `[cljsjs/react-dom            "15.3.1-0"]`
7. `[cljs-ajax                   "0.5.8"]`


params | dep | usage
--- | --- | ---
**+async** | `[org.clojure/core.async "0.2.395"]` | Clojure library to replace callback functions used in Ajax client
**+async** | `[io.nervous/kvlt "0.1.4"]` | HTTP client used along with core.async for HTTP requests (Ajax client cannot be used)
**+dispatch** | `[pit-dispatch "0.1.1"]` | Please see TODO

## License

Copyright © 2016

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
