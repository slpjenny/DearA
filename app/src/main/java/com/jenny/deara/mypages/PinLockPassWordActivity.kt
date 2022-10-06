package com.jenny.deara.mypages

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.jenny.deara.R
import kotlinx.android.synthetic.main.activity_pin_lock_password.*

class PinLockPassWordActivity : AppCompatActivity() {

    private var oldPwd =""
    private var changePwdUnlock = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_lock_password)

        val buttonArray = arrayListOf<Button>(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7 ,btn8, btn9, btnClear, btnErase)
        for (button in buttonArray){
            button.setOnClickListener(btnListener)
        }
    }

    // 버튼 클릭 했을때
    private val btnListener = View.OnClickListener { view ->
        var currentValue = -1
        when(view.id){
            R.id.btn0 -> currentValue = 0
            R.id.btn1 -> currentValue = 1
            R.id.btn2 -> currentValue = 2
            R.id.btn3 -> currentValue = 3
            R.id.btn4 -> currentValue = 4
            R.id.btn5 -> currentValue = 5
            R.id.btn6 -> currentValue = 6
            R.id.btn7 -> currentValue = 7
            R.id.btn8 -> currentValue = 8
            R.id.btn9 -> currentValue = 9
            R.id.btnClear -> onClear()
            R.id.btnErase -> onDeleteKey()
        }

        val strCurrentValue = currentValue.toString() // 현재 입력된 번호 String으로 변경
        if (currentValue != -1){
            when {
                etPasscode1.isFocused -> {
                    setEditText(etPasscode1, etPasscode2, strCurrentValue)
                }
                etPasscode2.isFocused -> {
                    setEditText(etPasscode2, etPasscode3, strCurrentValue)
                }
                etPasscode3.isFocused -> {
                    setEditText(etPasscode3, etPasscode4, strCurrentValue)
                }
                etPasscode4.isFocused -> {
                    etPasscode4.setText(strCurrentValue)
                }
            }
        }

        // 비밀번호를 4자리 모두 입력시
        if (etPasscode4.text.isNotEmpty() && etPasscode3.text.isNotEmpty() && etPasscode2.text.isNotEmpty() && etPasscode1.text.isNotEmpty()) {
            inputType(intent.getIntExtra("type", 0))
        }
    }

    // 한 칸 지우기를 눌렀을때
    private fun onDeleteKey() {
        when {
            etPasscode1.isFocused -> {
                etPasscode1.setText("")
            }
            etPasscode2.isFocused -> {
                etPasscode1.setText("")
                etPasscode1.requestFocus()
            }
            etPasscode3.isFocused -> {
                etPasscode2.setText("")
                etPasscode2.requestFocus()
            }
            etPasscode4.isFocused -> {
                etPasscode3.setText("")
                etPasscode3.requestFocus()
            }
        }
    }

    // 모두 지우기
    private fun onClear(){
        etPasscode1.setText("")
        etPasscode2.setText("")
        etPasscode3.setText("")
        etPasscode4.setText("")
        etPasscode1.requestFocus()
    }

    // 입력된 비밀번호를 합치기
    private fun inputedPassword():String {
        return "${etPasscode1.text}${etPasscode2.text}${etPasscode3.text}${etPasscode4.text}"
    }

    // EditText 설정
    private fun setEditText(currentEditText : EditText, nextEditText: EditText, strCurrentValue : String){
        currentEditText.setText(strCurrentValue)
        nextEditText.requestFocus()
        nextEditText.setText("")
    }

    // Intent Type 분류
    private fun inputType(type : Int) {
        when (type) {
            PinLockConst.ENABLE_PASSLOCK -> { // 잠금설정
                if (oldPwd.isEmpty()) {
                    oldPwd = inputedPassword()
                    onClear()
                    etInputInfo.text = "다시 한번 입력"
                } else {
                    if (oldPwd == inputedPassword()) {
                        PinLock(this).setPassLock(inputedPassword())
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        onClear()
                        oldPwd = ""
                        etInputInfo.text = "비밀번호 입력"
                    }
                }
            }

            PinLockConst.DISABLE_PASSLOCK -> { // 잠금삭제
                if (PinLock(this).isPassLockSet()) {
                    if (PinLock(this).checkPassLock(inputedPassword())) {
                        PinLock(this).removePassLock()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        etInputInfo.text = "비밀번호가 틀립니다."
                        onClear()
                    }
                } else {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }

            PinLockConst.UNLOCK_PASSWORD ->
                if (PinLock(this).checkPassLock(inputedPassword())) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    etInputInfo.text = "비밀번호가 틀립니다."
                    onClear()
                }

            PinLockConst.CHANGE_PASSWORD -> { // 비밀번호 변경
                if (PinLock(this).checkPassLock(inputedPassword()) && !changePwdUnlock) {
                    onClear()
                    changePwdUnlock = true
                    etInputInfo.text = "새로 설정할 비밀번호 입력"
                } else if (changePwdUnlock) {
                    if (oldPwd.isEmpty()) {
                        oldPwd = inputedPassword()
                        onClear()
                        etInputInfo.text = "새로 설정할 비밀번호 확인"
                    } else {
                        if (oldPwd == inputedPassword()) {
                            PinLock(this).setPassLock(inputedPassword())
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            onClear()
                            oldPwd = ""
                            etInputInfo.text = "현재 비밀번호 다시 입력"
                            changePwdUnlock = false
                        }
                    }
                } else {
                    etInputInfo.text = "비밀번호가 틀립니다."
                    changePwdUnlock = false
                    onClear()
                }
            }
        }
    }
}