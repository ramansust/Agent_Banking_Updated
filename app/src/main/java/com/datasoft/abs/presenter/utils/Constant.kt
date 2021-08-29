package com.datasoft.abs.presenter.utils

object Constant {
    const val ConnectTimeout: Long = 40
    const val ReadTimeout: Long = 60
    const val WriteTimeout: Long = 60
    const val SPLASH_DELAY = 3000
    const val DATABASE_NAME = "AgentBanking"
    const val PER_PAGE_ITEM = 100
    const val USER_NAME = "USER_NAME"
    const val ADDRESS_INFO = "ADDRESS_INFO"
    const val NOMINEE_INFO = "NOMINEE_INFO"
    const val DOCUMENT_INFO = "DOCUMENT_INFO"
    const val SIGNATURE_INFO = "SIGNATURE_INFO"
    const val ADULT_AGE = 18
    const val DATE_FORMAT = "dd-MM-yyyy"
    const val DATE_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss"
    const val BALANCE_FORMAT = "##,##,##,###.###"

    const val IMAGE_RESOLUTION_WIDTH = 1080
    const val IMAGE_RESOLUTION_HEIGHT = 1080
    const val IMAGE_COMPRESS = 1024
    const val MAX_SHARE = 100
    const val SHARE_PERCENT_INFO = "SHARE_PERCENT_INFO"
    const val PASSWORD_REGEX =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{8,}"

    const val NO_INTERNET = "NO_INTERNET"
    const val SOMETHING_WRONG = "SOMETHING_WRONG"
    const val FIELD_EMPTY = "FIELD_EMPTY"
    const val SEARCH_EMPTY = "SEARCH_EMPTY"
    const val ID_EMPTY = "ID_EMPTY"
}