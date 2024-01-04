package com.example.feature_search.VM

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.feature_search.data.states.States
import com.example.feature_search.domain.findCharacterUseCase.FindCharacterUseCase
import com.example.feature_search.domain.findLocationUseCase.FindLocationUseCase
import com.example.feature_search.domain.models.FoundCharacter
import com.example.feature_search.domain.models.FoundLocation
import com.example.feature_search.pagingSource.PagingSourceCharacter
import com.example.feature_search.pagingSource.PagingSourceLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FindCharacterVM(
    private val findCharacterUseCase: FindCharacterUseCase,
    private val findLocationUseCase: FindLocationUseCase
) : ViewModel() {

    private var _list = MutableStateFlow<PagingData<FoundCharacter>>(PagingData.empty())
    val list = _list.asStateFlow()

    private var _listLocation = MutableStateFlow<PagingData<FoundLocation>>(PagingData.empty())
    val listLocation = _listLocation.asStateFlow()

    private var _state = MutableStateFlow<States>(States.ShowCharacters)
    val state = _state.asStateFlow()

    fun findCharacter(name: String, status: String) {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(20),
                pagingSourceFactory = {
                    PagingSourceCharacter(
                        useCase = findCharacterUseCase,
                        name = name,
                        status = status
                    )
                }
            ).flow.cachedIn(viewModelScope)
                .collect {
                    Log.d("it", "it=======$it")
                    _list.value = it
                    _state.value = States.ShowCharacters
                    Log.d("it", "it=======$it")
                }
        }
    }

    fun getLocation(name: String) {
        viewModelScope.launch {
            Pager(config = PagingConfig(20), pagingSourceFactory = {
                PagingSourceLocation(findLocationUseCase, name)
            }).flow.cachedIn(viewModelScope).collect {
                _listLocation.value = it
                _state.value = States.ShowLocations
            }
        }
    }
}