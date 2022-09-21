package com.jenny.deara

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jenny.deara.databinding.ActivitySelfDiagnosisBinding

class SelfDiagnosisActivity : AppCompatActivity() {

    val binding by lazy { ActivitySelfDiagnosisBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 고려대학교 안암 병원
        binding.diagnosisBtn1.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://anam.kumc.or.kr/info/self_diagnosis_reg_02.jsp?ST_NO=2&cPage=7&cPage=7"))
            startActivity(intent)
        }

        //  보건복지부 국립춘천병원
        binding.diagnosisBtn2.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.cnmh.go.kr/cnmh/medical/medical_04.jsp?menu_cd=M_05_03"))
            startActivity(intent)
        }

        // 강남구보건소 정신건강복지센터
        binding.diagnosisBtn3.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://www.gangnam.go.kr/office/smilegn/contents/smilegn_adhd/1/view.do?mid=smilegn_adhd"))
            startActivity(intent)
        }

        // 뒤로가기
        binding.back2.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}