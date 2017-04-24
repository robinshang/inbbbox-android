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

[![Build Status](https://www.bitrise.io/app/cf503ac188f43ade.svg?token=k9n-sH184dmLSBSQRnW_qg&branch=master)](https://www.bitrise.io/app/cf503ac188f43ade) [![codecov](https://codecov.io/gh/netguru/inbbbox-android/branch/master/graph/badge.svg?token=0UKDDNsV4s)](https://codecov.io/gh/netguru/inbbbox-android)

With Inbbbox you can discover excellent visual works from Dribbble, the global directory for digital design. If you want to grab a copy for yourself just go straight to [Google Play](https://www.netguru.co/opensource) (link needed). However, if you are interested in the core of Inbbbox, check out [Configuration](#configuration) section. Note that Dribbble account is necessary for using the app.

We at Netguru strongly believe in open-source software. Inbbbox isn’t our only project repo where you can find the app’s full source code. Explore other [open source projects](https://www.netguru.co/opensource) created by our team.

## Configuration

### Instructions

1. Clone repo at `https://github.com/netguru/inbbbox-android.git`
2. Register your app for dribbble api [here](https://dribbble.com/account/applications/)
2. Create secret.properties file in main folder (inbbbox-android) and paste following contents:
`Dribbbleclientkey=[1]
Dribbbleclientsecret=[2]
Dribbbleclienttoken=[3]
Dribbbleoauthredirect=[4]
Hockeyappappiddev=optional
Hockeyappappidprod=optional
SonarAccessToken=optional
GithubOauthToken=optional
GithubRepoName=optional
GithubOwnerName=optional`
3. Fill the gap [1] with Client ID from your [dribble app](https://dribbble.com/account/applications/)
4. Fill the gap [2] with Client Secret from your [dribble app](https://dribbble.com/account/applications/)
5. Fill the gap [3] with Client Access Token from your [dribble app](https://dribbble.com/account/applications/)
6. Fill the gap [4] with the scheme of Callback URL you set in your [dribble app](https://dribbble.com/account/applications/), e.g. for inbbbox://example.com it would be inbbbox
7. Open project in Android Studio
8. Remember to check if Build Variant is set to noIntegrationsDebug
9. That's it!

## Contribution

You're more than welcome to contribute or report an issue in case of any problems, questions or improvement proposals.

## Authors

* [Designers Team](https://dribbble.com/netguru)

    * [Bartosz Białek](https://dribbble.com/bkbl)
    * [Mateusz Czajka](https://dribbble.com/czajkovsky)
    * [Michał Parulski](https://dribbble.com/Shuma87)
    * [Dawid Woźniak](https://dribbble.com/dawidw)
    * [Paweł Kontek](https://dribbble.com/pawelkontek)

* [Developers Team](https://github.com/netguru/inbbbox-android/graphs/contributors)

    * [Maciej Markiewicz](https://github.com/mmarkiew)
    * [Filip Zych](https://github.com/navarionek)
    * [Joanna Jasnowska](https://github.com/a-jottt)
    * [Rafał Naniewicz](https://github.com/freszu)
    * [Łukasz Janyga](https://github.com/alvarg93)
    * [Grzegorz Podbielski](https://github.com/Dabler)
    * [Gonzalo Acosta](https://github.com/GNZ)
    * [Maciej Janusz](https://github.com/maciekjanusz)

Copyright © 2016 [Netguru](http://netguru.co).
