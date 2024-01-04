package com.example.feature_search.data.states

sealed class States {
    data object ShowCharacters : States()
    data object ShowLocations : States()
}