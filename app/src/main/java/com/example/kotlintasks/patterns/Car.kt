package com.example.kotlintasks.patterns

interface Transport {
    interface Factory {
        fun create(): Transport
    }

    fun run()
}

class Car private constructor(
    val model: String,
    val country: String,
    val year: Int,
    val color: String
) : Transport {
    class Builder(
        private var model: String = "undefined",
        private var country: String = "undefined",
        private var year: Int = 0,
        private var color: String = "undefined"
    ) {
        fun model(model: String) = apply { this.model = model }
        fun country(country: String) = apply { this.country = country }
        fun year(year: Int) = apply { this.year = year }
        fun color(color: String) = apply { this.color = color }
        fun build() = Car(model, country, year, color)
    }

    class Factory(
        private val model: String,
        private val country: String,
        private val year: Int,
        private val color: String

    ) : Transport.Factory {
        override fun create(): Transport =
            Car(model, country, year, color)
    }

    override fun run() {
        println("run")
    }
}

fun main() {
    val carA = Car.Builder()
        .model("Opel")
        .country("Spain")
        .year(2012)
        .color("white")
        .build()
    val carB = Car.Factory(
        "Opel",
        "Spain", 2012, "white"
    ).create()
}