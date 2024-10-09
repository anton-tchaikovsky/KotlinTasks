package com.example.kotlintasks.shaker_sort

fun List<Int?>?.shakerSort(): List<Int?>? {
    if (this==null)
        return null
    val mList = this.toMutableList()
    var left = 0
    var right = size - 1
    while (left < right) {
        for (i in left until right) {
            if (mList[i + 1]==null)
                continue
            if ((mList[i] == null) || (mList[i]!! > mList[i + 1]!!))
                mList.swap(i, i + 1)
        }
        right--

        for (i in right downTo left + 1) {
            if (mList[i] == null)
             continue
            if ((mList[i - 1] == null) || (mList[i - 1]!! > mList[i]!!))
                mList.swap(i - 1, i)
        }
        left++
    }
    return mList
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

val exampleList = listOf(0, null, 5, 98, 5, null, 32, null, -3, -2, null, 4, null)