Even Better Passwork Maker
==========================

[![Build Status](https://travis-ci.org/joninvski/even_better_password_maker.svg?branch=master)](https://travis-ci.org/joninvski/even_better_password_maker)

Attribution
-----------

Part of the code was based on passwordmaker-je.
https://code.google.com/p/passwordmaker-je/

Kudos to the dev of the project

Compile
-------

    ANDROID_HOME=/home/.../android/sdk; export ANDROID_HOME # Optional
    ./gradlew assemble

Install on device
-----------------

    # Make sure emulator is running or connected to real device
    ./gradlew installDebug

Activity tests
--------------

    # Installs and runs the tests for Build 'debug' on connected devices.
    ./gradlew connectedAndroidTest

    # Runs all the instrumentation test variations on all the connected devices
    ./gradlew spoon        # results in app/build/spoon/debug/index.html

Code quality
------------

    # Runs lint on all variants
    ./gradlew lint         # results in app/build/lint-results.html

    # Run tests and generate Cobertura coverage reports
    ./gradlew cobertura    # results in domain/build/reports/cobertura/index.html

    # Checks if the code is accordings with the code style
    ./gradlew domain:check app:checktyle   # results in domain/build/reports/checkstyle/main.xml

Robelectric tests
-----------------

    # Runs the roboelectric tests (do not need an emeulator)
    ./gradlew :app:testDebug

Unit tests
----------

    # Run the unit tests of the domain subproject
    ./gradlew :domain:test # Check the results in _domain/build/reports/tests/index.html_

Create apk for market
---------------------

    ./release.sh ~/path_to_keystore

Libraries Used
--------------

- [Robotium](http://code.google.com/p/robotium/)
- Square's [Spoon](http://square.github.io/spoon/)
- Jake Wharton's [Butterknife](http://jakewharton.github.io/butterknife/)
- Michael Evan's [Chromehashview](https://github.com/MichaelEvans/ChromaHashView)
- Google's [Android Support Library v4](http://developer.android.com/reference/android/support/v4/app/package-summary.html)
- Google's [Android Support AppCompact v7](https://developer.android.com/reference/android/support/v7/appcompat/package-summary.html)
- [Joda Time](http://www.joda.org/joda-time/)
- Roberto Tyley's [Spongy Castle](http://rtyley.github.io/spongycastle/) - bouncy castle replacement for android

License
-------

    /*
     * EvenBetterPasswordMaker
     * Copyright (C) 2014 Joao Trindade
     *
     * This program is free software: you can redistribute it and/or modify
     * it under the terms of the GNU General Public License as published by
     * the Free Software Foundation, either version 3 of the License, or
     * (at your option) any later version.
     *
     * This program is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     * GNU General Public License for more details.
     *
     * You should have received a copy of the GNU General Public License
     * along with this program.  If not, see <http://www.gnu.org/licenses/>.
     */
