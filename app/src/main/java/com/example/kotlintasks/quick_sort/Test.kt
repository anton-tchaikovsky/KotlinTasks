package com.example.kotlintasks.quick_sort

fun main() {

//    val array = intArrayOf(3, 1, 4, 8, 3, 9, 4, 1, 8, 5, 3, 0, 3, 6, 9, 7, 3, 1, 5, 7)
//    QuickSort.quickSort(array)
//    println("Отсортированный массив " + array.contentToString())

//    runBlocking {
//        val workerThread = WorkerThread(false)
//        workerThread.start()
//
//        repeat(10){
//            if(it==5)
//                Thread.sleep(3000)
//
//            workerThread.putTask {
//                println("task ${it+1} start")
//                Thread.sleep(Random.nextLong(100, 300))
//                println("task ${it+1} stop")
//            }
//        }
//
//        workerThread.stopThread()
//    }

    val huffman = Huffman("Привет")
    println("Закодированный текст " + huffman.getEncodedText())
    huffman.printDecodedText()
}