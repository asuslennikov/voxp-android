package ru.voxp.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_activity)
    }
}
