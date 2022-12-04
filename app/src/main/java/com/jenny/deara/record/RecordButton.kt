package com.jenny.deara.record

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.solver.state.State
import com.jenny.deara.R

class RecordButton(context: Context,
                   attrs: AttributeSet
)
    : AppCompatImageButton(context, attrs) {

    fun updateIconWithState(state: voiceState){
        when(state) {

            // 녹음 전
            voiceState.BEFORE_RECORDING ->{
                setImageResource(R.drawable.record_start)
            }

            // 녹음 중
            voiceState.ON_RECORDING -> {
                setImageResource(R.drawable.record_stop)
            }

            // 녹음 완료 후
            voiceState.AFTER_RECORDING -> {

                setImageResource(R.drawable.play_button)

            }

            // 재생중
            voiceState.ON_PLAYING -> {

                setImageResource(R.drawable.vinyl)

            }
        }
    }
}