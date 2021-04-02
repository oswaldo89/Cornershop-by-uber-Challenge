package com.cornershop.counterstest.utils.sealed_classes

sealed class MainListUiState{
    object Initial : MainListUiState()
    object Loading : MainListUiState()
    object NoContent : MainListUiState()
    object HasContent : MainListUiState()
    object Refreshing : MainListUiState()
    data class Error(val message : String ): MainListUiState()
}