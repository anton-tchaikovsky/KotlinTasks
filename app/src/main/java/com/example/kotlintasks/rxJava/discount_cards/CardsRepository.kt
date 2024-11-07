package com.example.kotlintasks.rxJava.discount_cards

import io.reactivex.rxjava3.core.Single

interface CardsRepository {
    fun getCards(): Single<List<Card>>

    fun getCardsIgnoreErrors(): Single<List<Card>>

    fun getCardsIgnoreOneError(): Single<List<Card>>
}