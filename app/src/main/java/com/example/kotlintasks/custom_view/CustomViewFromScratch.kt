package com.example.kotlintasks.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.kotlintasks.R
import kotlinx.parcelize.Parcelize
import kotlin.properties.Delegates
import kotlin.random.Random

class CustomViewFromScratch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var percent = 0

    private var customViewWith: Float by Delegates.notNull()

    private var customViewHeight: Float by Delegates.notNull()

    private val firstFractionViewPaint = Paint()

    private val secondFractionViewPaint = Paint()

    private var isRandomColor: Boolean by Delegates.notNull()

    private val firstFractionViewWith: Float
        get() = customViewWith * percent / 100

    private var firstFractionViewColor: Int by Delegates.notNull()

    init {
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
        customViewWith = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        customViewHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        Log.d("@@@", "OnMeasure")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("@@@", "onDraw")
        canvas.drawRect(
            0F,
            0F,
            firstFractionViewWith,
            customViewHeight,
            firstFractionViewPaint
        )
        canvas.drawRect(
            firstFractionViewWith,
            0F,
            customViewWith,
            customViewHeight,
            secondFractionViewPaint
        )
        super.onDraw(canvas)
    }

    private fun initAttr(
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val typeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomViewFromScratch,
            defStyleAttr,
            defStyleRes
        )

        firstFractionViewPaint.color = typeArray.getColor(
            R.styleable.CustomView_colorFirstFraction, Color.RED
        ).also { firstFractionViewColor = it }

        secondFractionViewPaint.color = typeArray.getColor(
            R.styleable.CustomView_colorSecondFraction, Color.GRAY
        )

        isRandomColor = typeArray.getBoolean(R.styleable.CustomViewFromScratch_isRandomColor, false)
        typeArray.recycle()
    }

    private fun setOnClickListener() {
        this.setOnClickListener {
            if (percent != 100)
                percent += 10
            else percent = 0
            if (isRandomColor)
                firstFractionViewPaint.color =
                    Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
                        .also { firstFractionViewColor = it }
            invalidate()
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