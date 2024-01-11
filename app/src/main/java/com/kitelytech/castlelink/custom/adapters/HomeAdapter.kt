package com.kitelytech.castlelink.custom.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.custom.models.*
import com.kitelytech.castlelink.custom.viewholders.ProductViewHolder
import com.kitelytech.castlelink.custom.viewholders.FooterViewHolder
import com.kitelytech.castlelink.custom.viewholders.HeaderViewHolder
import com.kitelytech.castlelink.custom.viewholders.HomeViewHolder
import com.thoughtbot.expandablerecyclerview.MultiTypeExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder


class HomeAdapter(
    groups: List<ExpandableGroup<*>>,
    gridLayoutManager: GridLayoutManager,
    private val sectionClickListener: SectionClickListener
): MultiTypeExpandableRecyclerViewAdapter<GroupViewHolder, ProductViewHolder>(groups, gridLayoutManager) {
    private lateinit var mContext: Context

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        mContext = parent.context
        return when (viewType) {
            HEADER_VIEW_TYPE -> {
                val artist = LayoutInflater.from(parent.context).inflate(R.layout.cell_home_header, parent, false)
                HeaderViewHolder(artist)
            }
            FOOTER_VIEW_TYPE -> {
                val favorite = LayoutInflater.from(parent.context).inflate(R.layout.cell_home_footer, parent, false)
                FooterViewHolder(favorite)
            }
            COMMON_VIEW_TYPE -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.cell_home, parent, false)
                HomeViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindChildViewHolder(
        holder: ProductViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>,
        childIndex: Int
    ) {
        val section = (group as ConnectHelpSection).items[childIndex]
        holder.nameTextView.text = section.name
        holder.itemView.setOnClickListener {
            sectionClickListener.onClickSection(section)
        }
    }

    override fun onBindGroupViewHolder(
        holder: GroupViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?
    ) {
        when (getItemViewType(flatPosition)) {
            HEADER_VIEW_TYPE -> {
                val headerViewHolder = holder as HeaderViewHolder
                val drawable = (group as ConnectHelpSection).drawable
                val name = group.name
                headerViewHolder.logoImageView.setImageDrawable(
                    ContextCompat.getDrawable(mContext, drawable)
                )
                headerViewHolder.headerTextView.text = name
            }
            FOOTER_VIEW_TYPE -> {
                val footerViewHolder = holder as FooterViewHolder
                val drawable = (group as ConnectHelpSection).drawable
                val name = group.name
                footerViewHolder.logoImageView.setImageDrawable(
                    ContextCompat.getDrawable(mContext, drawable)
                )
                footerViewHolder.footerTextView.text = name
            }
            COMMON_VIEW_TYPE -> {
                val homeViewHolder = holder as HomeViewHolder
                val drawable = (group as ConnectHelpSection).drawable
                homeViewHolder.logoImageView.setBackgroundResource(drawable)
                homeViewHolder.titleTextView.text = group.name
                homeViewHolder.descriptionTextView.text = group.subName
            }
        }
    }

    override fun getGroupViewType(position: Int, group: ExpandableGroup<*>?): Int {
        val section = group as ConnectHelpSection
        return when {
            section.isHeader -> {
                HEADER_VIEW_TYPE
            }
            section.isFooter -> {
                FOOTER_VIEW_TYPE
            }
            else -> {
                COMMON_VIEW_TYPE
            }
        }
    }

    override fun isGroup(viewType: Int): Boolean {
        return viewType == COMMON_VIEW_TYPE || viewType == HEADER_VIEW_TYPE || viewType == FOOTER_VIEW_TYPE
    }

    companion object {
        const val COMMON_VIEW_TYPE = 3
        const val HEADER_VIEW_TYPE = 4
        const val FOOTER_VIEW_TYPE = 5
    }
}

class SectionClickListener(
    val onClickSectionListener: (section: ConnectHelpName) -> Unit
) {
    fun onClickSection(section: ConnectHelpName) = onClickSectionListener(section)
}