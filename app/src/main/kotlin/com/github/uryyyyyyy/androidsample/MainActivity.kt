package com.github.uryyyyyyy.androidsample;

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import io.reactivex.subjects.BehaviorSubject


class MainActivity : Activity() {

    val count: BehaviorSubject<Int> = BehaviorSubject.createDefault(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        setContentView(linearLayout)

        val addButton = Button(this)
        addButton.text = "add 1"
        linearLayout.addView(addButton)
        addButton.setOnClickListener{Unit ->
            count.onNext(count.value + 1)
        }

        val minusButton = Button(this)
        minusButton.text = "minus 1"
        linearLayout.addView(minusButton)
        minusButton.setOnClickListener{Unit ->
            count.onNext(count.value - 1)
        }

        val scoreView = TextView(this)
        linearLayout.addView(scoreView)
        count.subscribe{v -> scoreView.text = "${v}点だよ" }

        val backButton = Button(this)
        backButton.text = "back"
        linearLayout.addView(backButton)
//        backButton.setOnClickListener{v ->
//            val intent = Intent(this, javsaClass<MainActivity>())
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP + Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
        Log.e("shiba", "last")
    }
}