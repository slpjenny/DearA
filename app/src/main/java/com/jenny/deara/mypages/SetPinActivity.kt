package com.jenny.deara.mypages

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jenny.deara.R
import kotlinx.android.synthetic.main.activity_pin_lock.*

class SetPinActivity : AppCompatActivity() {

    var lock = true // 잠금 상태 여부 확인

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_lock)

        /*init()

        // 잠금 설정 버튼을 눌렀을때
        btnSetLock.setOnClickListener {
            val intent = Intent(this, PinLockPassWordActivity::class.java).apply {
                putExtra(PinLockConst.type, PinLockConst.ENABLE_PASSLOCK)
            }
            startActivityForResult(intent, PinLockConst.ENABLE_PASSLOCK)
        }

        // 잠금 비활성화 버튼을 눌렀을때
        btnSetDelLock.setOnClickListener{
            val intent = Intent(this, PinLockPassWordActivity::class.java).apply {
                putExtra(PinLockConst.type, PinLockConst.DISABLE_PASSLOCK)
            }
            startActivityForResult(intent, PinLockConst.DISABLE_PASSLOCK)
        }

        // 암호 변경버튼을 눌렀을때
        btnChangePwd.setOnClickListener {
            val intent = Intent(this, PinLockPassWordActivity::class.java).apply {
                putExtra(PinLockConst.type, PinLockConst.CHANGE_PASSWORD)
            }
            startActivityForResult(intent, PinLockConst.CHANGE_PASSWORD)
        }
    }

    // startActivityForResult 결과값을 받는다.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            PinLockConst.ENABLE_PASSLOCK ->
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(this, "암호 설정 됨", Toast.LENGTH_SHORT).show()
                    init()
                    lock  = false
                }

            PinLockConst.DISABLE_PASSLOCK ->
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(this, "암호 삭제 됨", Toast.LENGTH_SHORT).show()
                    init()
                }

            PinLockConst.CHANGE_PASSWORD ->
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(this, "암호 변경 됨", Toast.LENGTH_SHORT).show()
                    lock = false
                }

            PinLockConst.UNLOCK_PASSWORD ->
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(this, "잠금 해제 됨", Toast.LENGTH_SHORT).show()
                    lock = false
                }
        }

    }

    // 액티비티가 onStart인 경우
    override fun onStart() {
        super.onStart()
        if(lock && PinLock(this).isPassLockSet()){
            val intent = Intent(this, PinLockPassWordActivity::class.java).apply {
                putExtra(PinLockConst.type, PinLockConst.UNLOCK_PASSWORD)
            }
            startActivityForResult(intent, PinLockConst.UNLOCK_PASSWORD)
        }

    }

    // 액티비티가 onPause인경우
    override fun onPause() {
        super.onPause()
        if (PinLock(this).isPassLockSet()) {
            lock = true // 잠금로 변경
        }
    }

    // 버튼 비활성화
    private fun init(){
        if (PinLock(this).isPassLockSet()){
            btnSetLock.isEnabled = false
            btnSetDelLock.isEnabled = true
            btnChangePwd.isEnabled = true
            lock = true
        }
        else{
            btnSetLock.isEnabled = true
            btnSetDelLock.isEnabled = false
            btnChangePwd.isEnabled = false
            lock = false
        }*/
    }
}