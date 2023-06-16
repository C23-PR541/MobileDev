package com.bangkit.gymguru.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskView(
    var date: String? = null,
    var tow: String? = null,
    var time_start : String? = null,
    var time_end: String? = null,
    var notes: String? = null,
    var taskId: String? = null,
)  : Parcelable
