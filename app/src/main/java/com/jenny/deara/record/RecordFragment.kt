package com.jenny.deara.record

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentRecordBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

class RecordFragment : Fragment() {

    private lateinit var binding: FragmentRecordBinding

    lateinit var RecordListAdapter: RecordListAdapter

    val recordList = mutableListOf<RecordData>()
    val recordkeyList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)

        initRecycler()
        getFBRecordData()

        binding.plusButton.bringToFront()

        // 진료기록 추가 화면으로 페이지 전환
        binding.plusButton.setOnClickListener {
            val intent = Intent(context,AddRecordActivity::class.java)
            startActivity(intent)
        }


        binding.mypage.setOnClickListener {
            val intent2 = Intent(context,MyPageActivity::class.java)
            startActivity(intent2)
        }

        return binding.root
    }


    // 리싸이클러뷰 띄우기
    private fun initRecycler() {
        RecordListAdapter = RecordListAdapter(requireContext(),recordkeyList)

        val rv : RecyclerView = binding.recordRv
        rv.adapter= RecordListAdapter

        RecordListAdapter.datas = recordList
        RecordListAdapter.notifyDataSetChanged()
    }

    //파이어베이스 데이터 불러오기
    private fun getFBRecordData(){

        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                recordList.clear()

                for (dataModel in dataSnapshot.children) {
                    // 리싸이클러뷰 데이터에 항목 세개만 넣어서 추가하기
                    val item = dataModel.getValue(RecordData::class.java)

                    if (item != null) {
                        if(FBAuth.getUid() == item.uid){
                            recordList.add(item)
                            recordkeyList.add(dataModel.key.toString())
                        }
                    }

                }
                recordkeyList.reverse()
                recordList.reverse()
                RecordListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("recordListTest", "취소되었습니다.", databaseError.toException())
            }
        }
        FBRef.recordRef.addValueEventListener(postListener)
    }


}