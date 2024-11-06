package com.example.kotlintasks.rxJava.network_request.presentation

sealed interface CatFactViewState {
    class Success(val fact: String): CatFactViewState
    class Error(val message: String?):  CatFactViewState
    data object Loading: CatFactViewState
}