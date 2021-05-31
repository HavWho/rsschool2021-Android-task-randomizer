package com.rsschool.android2021

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class FirstFragment : Fragment() {

    private var generateButton: Button? = null
    private var previousResult: TextView? = null

    private var min : Int? = 0
    private var max : Int? = 0

    private var minValueEditText : EditText? = null
    private var maxValueEditText : EditText? = null

    private var secondFragmentStarter: SecondFragmentStarter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SecondFragmentStarter) {
            secondFragmentStarter = context
        } else {
            throw RuntimeException("$context must implement SecondFragmentStarter")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        previousResult = view.findViewById(R.id.previous_result)
        generateButton = view.findViewById(R.id.generate)
        minValueEditText = view.findViewById(R.id.min_value)
        maxValueEditText = view.findViewById(R.id.max_value)

        val result = arguments?.getInt(PREVIOUS_RESULT_KEY)

        previousResult?.text = "Previous result: ${result.toString()}"

        generateButton?.setOnClickListener {
            val minValueString = minValueEditText?.text.toString()
            val maxValueString = maxValueEditText?.text.toString()

            if (minValueString.isBlank()) {
                Toast.makeText(this.context, "Minimal value must be specified!", Toast.LENGTH_SHORT).show()
                minValueEditText?.requestFocus()
                return@setOnClickListener
            }

            if (maxValueString.isBlank()) {
                Toast.makeText(this.context, "Maximum value must be specified!", Toast.LENGTH_SHORT).show()
                maxValueEditText?.requestFocus()
                return@setOnClickListener
            }

            min = minValueString.toIntOrNull() ?: -1
            max = maxValueString.toIntOrNull() ?: -1

            if (min!! < 0 || min!! > Int.MAX_VALUE) {
                Toast.makeText(this.context, "Min value must be between ${1} and ${Int.MAX_VALUE}", Toast.LENGTH_SHORT).show()
                minValueEditText?.requestFocus()
                return@setOnClickListener
            }

            if (max!! < 0 || max!! > Int.MAX_VALUE) {
                Toast.makeText(this.context, "Max value must be between ${1} and ${Int.MAX_VALUE}", Toast.LENGTH_SHORT).show()
                maxValueEditText?.requestFocus()
                return@setOnClickListener
            }

            if (min!! > max!!) {
                Toast.makeText(this.context, "Min value must not exceed the max value", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            secondFragmentStarter?.startSecondFragment(min!!, max!!)

        }
    }

    companion object {

        @JvmStatic
        fun newInstance(previousResult: Int): FirstFragment {
            val args = Bundle().apply {
                putInt(PREVIOUS_RESULT_KEY, previousResult)
            }
            val fragment = FirstFragment()
            fragment.arguments = args
            return fragment
        }

        private const val PREVIOUS_RESULT_KEY = "PREVIOUS_RESULT"
    }
}