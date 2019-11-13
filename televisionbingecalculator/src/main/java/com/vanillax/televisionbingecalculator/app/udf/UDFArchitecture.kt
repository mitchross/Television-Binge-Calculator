package com.vanillax.televisionbingecalculator.app.udf

import androidx.lifecycle.MutableLiveData

class UIEvent<T> : MutableLiveData<T>() {

    private var consumed = false

    override fun getValue(): T? {

        if (consumed) {
            return null
        }

        consumed = true
        return super.getValue()
    }

    override fun setValue(value: T) {
        consumed = false
        super.setValue(value)
    }
}