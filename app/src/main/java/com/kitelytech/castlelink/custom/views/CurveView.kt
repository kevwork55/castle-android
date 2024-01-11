package com.kitelytech.castlelink.custom.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.custom.models.ComponentSetting
import kotlinx.android.synthetic.main.layout_curve.view.*

@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("ViewConstructor", "UseCompatLoadingForDrawables", "CutPasteId")
class CurveView constructor(
    context: Context,
    component: ComponentSetting
) : RelativeLayout(context) {
    var isExpanded : Boolean = false
    var curvePointActionType : CurvePointActionType = CurvePointActionType.ADJUST
    var curveType : CurveType = CurveType.LINEAR
    var points : MutableList<DataPoint> = mutableListOf()

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_curve, this, true)

        tvTitle.text = component.name
        optionView.visibility = GONE

        btnCurveEdit.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                optionView.visibility = VISIBLE
            } else {
                optionView.visibility = GONE
            }
        }

        changeCurvePointActionType()
        changeCurveType()

        btnAdd.setOnClickListener {
            curvePointActionType = CurvePointActionType.ADD
            changeCurvePointActionType()
        }

        btnAdjust.setOnClickListener {
            curvePointActionType = CurvePointActionType.ADJUST
            changeCurvePointActionType()
        }

        btnDelete.setOnClickListener {
            curvePointActionType = CurvePointActionType.DELETE
            changeCurvePointActionType()
        }

        btnLinear.setOnClickListener {
            curveType = CurveType.LINEAR
            changeCurveType()
        }

        btnCurves.setOnClickListener {
            curveType = CurveType.CURVES
            changeCurveType()
        }

        btnClear.setOnClickListener {
            refreshGraphView(true)
        }

        for (i in 1..11) {
            val xCoordinateTextView = TextView(context)
            xCoordinateTextView.layoutParams = LayoutParams((24 * resources.displayMetrics.scaledDensity).toInt(), ViewGroup.LayoutParams.MATCH_PARENT)
            xCoordinateTextView.gravity = Gravity.CENTER
            xCoordinateTextView.text = ((i - 1) * 10).toString()
            xCoordinateTextView.textSize = 7.0f
            xCoordinateTextView.setTextColor(context.getColor(R.color.colorWhite))
            linX.addView(xCoordinateTextView)

            val yCoordinateTextView = TextView(context)
            yCoordinateTextView.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (24 * resources.displayMetrics.scaledDensity).toInt())
            yCoordinateTextView.gravity = Gravity.CENTER
            yCoordinateTextView.text = ((11 - i)  * 10).toString()
            yCoordinateTextView.textSize =7.0f
            yCoordinateTextView.setTextColor(context.getColor(R.color.colorWhite))
            linY.addView(yCoordinateTextView)
        }

        refreshGraphView(true)
    }

    private fun changeCurvePointActionType() {
        btnAdd.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_left)
        btnAdjust.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_center)
        btnDelete.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_right)

        when (curvePointActionType) {
            CurvePointActionType.ADD -> {
                btnAdd.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_left)
            }
            CurvePointActionType.ADJUST -> {
                btnAdjust.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_center)
            }
            CurvePointActionType.DELETE -> {
                btnDelete.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_right)
            }
        }

        refreshGraphView(false)
    }

    private fun changeCurveType() {
        btnLinear.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_left)
        btnCurves.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_right)

        when (curveType) {
            CurveType.LINEAR -> {
                btnLinear.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_left)
            }
            CurveType.CURVES -> {
                btnCurves.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_right)
            }
        }

        refreshGraphView(false)
    }

    private fun refreshGraphView(isClearClicked : Boolean) {
        linGraph.removeAllViews()
        val graphView = GraphView(context, this, curveType, curvePointActionType, isClearClicked)
        graphView.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        linGraph.addView(graphView)
        invalidate()
    }
}

enum class CurvePointActionType {
    ADD, ADJUST, DELETE
}

enum class CurveType {
    LINEAR, CURVES
}