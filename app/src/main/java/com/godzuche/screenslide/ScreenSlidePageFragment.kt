package com.godzuche.screenslide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.google.android.material.textview.MaterialTextView

class ScreenSlidePageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_screen_slide_page, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            view.findViewById<MaterialTextView>(R.id.tv_content)
                .text = getInt(ARG_OBJECT).toString()
            view.findViewById<ScrollView>(R.id.content)
                .background =
                resources.getColor(colors[getInt(ARG_OBJECT, 1) - 1], null).toDrawable()
        }
    }

}

val colors = listOf(
    android.R.color.holo_blue_bright,
    android.R.color.holo_green_light,
    android.R.color.holo_orange_light,
    android.R.color.holo_red_light,
    android.R.color.holo_blue_light
)