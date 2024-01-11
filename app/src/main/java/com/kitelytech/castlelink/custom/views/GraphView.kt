package com.kitelytech.castlelink.custom.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import com.kitelytech.castlelink.R
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@SuppressLint("ViewConstructor")
@RequiresApi(Build.VERSION_CODES.M)
class GraphView(context: Context, curveView: CurveView, curveType: CurveType, curvePointActionType: CurvePointActionType, isClearClicked: Boolean): View(context) {

    private var curvePointActionType : CurvePointActionType = CurvePointActionType.ADJUST
    private var curveType : CurveType = CurveType.LINEAR
    private var isClearClicked : Boolean = false
    private var mCurveView: CurveView
    private lateinit var mCanvas : Canvas
    private lateinit var adjustStartPoint : DataPoint
    private var movedX : Float = 0f
    private var movedY : Float = 0f

    private val dotFillPaint = Paint().apply {
        color = context.getColor(R.color.colorMain)
    }

    private val topBackgroundPoint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.colorGraphTopBackground)
        style = Paint.Style.FILL
    }

    private val bottomBackgroundPoint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.colorGraphBottomBackground)
        style = Paint.Style.FILL
    }

    private val axisLinePaint = Paint().apply {
        color = context.getColor(R.color.colorAxis)
        strokeWidth = 1f
    }

    init {
        this.curveType = curveType
        this.curvePointActionType = curvePointActionType
        this.isClearClicked = isClearClicked
        this.mCurveView = curveView
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mCanvas = canvas

        initGraphView()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (curvePointActionType) {
            CurvePointActionType.ADD -> {
                if (event?.actionButton == MotionEvent.ACTION_UP || event?.actionMasked == MotionEvent.ACTION_UP) {
                    val pointX = event.x
                    val pointY = event.y
                    addPoint(DataPoint(pointX, pointY))
                }
            }
            CurvePointActionType.ADJUST -> {
                if (event?.actionButton == MotionEvent.ACTION_DOWN || event?.actionMasked == MotionEvent.ACTION_DOWN) {
                    val pointX = event.x
                    val pointY = event.y
                    adjustStartPoint = DataPoint(pointX, pointY)
                    movedX += pointX - adjustStartPoint.xVal
                    movedY += pointY - adjustStartPoint.yVal
                    adjustPoint(adjustStartPoint)
                }
            }
            CurvePointActionType.DELETE -> {
                if (event?.actionButton == MotionEvent.ACTION_UP || event?.actionMasked == MotionEvent.ACTION_UP) {
                    val pointX = event.x
                    val pointY = event.y
                    deletePoint(DataPoint(pointX, pointY))
                }
            }
        }
        invalidate()
        return true
    }

    private fun initGraphView() {
        if (isClearClicked) {
            mCurveView.points.clear()
            isClearClicked = false
        }

        drawRegion()
        drawAxis()
        drawDots()
    }

    private fun drawRegion() {
        val path = Path()
        when (curveType) {
            CurveType.LINEAR -> {
                // Top Side
                path.reset()
                path.moveTo(0f, 0f)
                path.lineTo(0f, height.toFloat())
                mCurveView.points.forEachIndexed { _, point ->
                    path.lineTo(point.xVal, point.yVal)
                }
                path.lineTo(width.toFloat(), 0f)
                path.lineTo(0f, 0f)
                mCanvas.drawPath(path, topBackgroundPoint)

                // Bottom Side
                path.reset()
                path.moveTo(0f, height.toFloat())
                mCurveView.points.forEachIndexed { _, point ->
                    path.lineTo(point.xVal, point.yVal)
                }
                path.lineTo(width.toFloat(), 0f)
                path.lineTo(width.toFloat(), height.toFloat())
                path.lineTo(0f, height.toFloat())
                mCanvas.drawPath(path, bottomBackgroundPoint)
            }

            CurveType.CURVES -> {
                // Top Side
                path.reset()
                path.moveTo(0f, 0f)
                path.cubicTo(0f, 0f, 0f, height.toFloat()/2, 0f, height.toFloat())
                if (mCurveView.points.size != 0) {
                    var previousControlPoint = getCurveControlPoints(DataPoint(0f, height.toFloat()), DataPoint(0f, height.toFloat()), mCurveView.points[0])
                    mCurveView.points.forEachIndexed { index, point ->

                        val beforePoint = if (index == 0) {
                            DataPoint(0f, height.toFloat())
                        } else {
                            mCurveView.points[index - 1]
                        }

                        val nextPoint = if (index == mCurveView.points.size - 1) {
                            DataPoint(width.toFloat(), 0f)
                        } else {
                            mCurveView.points[index + 1]
                        }
                        val controlPoint = getCurveControlPoints(beforePoint, point, nextPoint)

                        path.cubicTo(previousControlPoint.point2.xVal, previousControlPoint.point2.yVal, controlPoint.point1.xVal, controlPoint.point1.yVal, point.xVal, point.yVal)
                        previousControlPoint = controlPoint
                    }

                    val lastPoint = mCurveView.points[mCurveView.points.size - 1]
                    val controlPoint = getCurveControlPoints(lastPoint, DataPoint(width.toFloat(), 0f), DataPoint(width.toFloat(), 0f))
                    path.cubicTo(previousControlPoint.point2.xVal, previousControlPoint.point2.yVal, controlPoint.point1.xVal, controlPoint.point1.yVal, width.toFloat(), 0f)
                } else {
                    path.cubicTo(0f, height.toFloat(), width.toFloat()/2, height.toFloat()/2, width.toFloat(), 0f)
                }
                path.cubicTo(width.toFloat(), 0f, width.toFloat()/2, 0f, 0f, 0f)
                mCanvas.drawPath(path, topBackgroundPoint)

                // Bottom Side
                path.reset()
                path.moveTo(0f, height.toFloat())
                if (mCurveView.points.size != 0) {
                    var previousControlPoint = getCurveControlPoints(DataPoint(0f, height.toFloat()), DataPoint(0f, height.toFloat()), mCurveView.points[0])
                    mCurveView.points.forEachIndexed { index, point ->

                        val beforePoint = if (index == 0) {
                            DataPoint(0f, height.toFloat())
                        } else {
                            mCurveView.points[index - 1]
                        }

                        val nextPoint = if (index == mCurveView.points.size - 1) {
                            DataPoint(width.toFloat(), 0f)
                        } else {
                            mCurveView.points[index + 1]
                        }
                        val controlPoint = getCurveControlPoints(beforePoint, point, nextPoint)

                        path.cubicTo(previousControlPoint.point2.xVal, previousControlPoint.point2.yVal, controlPoint.point1.xVal, controlPoint.point1.yVal, point.xVal, point.yVal)
                        previousControlPoint = controlPoint
                    }

                    val lastPoint = mCurveView.points[mCurveView.points.size - 1]
                    val controlPoint = getCurveControlPoints(lastPoint, DataPoint(width.toFloat(), 0f), DataPoint(width.toFloat(), 0f))
                    path.cubicTo(previousControlPoint.point2.xVal, previousControlPoint.point2.yVal, controlPoint.point1.xVal, controlPoint.point1.yVal, width.toFloat(), 0f)
                } else {
                    path.cubicTo(0f, height.toFloat(), width.toFloat()/2, height.toFloat()/2, width.toFloat(), 0f)
                }

                path.cubicTo(width.toFloat(), 0f, width.toFloat(), height.toFloat()/2, width.toFloat(), height.toFloat())
                path.cubicTo(width.toFloat(), height.toFloat(), width.toFloat()/2, height.toFloat(), 0f, height.toFloat())
                mCanvas.drawPath(path, bottomBackgroundPoint)
            }
        }
    }

    private fun getCurveControlPoints(beforePoint: DataPoint, point : DataPoint, nextPoint : DataPoint) : ControlPoint {
        val distance01 = sqrt((point.xVal - beforePoint.xVal).toDouble().pow(2.0) + (point.yVal - beforePoint.yVal).toDouble().pow(2.0))
        val distance12 = sqrt((nextPoint.xVal - point.xVal).toDouble().pow(2.0) + (nextPoint.yVal - point.yVal).toDouble().pow(2.0))

        val scale01 = (distance01 / (distance01 + distance12)) * 0.333
        val scale12 = (distance12 / (distance01 + distance12)) * 0.333

        val point1 = DataPoint((point.xVal - scale01 * (nextPoint.xVal - beforePoint.xVal)).toFloat(), (point.yVal - scale01 * (nextPoint.yVal - beforePoint.yVal)).toFloat())
        val point2 = DataPoint((point.xVal + scale12 * (nextPoint.xVal - beforePoint.xVal)).toFloat(), (point.yVal + scale12 * (nextPoint.yVal - beforePoint.yVal)).toFloat())

        return ControlPoint(point1, point2)
    }
    private fun drawAxis() {
        val step = width.toFloat() / 10
        for (i in 2..10) {
            val startX = (i-1) * step
            val startY = 0f
            val stopX = (i-1) * step
            val stopY = height.toFloat()
            mCanvas.drawLine(startX, startY, stopX, stopY, axisLinePaint)
            mCanvas.drawLine(startY, startX, stopY, stopX, axisLinePaint)
        }
    }

    private fun drawDots() {
        mCurveView.points.forEachIndexed { _, point ->
            mCanvas.drawCircle(point.xVal, point.yVal, 30f, dotFillPaint)
        }
    }

    private fun addPoint(point: DataPoint) {
        if (!isExisted(point)) {
            mCurveView.points.add(point)
            mCurveView.points.sortBy { it.xVal }

            drawRegion()
            drawAxis()
            drawDots()
        }
    }

    private fun adjustPoint(movedPoint: DataPoint) {
        val startPoint = DataPoint(movedPoint.xVal - movedX, movedPoint.yVal - movedY)
        if (isExisted(startPoint)) {
            var deleteIndex : Int = -1
            mCurveView.points.forEachIndexed { index, dataPoint ->
                if (abs(startPoint.xVal - dataPoint.xVal) < 30f && abs(startPoint.yVal - dataPoint.yVal) < 30f) {
                    deleteIndex = index
                }
            }

            if (deleteIndex != -1) {
                mCurveView.points.removeAt(deleteIndex)
            }

            mCurveView.points.add(movedPoint)
            mCurveView.points.sortBy { it.xVal }

            drawRegion()
            drawAxis()
            drawDots()
        }
    }

    private fun deletePoint(point: DataPoint) {
        if (isExisted(point)) {
            var deleteIndex : Int = -1
            mCurveView.points.forEachIndexed { index, dataPoint ->
                if (abs(point.xVal - dataPoint.xVal) < 30f && abs(point.yVal - dataPoint.yVal) < 30f) {
                    deleteIndex = index
                }
            }

            if (deleteIndex != -1) {
                mCurveView.points.removeAt(deleteIndex)
                mCurveView.points.sortBy { it.xVal }
            }

            drawRegion()
            drawAxis()
            drawDots()
        }
    }

    private fun isExisted(point: DataPoint) : Boolean {
        var isExisted = false
        mCurveView.points.forEachIndexed { _, dataPoint ->
            if (abs(point.xVal - dataPoint.xVal) < 30f && abs(point.yVal - dataPoint.yVal) < 30f) {
                isExisted = true
            }
        }

        return isExisted
    }
}

data class DataPoint(
    val xVal: Float,
    val yVal: Float
)

data class ControlPoint(
    val point1: DataPoint,
    val point2: DataPoint
)
