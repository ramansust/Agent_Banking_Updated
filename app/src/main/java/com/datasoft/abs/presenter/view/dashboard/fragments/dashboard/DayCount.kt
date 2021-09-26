package com.datasoft.abs.presenter.view.dashboard.fragments.dashboard

import com.datasoft.abs.presenter.view.dashboard.fragments.dashboard.DayCount.*

enum class DayCount(val value: String) {
    TODAY("1"),
    TOMORROW("2"),
    LAST_WEEK("7"),
    LAST_MONTH("30"),
    QUARTER("90"),
}

fun getAllDays(): List<DayCount> {
    return listOf(TODAY, TOMORROW, LAST_WEEK, LAST_MONTH, QUARTER)
}

fun getDay(value: String): DayCount? {
    val map = DayCount.values().associateBy(DayCount::value)
    return map[value]
}