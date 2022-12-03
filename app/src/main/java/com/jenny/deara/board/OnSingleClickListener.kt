package com.jenny.deara.board

import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.viewmodel.viewModelFactory

class OnSingleClickListener
    (private val onClickListener: (view: View) -> Unit
) : View.OnClickListener {

    companion object
    {
    const val CLICK_INTERVAL: Long = 10000 //클릭 간 간격(10초)
    const val TAG = "OnSingleClickListener" //로그 확인을 위한 string
    }

    private var clickable = true //클릭 가능한 타이밍

    //이전 클릭 시간 기록
    private var lastClickedTime = 0L// 클릭 시간

    override fun onClick(view: View) {
        // 클릭 시간
        val onClickedTime = SystemClock.elapsedRealtime()

        // 간격보다 작으면 클릭 no
         if ((onClickedTime-lastClickedTime) < CLICK_INTERVAL) {
             return
         }

        lastClickedTime = onClickedTime
        onClickListener.invoke(view)
    }
}

fun View.setOnSingleClickListener(
    onClickListener: (view: View) -> Unit
) {
    setOnClickListener(OnSingleClickListener(onClickListener))
}