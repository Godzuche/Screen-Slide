package com.godzuche.screenslide

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.godzuche.screenslide.page_transformers.DepthPageTransformer
import com.godzuche.screenslide.page_transformers.ZoomOutPageTransformer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ScreenSlidePagerFragment : Fragment() {
    lateinit var viewPager: ViewPager2
    private lateinit var preferenceDataStore: PreferenceDataStore
    private var isZoomOutTransformer = false
    private var isDepthTransformer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.fragment_screen_slide, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.pager)
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.apply {
            adapter = pagerAdapter
//            setPageTransformer(DepthPageTransformer())
        }

        preferenceDataStore = PreferenceDataStore(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferenceDataStore.preferencesFlow
                    .map { it[PreferenceDataStore.IS_ZOOM_OUT_PAGE_TRANSFORMER] }
                    .distinctUntilChanged()
                    .collectLatest {
                        it?.let {
                            isZoomOutTransformer = it
                            useZoomOutTransformer(pagerAdapter)
                        }
                    }

            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferenceDataStore.preferencesFlow
                    .map { it[PreferenceDataStore.IS_DEPTH_PAGE_TRANSFORMER] }
                    .distinctUntilChanged()
                    .collectLatest {
                        it?.let {
                            isDepthTransformer = it
                            useDepthTransformer(pagerAdapter)
                        }
                    }

            }
        }

        MainActivity.onBackButtonPressed {
            var position = 0
            if (viewPager.currentItem != 0) {
                position = viewPager.currentItem
                viewPager.currentItem--
                Log.d("Current Item after minus one", "is ${viewPager.currentItem}")
            } else {
                position = viewPager.currentItem
            }
            Log.d("Current Item before returning", "is ${viewPager.currentItem}")
            return@onBackButtonPressed position
        }

    }

    private fun useDepthTransformer(pagerAdapter: ScreenSlidePagerAdapter) {
        if (!isDepthTransformer && !isZoomOutTransformer) {
            useDefaultTransformer(pagerAdapter)
            return
        }
        if (isDepthTransformer) {
            viewPager.apply {
                adapter = pagerAdapter
                setPageTransformer(DepthPageTransformer())
            }
        }
    }

    private fun useZoomOutTransformer(pagerAdapter: ScreenSlidePagerAdapter) {
        if (!isDepthTransformer && !isZoomOutTransformer) {
            useDefaultTransformer(pagerAdapter)
            return
        }
        if (isZoomOutTransformer) {
            viewPager.apply {
                adapter = pagerAdapter
                setPageTransformer(ZoomOutPageTransformer())
            }
        }
    }

    private fun useDefaultTransformer(pagerAdapter: ScreenSlidePagerAdapter) {
        viewPager.apply {
            adapter = pagerAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.transform_zoom_out -> {
                isZoomOutTransformer = !isZoomOutTransformer
                viewLifecycleOwner.lifecycleScope.launch {
                    preferenceDataStore.setZoomOutPageTransformer(true, requireContext())
                }
                true
            }
            R.id.transform_depth -> {
                isDepthTransformer = !isDepthTransformer
                viewLifecycleOwner.lifecycleScope.launch {
                    preferenceDataStore.setDepthPageTransformer(true, requireContext())
                }
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

}

const val ARG_OBJECT = "object"
const val NUM_PAGES = 5
