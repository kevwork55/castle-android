package com.kitelytech.castlelink.custom.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kitelytech.castlelink.R
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class HomeViewHolder(itemView: View): GroupViewHolder(itemView) {
    var logoImageView: ImageView = itemView.findViewById(R.id.logo_image_view)
    var titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
    var descriptionTextView: TextView = itemView.findViewById(R.id.description_text_view)
}