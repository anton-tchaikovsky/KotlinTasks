package com.example.kotlintasks.rxJava

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.kotlintasks.R
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.subjects.PublishSubject

class ElementsAdapter(private val elements: List<String>, private val clickElementObserver: Observer<Int>) :
    RecyclerView.Adapter<ElementsAdapter.ElementViewHolder>() {

    private val subject = PublishSubject.create<Int>().also {
       it.subscribe(clickElementObserver)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ElementViewHolder(inflater.inflate(R.layout.element_item, parent, false) as Button)
    }

    override fun getItemCount(): Int = elements.size

    override fun onBindViewHolder(holder: ElementViewHolder, position: Int) {
        holder.bind(elements[position])
    }

    inner class ElementViewHolder(private val elementView: Button) : ViewHolder(elementView) {

        fun bind(element: String) {
            elementView.text = element
            elementView.setOnClickListener {
               subject.onNext(adapterPosition)
            }
        }
    }
}