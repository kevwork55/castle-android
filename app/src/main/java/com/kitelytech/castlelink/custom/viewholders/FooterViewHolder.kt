package com.kitelytech.castlelink.custom.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kitelytech.castlelink.R
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class FooterViewHolder(itemView: View): GroupViewHolder(itemView) {
    var footerTextView: TextView = itemView.findViewById(R.id.footer_text_view)
    var logoImageView: ImageView = itemView.findViewById(R.id.logo_image_view)
}