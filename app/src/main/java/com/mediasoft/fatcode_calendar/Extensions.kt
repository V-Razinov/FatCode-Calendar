package com.mediasoft.fatcode_calendar

import android.content.Context
import android.util.TypedValue

fun Number.toDp(context: Context): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    context.resources.displayMetrics
)

val String.capitalized
    get() = replaceFirstChar { it.uppercase() }