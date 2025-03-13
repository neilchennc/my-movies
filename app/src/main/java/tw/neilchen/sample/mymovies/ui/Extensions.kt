package tw.neilchen.sample.mymovies.ui

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

/**
 * Find a [androidx.activity.ComponentActivity] from the current context.
 * By default Jetpack Compose project uses [ComponentActivity] for MainActivity.
 *
 * Reference: https://stackoverflow.com/a/75643188
 */
fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
