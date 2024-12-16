package com.example.cs499_app

interface DateChecker {
    fun checkDateExists(
        date: Long,
        onDuplicateFound: () -> Unit,
        onDateAvailable: () -> Unit,
        onError: (String) -> Unit
    )
}