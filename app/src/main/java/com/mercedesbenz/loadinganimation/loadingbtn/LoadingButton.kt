package com.mercedesbenz.loadinganimation.loadingbtn

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.mercedesbenz.loadinganimation.R
import timber.log.Timber
import kotlin.math.cos
import kotlin.math.sin
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonText = ""
    private var progress = 0f
    private lateinit var rectF: RectF
    private var progressRadius = resources.getDimension(R.dimen.progressRadius)
    private var progressRadiusPaddingRight = resources.getDimension(R.dimen.paddingRightProgressRadius)
    private var urlIsSelected = false
    private var bgColor = 0
    private var bgFilledColor = 0
    private var textColor = 0

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->


    }

    private val vA = ValueAnimator.ofFloat(0f, 100f)

    private val paint = Paint().apply {
        // Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.defaultTextSize)
        textAlign = Paint.Align.CENTER
    }


    init {
        buttonState = ButtonState.ReadyToDownload
        isClickable = true
        buttonText = resources.getString(R.string.button_download)
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            bgColor = getColor(R.styleable.LoadingButton_backgroundColor, Color.BLACK)
            bgFilledColor = getColor(R.styleable.LoadingButton_backgroundFilledColor, Color.BLACK)
            textColor = getColor(R.styleable.LoadingButton_textColor, Color.WHITE)
        }
    }

    override fun performClick(): Boolean {
        Timber.i("btn clicked")
        super.performClick()
        if (!urlIsSelected) return true
        if (buttonState == ButtonState.ReadyToDownload) {
            buttonState = ButtonState.Loading
            isClickable = false
            buttonText = resources.getString(R.string.button_loading)
            animateProgressColor()
        }


        return true
    }

    fun setUrlIsSelected(isSelected: Boolean) {
        urlIsSelected = isSelected
    }

    fun setDownloadFinished(isFinished: Boolean) {
        if (isFinished) {
            buttonState = ButtonState.Completed
            invalidate()
        }
    }

    fun setIsReadyToDownload(isReady: Boolean) {
        if (isReady) {
            buttonState = ButtonState.ReadyToDownload
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(resources.getColor(R.color.colorPrimary))
        when (buttonState) {
            ButtonState.ReadyToDownload -> {
                isClickable = true
                buttonText = resources.getString(R.string.button_download)
                invalidate()
            }

            ButtonState.Loading -> {
                canvas.drawFillProgress()
                canvas.drawCircleProgress()
            }

            ButtonState.Completed -> {
                vA.cancel()
                progress = 1.0f
                isClickable = false
                buttonText = resources.getString(R.string.button_complete)
                rectF = RectF(0f, 0f, progress * widthSize, heightSize.toFloat())
                canvas.drawFillProgress()
                canvas.drawCircleProgress()
            }
        }
        canvas.save()
        paint.color = textColor
        canvas.translate((widthSize / 2).toFloat(), (heightSize / 2 + paint.textSize / 2))
        canvas.drawText(buttonText, 0f, 0f, paint)
        canvas.restore()

        super.onDraw(canvas)

    }

    private fun Canvas.drawCircleProgress() {
        save()
        translate((widthSize - progressRadiusPaddingRight - progressRadius), (heightSize / 2).toFloat())
        val step = 100

        for (i in 0..(progress * step).toInt()) {
            val angle = (Math.PI * i / step) * 2
            val xProgressCircle = (progressRadius * cos(angle)).toFloat()
            val yProgressCircle = (progressRadius * sin(angle)).toFloat()
            drawLine(0f, 0f, xProgressCircle, yProgressCircle, paint)
        }
        restore()
    }

    private fun Canvas.drawFillProgress() {
        save()
        paint.color = bgFilledColor
        paint.style = Paint.Style.FILL

        drawRect(rectF, paint)
        paint.color = resources.getColor(R.color.colorAccent)
        restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        Timber.i("width: $widthSize, height: $heightSize")
        setMeasuredDimension(w, h)
    }

    private fun animateProgressColor() {
        Timber.i("animate loading")
        vA.duration = 3_000
        vA.repeatCount = ValueAnimator.INFINITE

        vA.addUpdateListener { p ->
            progress = (p.animatedValue as Float) / 100
            rectF = RectF(0f, 0f, progress * widthSize, heightSize.toFloat())
            invalidate()
        }

        vA.start()

    }

}