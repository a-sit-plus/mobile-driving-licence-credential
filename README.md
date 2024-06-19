# Mobile Driving Licence

[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-brightgreen.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-multiplatform--mobile-orange.svg?logo=kotlin)](http://kotlinlang.org)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.10-blue.svg?logo=kotlin)](http://kotlinlang.org)
![Java](https://img.shields.io/badge/java-17-blue.svg?logo=OPENJDK)
[![Maven Central](https://img.shields.io/maven-central/v/at.asitplus.wallet/mobiledrivinglicence)](https://mvnrepository.com/artifact/at.asitplus.wallet/mobiledrivinglicence/)

Use data representing mobile driving licences as a ISO 18013-5 Credential, with the help
of [KMM VC Lib](https://github.com/a-sit-plus/kmm-vc-library).

Be sure to call `at.asitplus.wallet.mdl.Initializer.initWithVcLib` first thing in your application.

These attributes are implemented:

- `family_name`
- `given_name`
- `birth_date`
- `issue_date`
- `expiry_date`
- `issuing_country`
- `issuing_authority`
- `document_number`
- `portrait`
- `driving_privileges`
- `un_distinguishing_sign`
- `administrative_number`
- `sex`
- `height`
- `weight`
- `eye_colour`
- `hair_colour`
- `birth_place`
- `resident_address`
- `portrait_capture_date`
- `age_in_years`
- `age_birth_year`
- `age_over_18`
- `issuing_jurisdiction`
- `nationality`
- `resident_city`
- `resident_state`
- `resident_postal_code`
- `resident_country`
- `family_name_national_character`
- `given_name_national_character`
- `signature_usual_mark`

## Changelog

Release 1.0.0:

- Initial release, imported from vclib 3.7.0
