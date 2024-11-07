package com.example.kotlintasks.rxJava.discount_cards

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.subjects.SingleSubject
import java.io.IOException
import kotlin.math.absoluteValue
import kotlin.random.Random

class CardsRepositoryImpl : CardsRepository {

    private val firstCardsSingle = Single.create<List<Card>> { emitter ->
        emitter.onError(IOException("Server error"))
        emitter.onSuccess(List(5) {
            Card(
                "First ${it + 1}",
                (it + 1L) * Random.nextInt().absoluteValue
            )
        })
    }

    private val secondCardsSingle = Single.create<List<Card>> { emitter ->
        //emitter.onError(IOException("Server error"))
        emitter.onSuccess(List(5) {
            Card(
                "Second ${it + 1}",
                (it + 1L) * Random.nextInt().absoluteValue
            )
        })
    }

    // Ошибка в любом из серверов приведет к ошибке запроса из репозитория
    override fun getCards(): Single<List<Card>> =
        firstCardsSingle
            .map { it.toMutableList() }
            .zipWith(secondCardsSingle) { first, second ->
                first.addAll(second)
                return@zipWith first
            }
            .map { it.toList() }

    // Ошибка в любом из серверов игнорируется. При одновременных ошибках в обоих серверах
    // возвращается пустой список карт.
    override fun getCardsIgnoreErrors(): Single<List<Card>> =
        firstCardsSingle
            .onErrorReturn { listOf() }
            .map { it.toMutableList() }
            .zipWith(secondCardsSingle.onErrorReturn { listOf() }) { first, second ->
                first.addAll(second)
                return@zipWith first
            }

    // Ошибка в любом из серверов игнорируется. Одновременная ошибка в обоих серверах
    // приводит к ошибке запроса из репозитория
    override fun getCardsIgnoreOneError(): Single<List<Card>> {
        var countError = 0
        val single = SingleSubject.create<List<Card>>()
        val d = firstCardsSingle
            .onErrorReturn {
                ++countError
                return@onErrorReturn listOf()
            }
            .map { it.toMutableList() }
            .zipWith(secondCardsSingle.onErrorReturn {
                if (++countError > 1)
                    throw it
                listOf()
            }
            ) { first, second ->
                first.addAll(second)
                return@zipWith first
            }.subscribe(
                { cards ->
                    single.onSuccess(cards)
                },
                { error ->
                    if (error is CompositeException)
                        single.onError(error.exceptions.first())
                    else
                        single.onError(error)
                }
            )

        return single.doOnDispose {d.dispose()}
    }
}