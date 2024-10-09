package com.example.kotlintasks.extension_for_list

fun List<Any>.getInt(): Int = this.first { it is Int } as Int


val listAny: List<Any> =
    listOf(A(), B(), 5L, 35F, B(), "Hello", C(), People("Tom", 56), 45)

class A

class B

class C

class People(name: String, age: Int)
