# Mobile Driving Licence

<div align="center">
 
[![A-SIT Plus Official](https://raw.githubusercontent.com/a-sit-plus/a-sit-plus.github.io/709e802b3e00cb57916cbb254ca5e1a5756ad2a8/A-SIT%20Plus_%20official_opt.svg)](https://plus.a-sit.at/open-source.html)
[![Powered by VC-K](https://img.shields.io/badge/VC--K-powered-8A2BE2?logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA4LjAzIDkuNSI+PGcgZmlsbD0iIzhhMmJlMiIgZm9udC1mYW1pbHk9IlZBTE9SQU5UIiBmb250LXNpemU9IjEyLjciIHRleHQtYW5jaG9yPSJtaWRkbGUiPjxwYXRoIGQ9Ik01OS42NCAyMjIuMTNxMC0uOTguMzYtMS44Mi4zNy0uODQuOTgtMS40Ni42Mi0uNjIgMS40Ni0uOTYuODMtLjM2IDEuOC0uMzUgMS4wMy4wMiAxLjkuNDIuODcuNCAxLjUgMS4xMi4wNC4wNS4wMy4xMSAwIC4wNy0uMDUuMWwtMSAuODZxLS4wNi4wMy0uMTIuMDN0LS4xLS4wNnEtLjQyLS40OC0xLS43Ni0uNTYtLjMtMS4yMi0uMjgtLjYuMDEtMS4xMy4yNy0uNTQuMjQtLjkzLjY3LS40LjQyLS42Mi45OC0uMjMuNTYtLjIzIDEuMiAwIC42My4yNCAxLjE4LjI0LjU2LjY1Ljk4LjQuNDIuOTQuNjYuNTMuMjMgMS4xNC4yMy42My0uMDEgMS4yLS4zLjU1LS4yNy45Ni0uNzUuMDQtLjA1LjEtLjA1LjA2LS4wMi4xMS4wM2wxIC44NnEuMDYuMDMuMDYuMS4wMS4wNi0uMDMuMTEtLjY0LjczLTEuNTMgMS4xNC0uOS40MS0xLjk1LjQtLjk1IDAtMS43OS0uMzYtLjgyLS4zNy0xLjQzLS45OS0uNjEtLjYzLS45NS0xLjQ4LS4zNS0uODUtLjM1LTEuODN6IiBzdHlsZT0iLWlua3NjYXBlLWZvbnQtc3BlY2lmaWNhdGlvbjpWQUxPUkFOVDt0ZXh0LWFsaWduOmNlbnRlciIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoLTU5LjY0IC0yMTcuNDIpIi8+PHBhdGggZD0iTTY2LjIxIDIyMS4zNWgxLjNjLjEgMCAuMTYuMDYuMTYuMTd2MS4zOGMwIC4xMS0uMDUuMTctLjE2LjE3aC0xLjNjLS4xIDAtLjE2LS4wNi0uMTYtLjE3di0xLjM4YzAtLjExLjA1LS4xNy4xNi0uMTd6IiBsZXR0ZXItc3BhY2luZz0iLTMuMTIiIHN0eWxlPSItaW5rc2NhcGUtZm9udC1zcGVjaWZpY2F0aW9uOlZBTE9SQU5UO3RleHQtYWxpZ246Y2VudGVyIiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgtNTkuNjQgLTIxNy40MikiLz48L2c+PC9zdmc+&logoColor=white&labelColor=white)](https://github.com/a-sit-plus/vck)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-brightgreen.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-multiplatform--mobile-orange.svg?logo=kotlin)](http://kotlinlang.org)
[![Kotlin](https://img.shields.io/badge/kotlin-2.2.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
![Java](https://img.shields.io/badge/java-17-blue.svg?logo=OPENJDK)
[![Maven Central](https://img.shields.io/maven-central/v/at.asitplus.wallet/mobiledrivinglicence)](https://mvnrepository.com/artifact/at.asitplus.wallet/mobiledrivinglicence/)

</div>

Use data representing mobile driving licences as a ISO 18013-5 Credential, with the help
of [VC-K](https://github.com/a-sit-plus/vck).

Be sure to call `at.asitplus.wallet.mdl.Initializer.initWithVCK` first thing in your application.

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
- `age_over_12`
- `age_over_13`
- `age_over_14`
- `age_over_16`
- `age_over_18`
- `age_over_21`
- `age_over_25`
- `age_over_60`
- `age_over_62`
- `age_over_65`
- `age_over_68`
- `issuing_jurisdiction`
- `nationality`
- `resident_city`
- `resident_state`
- `resident_postal_code`
- `resident_country`
- `family_name_national_character`
- `given_name_national_character`
- `signature_usual_mark`
- `biometric_template_face`
- `biometric_template_finger`
- `biometric_template_signature_sign`
- `biometric_template_iris`

## Changelog

Next:
 - Kotlin 2.3.0
 - VC-K 5.11.0
 - Gradle 9.2
 - TestBalloon 0.8.2

Release 1.3.0:
- VC-K 5.8.0
- Kotlin 2.2.0
- kotlinx-datetime 0.7.1
- Gradle 8.14.3

Release 1.2.0:
 - VC-K 5.7.0
 - Remove `serialize()` and `deserialize()` methods in data classes

Release 1.1.5:
- Add additional claims: `age_over_13`, `age_over_25`, `age_over_60`, `age_over_62`, `age_over_65`, `age_over_68`, `biometric_template_face`, `biometric_template_finger`, `biometric_template_signature_sign`, `biometric_template_iris`

Release 1.1.4:
- Add additional claims: `age_over_12`, `age_over_14`, `age_over_16`, `age_over_21`

Release 1.1.3:
- VC-K 5.2.2

Release 1.1.2:
- VC-K 5.2.1
- Kotlin 2.1.0

Release 1.1.1:
- Fix serialization of boolean element `age_over_18`

Release 1.1.0:
- Proper deserialization handling
- Update to VC-K 5.0.0

Release 1.0.3:
- Rename `Initializer.initWithVck` to `Initializer.initWithVCK`

Release 1.0.2:
- Update to VC-K 4.1.0

Release 1.0.1:

- Update to `vclib` 4.0.0

Release 1.0.0:

- Initial release, imported from vclib 3.7.0



<br>

---
<p align="center">
The Apache License does not apply to the logos, (including the A-SIT logo and the VC-K logo) and the project/module name(s)
(even those used only in badges), as these are the sole property of A-SIT/A-SIT Plus GmbH and may not be used
in derivative works without explicit permission! 
</p>

