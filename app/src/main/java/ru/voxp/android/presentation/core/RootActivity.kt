package ru.voxp.android.presentation.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.voxp.android.R.layout

class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.root_activity)
    }
}
