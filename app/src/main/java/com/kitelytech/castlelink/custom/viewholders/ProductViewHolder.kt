package com.kitelytech.castlelink.custom.viewholders

import android.view.View
import android.widget.TextView
import com.kitelytech.castlelink.R
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder

class ProductViewHolder(itemView: View): ChildViewHolder(itemView) {
    var nameTextView: TextView = itemView.findViewById(R.id.name_text_view)
}