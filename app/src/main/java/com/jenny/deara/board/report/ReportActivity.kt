package com.jenny.deara.board.report

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity() : AppCompatActivity() {

    var count : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        //취소버튼
        report_cancel.setOnClickListener{
            onBackPressed()
        }

        //뒤로가기
        back.setOnClickListener{
            onBackPressed()
        }

        radio_group.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId) {
                R.id.radio1 -> {
                    report_submit.isEnabled=true
                    radio1_1.isVisible=true
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio2 -> {
                    report_submit.isEnabled=true
                    radio2_2.isVisible=true
                    radio1_1.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio3 -> {
                    report_submit.isEnabled=true
                    radio3_3.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio4 -> {
                    report_submit.isEnabled=true
                    radio4_4.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio5 -> {
                    report_submit.isEnabled=true
                    radio5_5.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio6 -> {
                    report_submit.isEnabled=true
                    radio6_6.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio7 -> {
                    report_submit.isEnabled=true
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

        //확인버튼
        report_submit.setOnClickListener{
            //key값 받아오기
            val key = intent.getStringExtra("key")

            val reportPopupFragment = CustomDialog(key.toString())
            reportPopupFragment.show(supportFragmentManager, "FragmentReportPopup")

        }

    }


}