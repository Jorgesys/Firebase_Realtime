package com.jorgesys.FirebaseRealtime

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserInfo(
    var name: String? = "",
    var mobile: String? = ""
)