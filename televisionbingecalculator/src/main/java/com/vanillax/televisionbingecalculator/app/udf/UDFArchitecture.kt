package com.vanillax.televisionbingecalculator.app.udf

class UIEvent<T> {

    private var consumed = false

    var value: T? = null
        set(value) {
            consumed = false
            field = value
        }
        get() {

            if (consumed) {
                return null
            }

            consumed = true
            return field
        }

    companion object {

        fun <T> create(t: T): UIEvent<T> {
            return UIEvent<T>().apply {
                value = t
            }
        }
    }
}

sealed class Resource<T>(open var data: T?) {
    data class Loading<T>(override var data: T?) : Resource<T>(data)
    data class Failure<T>(override var data: T?, val error: Throwable) : Resource<T>(data)
    data class Success<T>(override var data: T?) : Resource<T>(data)
    data class Stale<T>(override var data: T?) : Resource<T>(data)
}