package com.concokorea.concoauth.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object {
        @JvmStatic
        fun getPreYear(juminNo2: String): String {
            return if (juminNo2.startsWith("1") || juminNo2.startsWith("2")) {
                "19"
            } else if (juminNo2.startsWith("3") || juminNo2.startsWith("4")) {
                "20"
            } else {
                juminNo2
            }
        }

        @JvmStatic
        fun getConvertSimpleDate1(stringDate: String): String {
            return if (stringDate.length == 8) {
                stringDate.substring(0, 4) + "년" + stringDate.substring(4, 6) + "월" + stringDate.substring(6) + "일"
            } else {
                stringDate
            }
        }
    }
}