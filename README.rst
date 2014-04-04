
.. image:: https://travis-ci.org/joninvski/even_better_password_maker.png?branch=master
   :target: http://travis-ci.org/joninvski/even_better_password_maker


Attribution
===========

Part of the code was based on passwordmaker-je.
https://code.google.com/p/passwordmaker-je/

Kudos to the dev of the project

Instructions
============

Note: Do not forget to set ANDROID_HOME variable.

To compile
----------

    ./gradlew compile

To deploy to phone
------------------

    ./gradlew installDebug

Test
----
    ./gradlew connectedInstrumentTest

Other targets
-------------

    * assemble
    The task to assemble the output(s) of the project
    * check
    The task to run all the checks.
    * build
    This task does both assemble and check
    * clean
    This task cleans the output of the project

Create apk for market
---------------------

./release.sh ~/path_to_keystore

Note
----

Do not forget to set and export the ANDROID_HOME and JAVA_HOME variables

e.g.

    ANDROID_HOME=/home/workspace/android/sdk; export ANDROID_HOME
    JAVA_HOME=/usr/lib/jvm/java-7-openjdk-i386/jre/; export JAVA_HOME


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
