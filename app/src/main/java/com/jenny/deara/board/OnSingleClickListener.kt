package com.jenny.deara.board

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.viewmodel.viewModelFactory

class OnSingleClickListener (private val clickListener: View.OnClickListener) : View.OnClickListener {

    companion object {
        const val CLICK_INTERVAL: Long = 5000 //클릭 간 간격(3초)
        const val TAG = "OnSingleClickListener" //로그 확인을 위한 string
    }

    private var clickable = true //클릭 가능한 타이밍

    //클릭 시
    override fun onClick(v: View?) {
        if (clickable) {
            clickable = false
            v?.run {
                postDelayed({
                    clickable = true
                }, CLICK_INTERVAL)
                clickListener.onClick(v)

            }
        } else {
            Log.d(TAG, "글쓰기 시간제한 (테스트라 5초만)")
        }
    }
}

fun View.setOnSingleClickListener(action: (v: View) -> Unit) {
    val listener = View.OnClickListener { action(it) }
    setOnClickListener(OnSingleClickListener(listener))
}