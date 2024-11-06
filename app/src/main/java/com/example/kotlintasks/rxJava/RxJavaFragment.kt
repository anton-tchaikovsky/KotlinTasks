package com.example.kotlintasks.rxJava

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kotlintasks.R
import com.example.kotlintasks.rxJava.timer.Timer
import com.example.kotlintasks.rxJava.timer.TimerImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

class RxJavaFragment : Fragment() {

    private val timer: Timer = TimerImpl()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rx_java, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTimerView()
    }

    private fun initTimerView() {
        val timerView = view?.findViewById<TextView>(R.id.timer_text_view)
        timer.getTimer()
            .map {it.formatToString()}
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { timerView?.text = it }.also {
                compositeDisposable.add(it)
            }
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    companion object {

        @JvmStatic
        fun newInstance() = RxJavaFragment()
    }
}
