package com.jery.feedformulation7

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun TMRMaker(v: View?) {
        val intent = Intent(this, TMRMaker::class.java)
        startActivity(intent)
    }
    fun showFeedsList(v: View?) {
        startActivity(Intent(this, FeedList::class.java))
    }
}