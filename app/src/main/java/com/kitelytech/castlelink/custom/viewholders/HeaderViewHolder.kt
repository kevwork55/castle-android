package com.kitelytech.castlelink.custom.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kitelytech.castlelink.R
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class HeaderViewHolder(itemView: View): GroupViewHolder(itemView) {
    var headerTextView: TextView = itemView.findViewById(R.id.header_text_view)
    var logoImageView: ImageView = itemView.findViewById(R.id.logo_image_view)
}