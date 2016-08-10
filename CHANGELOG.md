## 2016-06-30 - [Unreleased] 
- 2016-06-30 
  - Merge pull request #54 from Manmohan-TechM/July_2015
- 2016-06-24 
  - Merge pull request #53 from erikkai/patch-2
Formatting, link fix 
- 2016-06-22 
  - Renaming resource variable(Code cleanup)
  - TMR-141/142 : Changes done for handling changes in AuthMethod enumeration value,  used when signing the Authentication header.
 
- 2016-06-21 
  - Formatting, link fix
One of the links broke, and it wasn't displaying correctly. This version corrects all of that. Also a problem with how it was showing a block of code. 
- 2016-06-17 
  - Merge pull request #52 from erikkai/patch-1
 
- 2016-06-17 
  - Broken link, style changes, added punctuation
Changes were that simple - needed to correct summarized items listed in title. 
- 2016-06-14 
  - Merge pull request #51 from erikkai/patch-1
 
- 2016-06-14 
  - Update readme.rst
 
- 2016-01-17 
  - Merge branch 'Manmohan-TechM-July_2015'
 
- 2016-01-15 
  - Changes done for throwing underlying IOException for better visibility of underlying subclasses in all APIs.
 
## [0.5.0] - 2016-01-15 - http://central.maven.org/maven2/com/telesign/telesign/0.5.0/
  - Merge pull request #32 from Manmohan-TechM/July_2015
Updated version to 0.5.0 
- 2016-01-14 
  - Updated version to 0.5.0 - Changes done to configure Https protocol via constructor parameters, performed code cleanup to remove redundant code.
 
- 2016-01-05 
  - Merge pull request #26 from gibffe/base_url_refactor
Removing string literals from method bodies 
- 2015-12-24 
  - Capitalising constants.
 
- 2015-12-16 
  - Renaming resource variables.
 
- 2015-12-16 
  - Merge branch 'master' of https://github.com/TeleSign/java_telesign into base_url_refactor
 
- 2015-12-16 
  - Capturing baseUrl and resources suffixes in static variables.
 
- 2015-12-16 
  - Capturing baseUrl and resource suffixes in static variables.
 
## [0.4.0] - 2015-12-03 - http://central.maven.org/maven2/com/telesign/telesign/0.4.0/
  - Merge pull request #24 from Manmohan-TechM/July_2015
Comitting changes for Verify Push, Verify Softtoken, Verify Registration, TLS. 
- 2015-12-03 
  - updated version to 0.4.0
 
- 2015-12-03 
  - Updated for formatting issues.
 
- 2015-12-02 
  - Added verify status overloaded method.
 
- 2015-12-01 
  - For addressing TMR-90
 
- 2015-12-01 
  - added overloaded method to make in sync with previous versions
 
- 2015-12-01 
  - Added overloaded method for call.
 
- 2015-11-12 
  - Updated exception messages
 
- 2015-11-12 
  - Merge branch 'master' into July_2015
Conflicts:
	src/com/telesign/verify/response/VerifyResponse.java
 
- 2015-11-12 
  - changes done for: - SmartVerify, Verify push, Verify softtoken, Verify Registration - set default TLS version to 1.2
 
- 2015-11-04 
  - Merge pull request #22 from gibffe/build_with_debug
Adding debug info to the jar. Generating sources jar during build. 
- 2015-11-03 
  - Adding debug info to the jar. Generating sources jar during build.
 
- 2015-11-02 
  - Merge pull request #20 from gibffe/shared_gson_instance
Shared Gson instance 
- 2015-11-02 
  - Preventing gson instance from being serialized. Adding verbose junit error output.
 
- 2015-10-13 
  - Merge branch 'shared_gson_instance' of https://github.com/gibffe/java_telesign into shared_gson_instance
 
- 2015-10-13 
  - Gson instance is thread safe - removing reduntand instantiations.
 
- 2015-10-13 
  - Gson instance is thread safe - removing reduntant instantiations.
 
- 2015-10-01 
  - Committing changes for Verify registration, smart Verify API's
 
## [0.3.0] - 2015-08-20 - http://central.maven.org/maven2/com/telesign/telesign/0.3.0/
  - Adding carrier info to PhoneID Live and Contact responses.

## [0.2.0] - 2015-08-19 - http://central.maven.org/maven2/com/telesign/telesign/0.2.0/
  - Merge branch 'Manmohan-TechM-July_2015'
 
- 2015-08-19 
  - Merge branch 'July_2015' of git://github.com/Manmohan-TechM/java_telesign into Manmohan-TechM-July_2015
 
- 2015-08-19 
  - Updated comments to PhoneId
 
- 2015-08-19 
  - Changes done as part of TMR-61 -PhoneID Score response - LZU-535-31903
 
- 2015-08-17 
  - Merge pull request #16 from Manmohan-TechM/July_2015
Removed special characters as it was giving errors while running Junit test 
- 2015-08-17 
  - Removed special characters as it was giving errors while running Junit test.
 
- 2015-08-13 
  - Merge pull request #15 from Manmohan-TechM/July_2015
Adding pom.xml used while migrating to Maven central 
- 2015-08-13 
  - Merge pull request #14 from daplay/master
use Locale.US for date header 
- 2015-08-03 
  - Added Originating_ip & Session_id to PhoneId & Verify calls. Changes done as part of PD-14562.
 
## [0.1.0] - 2015-07-30 - http://central.maven.org/maven2/com/telesign/telesign/0.1.0/
  - Updated pom.xml
 
- 2015-07-17 
  - fixes date header locale
 
- 2015-07-29 
  - adding pom.xml used while migrating to Maven central
 
- 2015-07-20 
  - Merge pull request #13 from Manmohan-TechM/July_2015
Adding connect and read timeouts as configurable parameters. 
- 2015-07-16 
  - Removed redundant 'url.openConnection() & additional timeouts'
 
- 2015-07-03 
  - Added carrier information to PhoneId Standard response. Change done as part of PD-16299.
 
- 2015-07-01 
  - Adding connect and read timeouts as configurable parameters.
 
- 2014-09-24 
  - Merge pull request #12 from dominicpezzuto/master
Specify UTF-8 encoding in ant build.xml file 