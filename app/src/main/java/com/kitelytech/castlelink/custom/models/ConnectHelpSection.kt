package com.kitelytech.castlelink.custom.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kotlinx.android.parcel.Parcelize

class ConnectHelpSection(
    val isHeader: Boolean,
    val isFooter: Boolean,
    val name: String,
    val subName: String,
    @DrawableRes var drawable: Int,
    items: MutableList<ConnectHelpName>
) : ExpandableGroup<ConnectHelpName>(name, items)

@Parcelize
data class ConnectHelpName (
    var name: String,
    var category: String
) : Parcelable

data class Section(
    val items : MutableList<ConnectHelp>
)