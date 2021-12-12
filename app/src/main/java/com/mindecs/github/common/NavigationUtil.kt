package com.mindecs.github.common

import android.content.Context
import android.content.Intent
import android.net.Uri

object NavigationUtil {

    fun navigateToBrowser(context: Context, url: String) {
        if (url.startsWith(Constants.HTTP) || url.startsWith(Constants.HTTPS)) {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
        }
    }
}