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