package com.jenny.deara.record

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.pilllist_item.view.*
import org.w3c.dom.Text
import java.util.*
import java.util.jar.Manifest


class AddRecordActivity : AppCompatActivity() {

    val binding by lazy { ActivityAddRecordBinding.inflate(layoutInflater) }

    lateinit var RecordListAdapter: RecordListAdapter
    val recordList = mutableListOf<RecordData>()

    // 복용 약 리싸이클러뷰
    lateinit var  PillListAdapter : PillListAdapter
    val pillList = mutableListOf<pillData>()
    val pillkeyList = mutableListOf<String>()

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
        initRecycler()


        binding.root.setOnClickListener {
            hideKeyboard()
        }

        binding.linearlayout.setOnClickListener {
            hideKeyboard()
        }

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
            saveFBRecordData()
        }


        // 복용 약 추가
        binding.addPillBtn.setOnClickListener{

            // 내용 존재할때만 추가 가능
            if(binding.pillName.text.toString().trim().isEmpty() || binding.dosage.text.toString().trim().isEmpty()){

            }else{
                // 파이어베이스에 복용약 내용 따로 저장
                saveFBPillData()
                PillListAdapter.notifyDataSetChanged()
            }
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
    private fun saveFBRecordData() {
        // 병원명,복용약,진료 메모 등 한꺼번에 객체로 저장해야함
        // 리싸이클러뷰로 불러올때에는 객체에서 특정 항목만 데이터리스트에 추가해야함!!
        val hospitalName = binding.hospitalName.text.toString()
        val date = binding.date.text.toString()
        val time = binding.time.text.toString()
//        val pillName = binding.pillName.text.toString()
//        val dosage = binding.dosage.text.toString()
        val memo = binding.memo.text.toString()
        val symptom = binding.symptom.text.toString()
        val uid = FBAuth.getUid()

        val key = FBRef.recordRef.push().key.toString()

        // 진료 기록 객체 형태로 저장
        FBRef.recordRef
            .child(key)
            .setValue(RecordData(hospitalName, date, time, memo, symptom, uid))


        Toast.makeText(this, "진료 기록이 저장되었습니다.", Toast.LENGTH_LONG).show()

        finish()
    }


    private fun saveFBPillData(){

        var pillNameTxt : String = binding.pillName.text.toString()
        var dosageTxt : String = binding.dosage.text.toString()

        var key = FBRef.pillRef.push().key.toString()
        val uid = FBAuth.getUid()

        // 복용 약 객체 형태로 저장
        FBRef.pillRef
            .child(key)
            .setValue(pillData(pillNameTxt,dosageTxt,uid))

        // recyclerview 데이터 리스트에 저장
        pillList.add(pillData(pillNameTxt,dosageTxt))

        // 저장 후에 editTextView 빈칸으로 비우기
        binding.pillName.setText("")
        binding.dosage.setText("")

    }

    // 복용 약 RecyclerView 띄우기
    private fun initRecycler(){
        PillListAdapter = PillListAdapter(this,pillList)

        val rv : RecyclerView = binding.pillLayout
        rv.adapter= PillListAdapter

//        PillListAdapter.pills = pillList
        PillListAdapter.notifyDataSetChanged()
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
            //MediaRecorder 설정하기
            setAudioSource(MediaRecorder.AudioSource.MIC)  // 오디오 입력을 마이크로 받음
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)  // 출력 포멧 설정 (MPEG4) 로 왜 안했지?
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // DEFAULT 로 설정 왜 안했지?
            setOutputFile(recordingFilePath)
            prepare()
        }

        //MediaRecorder 시작시키기
        recorder?.start()

        //state 변환시켜서 아이콘 변경
        state = voiceState.ON_RECORDING

        Log.d("record_state","startRecording")
    }

    private fun stopRecording(){
        recorder?.run{
            // 녹음 중지를 위한 두 개의 메서드
            stop()
                // MediaRecorder의 리소스를 해제하는 역할
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
        // 미디어플레이어를 앱 내에서 재사용하려면 기존에 사용하던 리소스를 먼저 해제해야함
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

    private fun hideKeyboard() {
        if(this != null && this.currentFocus != null) {
            val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


}
