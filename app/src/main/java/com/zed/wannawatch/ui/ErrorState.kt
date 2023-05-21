package com.zed.wannawatch.ui

sealed class ErrorState {
    object NoError: ErrorState()
    data class Error(val msg: String): ErrorState()
}
