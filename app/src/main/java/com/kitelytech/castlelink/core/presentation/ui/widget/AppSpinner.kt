package com.kitelytech.castlelink.core.presentation.ui.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.kitelytech.castlelink.R

class AppSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val radius: Float
    private var centerX: Float = 0f
    private var centerY: Float = 0f

    private var startAngle: Float = 0f
    private var sweepAngle: Float = 0f
    private var arcPercentLength: Float = 0.25f
    private var ovalRect: RectF = RectF()

    private var mStrokeWidth: Float

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppSpinner)
        try {
            mStrokeWidth = typedArray.getDimension(
                R.styleable.AppSpinner_as_stroke_width,
                resources.getDimension(R.dimen.spinner_background_stroke_large)
            )
            radius = typedArray.getDimension(
                R.styleable.AppSpinner_as_spinner_radius,
                resources.getDimension(R.dimen.spinner_default_radius)
            )
        } finally {
            typedArray.recycle()
        }
    }

    private val halfStrokeWidth = mStrokeWidth / 2f

    private var startGradientColor = ContextCompat.getColor(context, R.color.main_start_gradient)
    private var endGradientColor = ContextCompat.getColor(context, R.color.main_end_gradient)

    private val colors = intArrayOf(startGradientColor, endGradientColor)
    private val positions = floatArrayOf(0.0f, 1f)

    private var gradient2:LinearGradient? = null

    private val backgroundCirclePaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.main)
        alpha = (255 * 0.25).toInt()
        style = Paint.Style.STROKE
        strokeWidth = mStrokeWidth
    }

    private val foregroundArcPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = mStrokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    private val valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f).apply {
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        duration = 2000L
        addUpdateListener {
            updateState(it.animatedFraction)
        }
    }

    private fun updateState(fraction: Float) {
        startAngle = fraction * 360
        sweepAngle = (360 * arcPercentLength)
        Log.d("ASASDASD","startAngle = $startAngle sweepAngle = $sweepAngle ")
        invalidate()
    }

    fun showAndStart() {
        visibility = VISIBLE
        valueAnimator.start()
    }

    fun hideAndStop() {
        visibility = GONE
        valueAnimator.cancel()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        centerX = measuredWidth / 2f
        centerY = measuredHeight / 2f

        ovalRect.left = centerX - radius
        ovalRect.top = centerY - radius
        ovalRect.right = centerX + radius
        ovalRect.bottom = centerY + radius

        gradient2 = LinearGradient(
            ovalRect.left, ovalRect.top, ovalRect.right, ovalRect.bottom,
            colors, positions, Shader.TileMode.CLAMP
        )
        foregroundArcPaint.shader = gradient2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        canvas.drawCircle(centerX, centerY, radius, backgroundCirclePaint)
        canvas.drawArc(ovalRect, startAngle, sweepAngle, false, foregroundArcPaint)
    }
}