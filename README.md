<!-- 
    Couple of points about editing:
    
    1. Keep it SIMPLE.
    2. Refer to reference docs and other external sources when possible.
    3. Remember that the file must be useful for new / external developers, and stand as a documentation basis on its own.
    4. Try to make it as informative as possible.
    5. Do not put data that can be easily found in code.
    6. Include this file on ALL branches.
-->

<!-- Put your project's name -->
# Inbbbox Android

<!-- METADATA -->
<!-- Add links to JIRA, Google Drive, mailing list and other relevant resources -->
<!-- Add links to CI configs with build status and deployment environment, e.g.: -->
| environment |Jira            | deployment            |  build status      |  coverage          |
|-------------|-----------------------|-----------------------|--------------------|--------------------|
| Android     |[`JIRA`](https://netguru.atlassian.net/secure/RapidBoard.jspa?rapidView=214&projectKey=IA) |[`HockeyApp`](https://rink.hockeyapp.net/manage/apps/404535) | [![Build Status](https://www.bitrise.io/app/cf503ac188f43ade.svg?token=k9n-sH184dmLSBSQRnW_qg&branch=master)](https://www.bitrise.io/app/cf503ac188f43ade) |[![codecov](https://codecov.io/gh/netguru/inbbbox-android/branch/master/graph/badge.svg?token=0UKDDNsV4s)](https://codecov.io/gh/netguru/inbbbox-android) |
<!--- If applies, add link to app on Google Play -->


## Synopsis
<!-- Describe the project in few sentences -->
Inbbbox is mobile client of dribbble.com service.

## Development

### Architecture
Reactive MVP with Dagger2 dependency injection.
<!-- Describe the main architectural pattern used in the project, optionally put a flowchart -->

### Integrations
<!-- Describe external service and hardware integrations, link to reference docs, use #### headings -->
Inbbbox app is integrated with dribbble.com service using [Dribbble API v1](http://developer.dribbble.com/v1/)

### Coding guidelines
[Netguru Android code style guide](https://netguru.atlassian.net/wiki/display/ANDROID/Android+best+practices)
<!-- OPTIONAL: Describe any additional coding guidelines (if non-standard) -->

### Workflow & code review
[Netguru development workflow](https://netguru.atlassian.net/wiki/display/DT2015/Netguru+development+flow)
<!-- OPTIONAL: Describe workflow and code review process (if non-standard) --> 

## Testing
<!-- Describe the project's testing methodology -->
<!-- Examples: TDD? Using Espresso for views? What parts must be tested? etc -->
 - Unit Testing using Mockito + JUnit. Tests concerns Presnters layer and partially Model layer of MVP architecture


## Building
<!-- Aim to explain the process so that any new or external developer not familiar with the project can perform build and deploy -->

### Build types
<!-- List and describe build types -->
#### debug
 - debuggable
 - disabled ProGuard
 - uses built-in shrinking (no obfuscation)
 
#### release
 - uses full ProGuard configuration
 - enables zipAlign, shrinkResources
 - non-debuggable

### Product flavors
<!-- List and describe product flavors, purposes and dedicated deployment channels -->
#### mock
 - mocked data, functional testing
 
#### production
 - production API, real data,release

### Build properties
<!-- List all build properties that have to be supplied, including secrets. Describe the method of supplying them, both on local builds and CI -->

| Property         | External property name | Environment variable |
|------------------|------------------------|----------------------|
| HockeyApp App ID production | HockeyAppAppIdProd            | hockeyAppIdProd        |
| Dribble client key | DribbbleClientKey            | dribbbleClientSecret        |
| Dribble client secret | DribbbleClientSecret            | dribbbleClientSecret        |
| Dribble client redirect key | DribbbleOauthRedirect            | dribbbleOauthRedirect        |
| Dribble client token | DribbbleClientToken            | dribbbleClientToken        |

#### Secrets
Follow [this guide](https://netguru.atlassian.net/wiki/pages/viewpage.action?pageId=33030753) 

#### Other properties
 - SONAR_ACCESS_TOKEN - with access token to sonarQube server;
 - CODECOV_TOKEN - token for upload code coverage reports for CodeCove account;
 - GithubRepoName - repository name
 - GithubOwnerName - repository owner name

### ProGuard
<!-- Describe ProGuard configuration: is it enabled? Any unusual stuff? -->
Proguard configuration is placed in proguard-rules.pro. Proguard is enabled only for Release build variants.
In proguard-rules.pro file there are rules for such libs/tools like:
 - app compat-v7
 - FragmentArgs
 - Gson
 - retrofit
 - dagger
 - xlog
 - stetho
 - leak canary
 - fabric
 - rx
 - glide
 - BugTags
 - AutoBundle
 - AutoGson
 - AutoParcel
 - GreenDao

## Deployment
<!-- Aim to explain the process so that any new or external developer can perform deploy -->

### Bitrise
<!-- Describe the Continuous Integration process: Bitrise workflows, global configs etc. -->
 Bitrise is separated for workflow mentioned below. Feature,
 - feature - workflow triggered on Push to feature or bug brunch;
 - MasterPR -  workflow triggered on Pull Request for feature/bug brunch to Master brunch
 - Master - workflow triggered on Push or PR marge to Master brunch;

 Each workflows has defined such steps us:
  - Unit tests,
  - CodeCove rapports upload,
  - SonarCube analysis,
  - lintAnalysis
  - build of productionRelease flavour of the application;
  - build upload build apk file to bitrise;
  Additionally MasterPR step there is SigningApk step and deploy to HockeyApp step

### HockeyApp / Fabric environments
<!-- Describe the deployment channels -->
 - Project use HockeyApp for beta distribution and Crash managing and monitoring. Deploy to Hockeyapp is performed automatically by Bitrise system in Master step.


### Supported devices
<!-- Describe the supported and target devices (do not put stuff that can be easily found in build.gradle files) -->
 - all devices from API level 19 - Android 4.4+
 - no devices restricted yet