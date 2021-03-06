package ru.voxp.android.presentation.core

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener

object Bindings {

    @JvmStatic
    @BindingAdapter("android:src")
    fun bindImageDrawableResourceId(view: ImageView, resourceId: Int?) {
        if (resourceId != null) {
            view.setImageDrawable(AppCompatResources.getDrawable(view.context, resourceId))
        } else {
            view.setImageDrawable(null)
        }
    }

    @JvmStatic
    @BindingAdapter("android:text")
    fun bindTextStringResourceId(view: TextView, resourceId: Int?) {
        if (resourceId != null) {
            view.setText(resourceId)
        } else {
            view.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("refresh")
    fun bindSwipeRefreshLayoutListener(view: SwipeRefreshLayout, listener: OnRefreshListener?) {
        view.setOnRefreshListener(listener)
    }
}