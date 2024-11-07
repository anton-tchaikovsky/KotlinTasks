package com.example.kotlintasks.rxJava

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

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
        initEditText()
    }

    private fun initEditText() {
        val inputTextObservable = PublishSubject.create<String>()
        inputTextObservable
            .debounce(3, TimeUnit.SECONDS)
            .subscribe { Log.i("@@@", it) }.also {
            compositeDisposable.add(it)
        }
        view?.findViewById<EditText>(R.id.edit_text)?.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputTextObservable.onNext(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) { }

        })
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
