.. image:: https://img.shields.io/travis/TeleSign/java_telesign.svg?branch=master
    :target: https://travis-ci.org/TeleSign/java_telesign

.. image:: https://img.shields.io/maven-central/v/com.telesign/telesign.svg
    :target: http://maven-repository.com/artifact/com.telesign/telesign/

.. image:: https://img.shields.io/github/license/TeleSign/java_telesign.svg
    :target: https://github.com/TeleSign/java_telesign/blob/master/LICENSE.txt

========
TeleSign
========

TeleSign provides the world’s most comprehensive approach to account security for Web and mobile applications.

For more information about TeleSign, visit the `TeleSign website <http://www.TeleSign.com>`_.

TeleSign REST API: Java SDK
---------------------------

**TeleSign web services** conform to the `REST Web Service Design Model
<http://en.wikipedia.org/wiki/Representational_state_transfer>`_. Services are exposed as URI-addressable resources
through the set of *RESTful* procedures in our **TeleSign REST API**.

The **TeleSign Java SDK** is a set classes and functions — a *Java Library* that wraps the
TeleSign REST API, and it simplifies TeleSign application development in the `Java programming language
<https://www.oracle.com/java>`_. The SDK software is distributed on
`GitHub <https://github.com/TeleSign/java_telesign>`_ and also as a Java Software Package using the
`The Central Repository (Maven) <https://search.maven.org>`_.

Documentation
-------------

Detailed documentation for TeleSign REST APIs is available in the `Developer Portal <https://developer.telesign.com/>`_.

Installation
------------

To add the TeleSign Java SDK to your Gradle project:

.. code-block:: groovy

    compile 'com.telesign.rest:telesign:[current_version]'

To add the TeleSign Java SDK to your Maven project:

.. code-block:: xml

    <dependency>
        <groupId>com.telesign</groupId>
        <artifactId>telesign</artifactId>
        <version>[current_version]</version>
    </dependency>

Java Code Example: Messaging
----------------------------

Here's a basic code example with JSON response.

.. code-block:: java

    String customerId = "customer_id";
    String secretKey = "secret_key";

    String phoneNumber = "phone_number";
    String message = "You're scheduled for a dentist appointment at 2:30PM.";
    String messageType = "ARN";

    MessagingClient messagingClient = new MessagingClient(customerId, secretKey);
    RestClient.TelesignResponse telesignResponse = messagingClient.message(phoneNumber, message, messageType, null);

.. code-block:: javascript
    
    {'reference_id': 'DGFDF6E11AB86303ASDFD425BE00000657',
     'status': {'code': 103,
        'description': 'Call in progress',
        'updated_on': '2016-12-12T00:39:58.325559Z'}}

For more examples, see the examples folder or visit `TeleSign Developer Portal <https://developer.telesign.com/>`_.

Authentication
--------------

You will need a Customer ID and API Key in order to use TeleSign’s REST API. If you are already a customer and need an
API Key, you can generate one in the `Portal <https://portal.telesign.com>`_.
