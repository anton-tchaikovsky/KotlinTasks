package com.example.kotlintasks.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.kotlintasks.R
import com.example.kotlintasks.databinding.CustomViewBinding
import kotlinx.parcelize.Parcelize
import kotlin.properties.Delegates
import kotlin.random.Random

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: CustomViewBinding

    private var percent = 0

    private var customViewWith: Int by Delegates.notNull()

    private var isRandomColor: Boolean by Delegates.notNull()

    private val firstFractionViewWith: Int
        get() = customViewWith * percent / 100

    private var firstFractionViewColor: Int by Delegates.notNull()

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.custom_view, this, true)
        binding = CustomViewBinding.bind(this)
        initAttr(attrs, defStyleAttr, defStyleRes)
        setOnClickListener()
    }

    override fun onSaveInstanceState(): Parcelable {
        return SaveState(super.onSaveInstanceState(), percent, firstFractionViewColor)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val saveState = state as? SaveState
        super.onRestoreInstanceState(saveState?.superSavedState ?: state)
        saveState?.let {
            percent = it.percent
            firstFractionViewColor = it.firstFractionViewColor
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        customViewWith = MeasureSpec.getSize(widthMeasureSpec)
        Log.d("@@@", "OnMeasure")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Log.d("@@@", "onLayout")
        val firstFractionView = getChildAt(0)
        val secondFractionView = getChildAt(1)
        val firstFractionViewHeight = firstFractionView.measuredHeight
        firstFractionView.layout(0, 0, firstFractionViewWith, firstFractionViewHeight)
        if (isRandomColor)
            firstFractionView.setBackgroundColor(firstFractionViewColor)
        secondFractionView.layout(firstFractionViewWith, 0, customViewWith, firstFractionViewHeight)
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("@@@", "onDraw")
        super.onDraw(canvas)
    }

    private fun initAttr(
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val typeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomView,
            defStyleAttr,
            defStyleRes
        )
        with(binding) {
            firstFractionView.setBackgroundColor(
                typeArray.getColor(
                    R.styleable.CustomView_colorFirstFraction, Color.RED
                ).also { firstFractionViewColor = it }
            )

            secondFractionView.setBackgroundColor(
                typeArray.getColor(
                    R.styleable.CustomView_colorSecondFraction, Color.GRAY
                )
            )
        }
        isRandomColor = typeArray.getBoolean(R.styleable.CustomView_isRandomColor, false)
        typeArray.recycle()
    }

    private fun setOnClickListener() {
        this.setOnClickListener {
            if (percent != 100)
                percent += 10
            else percent = 0
            firstFractionViewColor =
                Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            requestLayout()
        }
    }

    @Parcelize
    class SaveState(
        val superSavedState: Parcelable?,
        val percent: Int,
        val firstFractionViewColor: Int
    ) :
        BaseSavedState(superSavedState), Parcelable
}