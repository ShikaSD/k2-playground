package test

import androidx.compose.runtime.Composable

@Composable fun Test(content: @Composable (() -> Unit)? = null) {
    content?.invoke()
}