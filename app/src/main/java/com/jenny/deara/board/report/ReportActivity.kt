package com.jenny.deara.board.report

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.jenny.deara.R
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        //취소버튼
        report_cancel.setOnClickListener{
            onBackPressed()
        }

        //확인버튼
        report_submit.setOnClickListener{
            val reportPopupFragment = CustomDialog()
            reportPopupFragment.show(supportFragmentManager, "FragmentReportPopup")
        }

        //뒤로가기
        back.setOnClickListener{
            onBackPressed()
        }


        radio_group.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId) {
                R.id.radio1 -> {
                    radio1_1.isVisible=true
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio2 -> {
                    radio2_2.isVisible=true
                    radio1_1.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio3 -> {
                    radio3_3.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio4 -> {
                    radio4_4.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio5 -> {
                    radio5_5.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio6 -> {
                    radio6_6.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio7 -> {
                    edittext.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                }
            }
        }
    }


}