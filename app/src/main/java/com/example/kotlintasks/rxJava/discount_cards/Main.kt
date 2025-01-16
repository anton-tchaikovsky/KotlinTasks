package com.example.kotlintasks.rxJava.discount_cards

fun main(){
    val repository: CardsRepository = CardsRepositoryImpl()

   val disposable = repository.getCardsIgnoreOneError()
       .subscribe(
           { cards ->
               println(cards.toString())
           },
           {error ->
               println(error.message)
           }
       )
}