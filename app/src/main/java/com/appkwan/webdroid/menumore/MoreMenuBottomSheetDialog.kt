package com.appkwan.webdroid.menumore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appkwan.webdroid.R
import com.appkwan.webdroid.utils.CustomRecyclerItemSpaceDecoration
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MoreMenuBottomSheetDialog(var menuList: ArrayList<MoreMenuItem>) : BottomSheetDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.more_menu_bottom_sheet_layout, container, false)
        var more_menu_recyclerview = view.findViewById(R.id.more_menu_recycler_view) as RecyclerView

        more_menu_recyclerview.layoutManager = GridLayoutManager(context, 4, RecyclerView.VERTICAL, false)
        more_menu_recyclerview.adapter = MoreMenuAdapter(menuList, activity as MoreMenuClickListener)
        more_menu_recyclerview.addItemDecoration(CustomRecyclerItemSpaceDecoration(15, 15, 25, 25, 55, 5 ))
        return view
    }
}