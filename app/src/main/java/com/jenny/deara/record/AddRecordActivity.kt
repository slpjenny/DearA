package com.jenny.deara.record

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.wrappers.Wrappers.packageManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityAddRecordBinding
import com.jenny.deara.databinding.ActivitySignInBinding
import com.jenny.deara.diary.DiaryData
import com.jenny.deara.diary.DiaryListAdapter
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import java.util.*
import java.util.jar.Manifest

class AddRecordActivity : AppCompatActivity() {

    val binding by lazy { ActivityAddRecordBinding.inflate(layoutInflater) }

    lateinit var RecordListAdapter: RecordListAdapter
    val recordList = mutableListOf<RecordData>()

    private val recordButton: RecordButton by lazy { findViewById(R.id.record_play)}

    // 녹음관련 필요한 권한 선언
    private val requiredPermissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    // 초기 녹음 상태 설정
    private var state = voiceState.BEFORE_RECORDING
        set(value) {
            field = value
            recordButton.updateIconWithState(value)
        }


    // 녹음 저장 선언
    private var recorder:MediaRecorder?=null
    private val recordingFilePath : String by lazy{
        // 외부 캐시 저장소의 path
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }

    // 녹음 재생 player 선언
    private var player: MediaPlayer? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        requestAudioPermission()
        initViews()
        bindViews()
        initVariables()

        // 날짜 선택하기
        binding.dateBtn.setOnClickListener {
            chooseDate()
        }


        // 시간 선택하기
        binding.timeBtn.setOnClickListener {
            choosetime()
        }


        // 진료기록 저장
        binding.saveBtn.setOnClickListener {
            saveFBDiaryData()
        }

        // 뒤로가기 버튼
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }


    }

    // 날짜 - Datepicekr
    private fun chooseDate() {
        var dateString = ""

        val cal = Calendar.getInstance()    //캘린더뷰 만들기
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${year}년 ${month + 1}월 ${dayOfMonth}일"
            binding.date.text = dateString
        }
        DatePickerDialog(
            this,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()

    }

    // 시간 - TimePicker
    private fun choosetime() {

        var timeString = ""

        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            timeString = "${hourOfDay}시 ${minute}분"
            binding.time.text = timeString
        }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }


    // 파이어베이스에 진료 기록 데이터 저장
    private fun saveFBDiaryData() {
        // 병원명,복용약,진료 메모 등 한꺼번에 객체로 저장해야함
        // 리싸이클러뷰로 불러올때에는 객체에서 특정 항목만 데이터리스트에 추가해야함!!
        val hospitalName = binding.hospitalName.text.toString()
        val date = binding.date.text.toString()
        val time = binding.time.text.toString()
        val pillName = binding.pillName.text.toString()
        val dosage = binding.dosage.text.toString()
        val memo = binding.memo.text.toString()
        val symptom = binding.symptom.text.toString()
        val uid = FBAuth.getUid()

        val key = FBRef.recordRef.push().key.toString()

        // 진료 기록 객체 형태로 저장
        FBRef.recordRef
            .child(key)
            .setValue(RecordData(hospitalName, date, time, pillName, dosage, memo, symptom, uid))

        Toast.makeText(this, "진료 기록이 저장되었습니다.", Toast.LENGTH_LONG).show()

        finish()
    }

    // 어떤 permission을 요청할 것인가
    // 어떤 request 결과를 받을것인지 requestCode 지정
    // RECORD_AUDIO 권한 넣어주기
    private fun requestAudioPermission(){
        requestPermissions(requiredPermissions,REQUEST_RECORD_AUDIO_PERMISSION)
    }


    // requestPermission 결과값 받아오기
    // grantResults - 배열로 권한이 부여되었는지,아닌지 표현한다.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // 권한이 부여받은게 맞는지 체크, 부여받았으면 true
         val audioRequestPermissionGranted =
             requestCode == REQUEST_RECORD_AUDIO_PERMISSION && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        //권한이 부여되지않으면 어플 종료
        if(!audioRequestPermissionGranted){
            finish()
        }

    }


    private fun initViews() {
        recordButton.updateIconWithState(state)
    }

    companion object{
        //permission code 선언
        private const val REQUEST_RECORD_AUDIO_PERMISSION =201
    }

    // 녹음할 수 있는 상태 만들기
    private fun startRecording(){
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFilePath)
            prepare()
        }
        recorder?.start()
        state = voiceState.ON_RECORDING

        Log.d("record_state","startRecording")

    }

    private fun stopRecording(){
        recorder?.run{
            stop()
            release()
        }
        recorder =null
        state = voiceState.AFTER_RECORDING

        Log.d("record_state","stopRecording")
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {

            // 외부 캐시 저장소의 path
            setDataSource(recordingFilePath)
            prepare()
        }
        player?.start()
        state = voiceState.ON_PLAYING

        Log.d("record_state","startPlaying")

    }

    private fun stopPlaying(){
        // player 는 stop 없이 release()로 바로 멈출 수 있음
        player?.release()
        player =null
        state = voiceState.AFTER_RECORDING

        Log.d("record_state","stopPlaying")

    }

    private fun bindViews() {

        recordButton.setOnClickListener {
            when (state) {
                voiceState.BEFORE_RECORDING -> {
                    startRecording()
                }
                voiceState.ON_RECORDING -> {
                    stopRecording()
                }
                voiceState.AFTER_RECORDING -> {
                    startPlaying()

                }
                voiceState.ON_PLAYING -> {
                    stopPlaying()
                }
            }
        }
    }
    private fun initVariables(){
        state = voiceState.BEFORE_RECORDING
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }


}