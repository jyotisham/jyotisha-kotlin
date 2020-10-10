# JyotiSha tools and data

## Intro
A package to do various panchaanga (traditional vedic astronomical / astrological) calculations, produce calendars.

For a survey of similar software, see [here](https://sanskrit-coders.github.io/astronomy/).

### Vision
- This code will have common panchaanga and utsava computation parts which can be used with platform specific ephemeris code; while reusing the old festival db from [adyatithi](https://github.com/sanskrit-coders/adyatithi).
- The code itself will be capable of typical (amAnta, chitra-at-180 ayanAMsha, असङ्क्रान्तिमासोऽधिकः ) based calculation as well as the ancient but now uncommon [tropical lunisolar system](https://vvasuki.github.io/jyotiSham/history/kauNDinyAyana/).

## For users
For detailed examples and help, please see individual module files - especially test files in this package.

## For contributors
Contributions welcome! Please see some basic comments (pertaining to the time format used internally, API layers required) in the base jyotisha package though.

### Testing and autotesting
Every push to this repository SHOULD pass tests. We should have a rich, functional set of tests at various levels. Saves everyone's time.

You can see the status of failing tests and builds at https://github.com/sanskrit-coders/jyotisha-kotlin/actions . PS: You can probably subscribe to get email notification on failed workflow runs as well - I'm getting these.

### Contact
Have a problem or question? Please head to [github](https://github.com/sanskrit-coders/jyotisha-kotlin).

## Why Kotlin?
- Besides multi-platform targetting, Kotlin was chosen (over say Scala) because of it's good corporate (intellij) support + adaption, simplicity, richer language features (compared to Java). 
- The compact and popular swiss ephemeris code is ported for use in JVM languages as well.
