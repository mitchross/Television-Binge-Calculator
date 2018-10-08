package com.vanillax.televisionbingecalculator.app.kotlin.enum

enum class SearchType private constructor(private val searchType: String) {
    TV("tv"),
    MOVIE("movie");


    override fun toString(): String {
        return this.searchType
    }
}