package dev.rudak.androidcheatsheet.core.common.result

suspend inline fun <T> runSuspendCatching(
    crossinline block: suspend () -> T,
): Result<T> {
    return runCatching {
        block()
    }
}