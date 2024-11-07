package com.example.kotlintasks.rxJava.network_request.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlintasks.R
import com.example.kotlintasks.rxJava.network_request.data.data_source.RemoteDataSourceImpl
import com.example.kotlintasks.rxJava.network_request.data.repository.CatFactRepositoryImpl


class CatFactFragment : Fragment() {

    private val viewModel: CatFactViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelProviderFactoryOf { CatFactViewModel(CatFactRepositoryImpl(RemoteDataSourceImpl())) })[CatFactViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_cat_fact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeViewModel()
        if (savedInstanceState == null)
            viewModel.getCatFact()
    }

    private fun subscribeViewModel() {
        viewModel.liveData.observe(viewLifecycleOwner) {
            renderData(it)
        }
    }

    private fun renderData(viewState: CatFactViewState) {
        when (viewState) {
            is CatFactViewState.Error -> Toast.makeText(
                requireContext(),
                viewState.message,
                Toast.LENGTH_SHORT
            ).show()

            CatFactViewState.Loading -> Toast.makeText(
                requireContext(),
                "loading",
                Toast.LENGTH_SHORT
            ).show()

            is CatFactViewState.Success -> view?.findViewById<TextView>(R.id.fact_text_view)?.text =
                viewState.fact
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CatFactFragment()
    }
}