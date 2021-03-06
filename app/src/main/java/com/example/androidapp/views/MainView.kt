package com.example.androidapp.views

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.androidapp.R
import com.example.androidapp.models.tools.quiz.QuizMaster
import com.example.androidapp.views.adapters.MainAdapter
import com.google.android.material.tabs.TabLayout


class MainView : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        setUpTabs()
        setUpViewPager()

        QuizMaster.setEmission(3.0)
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }

 //       resultLauncher.launch(Intent(this, GameView::class.java))
    }

    private fun setUpTabs(){
        with(tabLayout) {

            addTab(setupTab(R.drawable.ic_home))
            addTab(setupTab(R.drawable.ic_graph))
            addTab(setupTab(R.drawable.ic_basket))


            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewPager.currentItem = tab!!.position
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Handle tab reselect
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Handle tab unselect
                }
            })
        }
    }
    private fun setupTab(drawable: Int): TabLayout.Tab {
        val newTab = tabLayout.newTab()
        val view = getLayoutInflater().inflate(R.layout.customtab,null);
        view.setBackgroundResource(drawable)
        newTab.customView = view
        return newTab
    }

    private fun setUpViewPager(){
        viewPagerAdapter = MainAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = viewPagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}
