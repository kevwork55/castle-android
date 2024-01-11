package com.kitelytech.castlelink.core.presentation.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.AdapterView
import androidx.appcompat.widget.LinearLayoutCompat
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.core.presentation.ui.widget.adapter.MultiplySelectorAdapter
import com.kitelytech.castlelink.databinding.LayoutMultiplySelectorBinding

class MultipleSelectorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val binding = LayoutMultiplySelectorBinding.inflate(LayoutInflater.from(context), this)
    var onItemClickListener: AdapterView.OnItemClickListener? = null
    private var adapter: MultiplySelectorAdapter? = null

    private var items: List<String>? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultipleSelectorView)
        try {
            items = typedArray.getTextArray(
                R.styleable.MultipleSelectorView_msv_items
            ).map { it.toString() }
            setItems(items!!)
        } finally {
            typedArray.recycle()
        }
    }

    fun setItems(items: List<String>) = with(binding) {
        itemGv.numColumns = items.size
        adapter = MultiplySelectorAdapter(context, R.layout.item_multiply_selector, items)
        itemGv.adapter = adapter
        itemGv.setOnItemClickListener { parent, view, position, id ->
            adapter?.selectedPosition = position
            adapter?.notifyDataSetChanged()
            onItemClickListener?.onItemClick(parent, view, position, id)
        }
    }



}