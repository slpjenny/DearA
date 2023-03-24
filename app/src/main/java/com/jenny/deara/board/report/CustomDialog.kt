package com.jenny.deara.board.report

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jenny.deara.MainActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentReportConfirmPopupBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef


class CustomDialog(key: String) : DialogFragment() {

    private var _binding: FragmentReportConfirmPopupBinding? = null

    private val binding get() = _binding!!
    val database = Firebase.database

    val key : String = key
    var count : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentReportConfirmPopupBinding.inflate(inflater, container, false)

        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // dialog 밖 누르면 창 없어지지 않게
        dialog?.setCanceledOnTouchOutside(false)

        // 확인버튼 (신고 완료)
        binding.confirm.setOnClickListener {

            // firebase에서 읽기
            getFBReportData()

            // 커뮤니티 목록으로
            changeFragment()

        }

        return view
    }


    // 파이어베이스에서 읽기
    private fun getFBReportData() {
        FBRef.reportRef
            .child(key)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var dataModel = snapshot.getValue(ReportModel::class.java)
                    val uid = FBAuth.getUid()
                    var value = dataModel?.report_count
                    var user1 = dataModel?.reporter_uid1
                    var user2 = dataModel?.reporter_uid2
                    var user3 = dataModel?.reporter_uid3
                    var user4 = dataModel?.reporter_uid4
                    var user5 = dataModel?.reporter_uid5


                    if(value==null && user1==null)
                    {
                        user1=uid
                        FBRef.reportRef
                            .child(key)
                            .setValue(ReportModel(++count, user1))
                    }

                    //중복 신고 막기
                    else {
                        count = value!!
                        when(count) {
                            1 -> {
                                user2 = uid
                                FBRef.reportRef
                                    .child(key)
                                    .setValue(ReportModel(++count, user1!!, user2))
                            }
                            2-> {
                                user3=uid
                                FBRef.reportRef
                                    .child(key)
                                    .setValue(ReportModel(++count, user1!!, user2!!, user3))
                            }
                            3-> {
                                user4=uid
                                FBRef.reportRef
                                    .child(key)
                                    .setValue(ReportModel(++count, user1!!, user2!!, user3!!, user4))
                            }
                            4-> {
                                user5=uid
                                FBRef.reportRef
                                    .child(key)
                                    .setValue(ReportModel(++count, user1!!, user2!!, user3!!, user4!!, user5))

                                //누적 5회 신고 됐으니까
                                FBRef.boardRef.child(key).removeValue()
                                Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT)
                                    .show() // 없앨 코드
                            }
                        }

                    }
                }

            })
    }

    private fun changeFragment()
    {

        var intent = Intent(context, MainActivity::class.java)
        intent.putExtra("nav", "fifth")
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}