package com.example.kotlintasks

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlintasks.duration.DurationCashImpl
import com.example.kotlintasks.duration.DurationDelegate
import com.example.kotlintasks.extension_for_list.getInt
import com.example.kotlintasks.extension_for_list.listAny
import com.example.kotlintasks.shaker_sort.exampleList
import com.example.kotlintasks.shaker_sort.shakerSort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

//Период в миллисекундах, через который определяетя и сохраняется продолжительность работы прогаммы
const val TIMES_PERIOD = 250L

//Интервал в секундах, с которым выводится время работы программы в логи
const val INTERVAL_LOG = 3

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        CoroutineScope(Dispatchers.Default).launch { startTimeCounter(TIMES_PERIOD) }
        findViewById<Button>(R.id.int_button).setOnClickListener {  printLogInt(listAny) }
        findViewById<Button>(R.id.sort_button).setOnClickListener {  printLogSortList(exampleList) }
    }

    private suspend fun startTimeCounter(timePeriodInMilliseconds: Long) {
        var currentTime: Duration by DurationDelegate(DurationCashImpl(), INTERVAL_LOG)
        while (true) {
            delay(timePeriodInMilliseconds)
            currentTime += timePeriodInMilliseconds.milliseconds
        }
    }

    private fun printLogInt(listAny: List<Any>) {
        try {
            Log.i("Int", "found Int ${listAny.getInt()}")
        } catch (e: NoSuchElementException) {
            Log.i("Int", "not found Int")
        }
    }

    private fun printLogSortList(list: List<Int?>?){
        Log.i ("Shaker sort", "$list -> ${list.shakerSort()}")
    }
}