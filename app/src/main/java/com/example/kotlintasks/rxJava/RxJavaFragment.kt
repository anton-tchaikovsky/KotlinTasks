package com.example.kotlintasks.rxJava

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintasks.R
import com.example.kotlintasks.rxJava.timer.Timer
import com.example.kotlintasks.rxJava.timer.TimerImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class RxJavaFragment : Fragment() {

    private val timer: Timer = TimerImpl()

    private val compositeDisposable = CompositeDisposable()

    private val clickElementObserver by lazy {
        object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }

            override fun onComplete() {
                Toast.makeText(requireContext(), "onComplete", Toast.LENGTH_SHORT).show()
            }

            override fun onNext(t: Int) {
                Toast.makeText(requireContext(), "Element ${t + 1}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private val elementsAdapter by lazy {
        ElementsAdapter(List(5) { "Element ${it + 1}" }, clickElementObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rx_java, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTimerView()
        initElementsRecycleView()
    }

    private fun initElementsRecycleView() {
        view?.let {
            it.findViewById<RecyclerView>(R.id.elements_recycler_view).apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = elementsAdapter
            }
        }
    }

    private fun initTimerView() {
        val timerView = view?.findViewById<TextView>(R.id.timer_text_view)
        timer.getTimer()
            .map { it.formatToString() }
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
