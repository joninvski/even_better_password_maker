
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

Note
----

Do not forget to set and export the ANDROID_HOME and JAVA_HOME variables

e.g.

    ANDROID_HOME=/home/workspace/android/sdk; export ANDROID_HOME
    JAVA_HOME=/usr/lib/jvm/java-7-openjdk-i386/jre/; export JAVA_HOME
