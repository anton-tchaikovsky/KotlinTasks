package com.example.kotlintasks.navigation_router.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlintasks.databinding.FragmentSimpleBinding

private const val KEY = "key"

class SimpleFragment : Fragment() {
    private lateinit var keyFragment: String

    private lateinit var navigationRequestListener: NavigationRequestListener

    private var _binding: FragmentSimpleBinding? = null
    private val binding: FragmentSimpleBinding
        get() = _binding!!

    override fun onAttach(context: Context) {
        navigationRequestListener = context as NavigationRequestListener
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(KEY)?.let {
            keyFragment = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            nameTextView.text = keyFragment
            nextButton.setOnClickListener { navigationRequestListener.onNext() }
            backButton.setOnClickListener { navigationRequestListener.onBack() }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            SimpleFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY, param1)
                }
            }
    }
}