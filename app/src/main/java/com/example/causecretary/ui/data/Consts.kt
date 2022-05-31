package com.example.causecretary.ui.data

class Consts {

    companion object {

        // common
        const val NUM_ZERO = "0"
        const val TEXT_Y = "y"
        const val TEXT_N = "n"
        const val TRUE = "true"
        const val FALSE = "false"

        // password
        const val PASSWORD_MIN_LENGTH=8
        const val PASSWORD_MAX_LENGTH=16

        //kyc_validation
        //임시 영문이름logic
        const val engNameValidation = "^[a-zA-Z]{5,}$"
        //phone number validation
        const val workPhoneValidation = "^[0][2-6][1-5]?[-][2-9]{2,3}[-][0-9]{4,}$"

        // 가로 길이가 500px 미만이면 에러 발생
        const val ID_CARD_MIN_WIDTH = 600
    }

    enum class TermsType() {
        TERMS_USE,
        TERMS_PERSONAL,
        TERMS_UNIQUE
    }

    enum class Gender(val gender: String){
        MALE("male"), FEMALE("female")
    }

    enum class Domestic(val residence: String) {
        DOMESTIC("domestic"), FOREIGN("foreign")
    }

    // 5-1 (외국인등록증), 5-2 (외국국적동포 국내거소신고증), 5-3 (영주증)
    enum class IdCardType(val type: String) {
        IDCARD("1"), DRIVER("2"), ALIEN("5-1"), FOREIGNKOREAN("5-2"), PERMANENTRESIDENT("5-3")
    }

    class WifiStrength {
        companion object {
            const val EXCELLENT: String = "Excellent"
            const val GOOD: String = "Good"
            const val FAIR: String = "Fair"
            const val WEAK: String = "Weak"
            const val NONE: String = "None"
        }
    }
}