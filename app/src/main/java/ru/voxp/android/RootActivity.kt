package ru.voxp.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

const val SPLASH_FRAGMENT_TAG: String = "SPLASH"

class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_activity)
    }
}
