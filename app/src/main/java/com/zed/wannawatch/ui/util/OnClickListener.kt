package com.zed.wannawatch.ui.util

import com.zed.wannawatch.services.models.Movie

/**
 * Implements an onclick callback function
 * https://www.section.io/engineering-education/handling-recyclerview-clicks-the-right-way/
 */
class OnClickListener<T>(val clickListener: (data: T) -> Unit) {
    fun onClick(data: T) = clickListener(data)
}