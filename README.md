# AllerGenie

Modern Web Application build with love and Clojure.

## Technology used:

Hosting - https://heroku.com/<br/>
Front End/Back End - Clojure with some JavaScript and HTML/CSS<br/>
Database - MongoDB

## API Used:

https://www.pollen.com/ - for the pollen info.<br/>
https://openweathermap.org/ - for the weather forecast.<br/>
https://www.airnow.gov/ - for the Air Quality Index in US.

## Link

[AllerGenie](https://clojure-allergenie.herokuapp.com)

## Installation

Clone or download source code from https://github.com/Romacoding/allergenie-clojure.

## Usage

    "$ lein run dev" for development or "lein run" for the production version.

The app will be avalible on localhost:3000.

## API keys

Create config.edn file in /resources directory with your API keys.

    Ex. {:port "3000", :airkey "Your airnow key", :weatherkey "Your openweather key"}

### Bugs

No bugs reported yet.

## License

Copyright © 2020 AllerGenie

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
