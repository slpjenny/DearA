package com.jenny.deara.home

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.jenny.deara.R

class TodoDialog(context: Context) {

    private val dialog = Dialog(context)

    fun showDia(){
        dialog.setContentView(R.layout.tododialog_item)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        val editText = dialog.findViewById<EditText>(R.id.todowrite)
        val okBtn = dialog.findViewById<Button>(R.id.okbtn)

        okBtn.setOnClickListener {
            onClickListener.onClicked(editText.text.toString())


            if(editText.length() == 0){
                Toast.makeText(dialog.context, "해야할 일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else{
                dialog.dismiss()
            }

        }

        dialog.show()

    }

    interface ButtonClickListener{
        fun onClicked(text: String)
    }

    private lateinit var onClickListener: ButtonClickListener

    fun setOnClickListener(listener: ButtonClickListener){
        onClickListener = listener
    }

}
