package com.jenny.deara.record

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentRecordBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.fragment_record.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class RecordFragment : Fragment() {

    private lateinit var binding: FragmentRecordBinding

    lateinit var RecordListAdapter: RecordListAdapter
    lateinit var TodayRecordAdapter: TodayRecordAdapter

    val recordList = mutableListOf<RecordData>()
    val recordkeyList = mutableListOf<String>()

    // 오늘 진료기록 recyclerview 변수
    val todayRecordList = mutableListOf<TodayRecordData>()
    val todayRecordKeyList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)

        initRecycler()
        initTodayRecycler()
        getFBRecordData()

        binding.plusButton.bringToFront()
        binding.todayRcRv.bringToFront()

        // 진료기록 추가 화면으로 페이지 전환
        binding.plusButton.setOnClickListener {
            val intent = Intent(context,AddRecordActivity::class.java)
            startActivity(intent)
        }


        // 마이페이지 버튼
        binding.mypage.setOnClickListener {
            val intent2 = Intent(context,MyPageActivity::class.java)
            startActivity(intent2)
        }


        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initRecycler()
        initTodayRecycler()
        getFBRecordData()
    }


    // 리싸이클러뷰 띄우기
    private fun initRecycler() {
        RecordListAdapter = RecordListAdapter(requireContext(),recordkeyList)

        val rv : RecyclerView = binding.recordRv
        rv.adapter= RecordListAdapter

        RecordListAdapter.datas = recordList
        RecordListAdapter.notifyDataSetChanged()
    }


    // 오늘 진료일정 리싸이클러뷰 띄우기
    private fun initTodayRecycler() {
        TodayRecordAdapter = TodayRecordAdapter(requireContext(),todayRecordKeyList)

        val rv : RecyclerView = binding.todayRcRv
        rv.adapter= TodayRecordAdapter

        TodayRecordAdapter.datas = todayRecordList
        TodayRecordAdapter.notifyDataSetChanged()

    }



    //파이어베이스 데이터 불러오기
    private fun getFBRecordData(){

        // 오늘 날짜 불러오기
        var now = LocalDate.now()
        var nowDate :String = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 "))
        var nowDay :String = now.dayOfMonth.toString()

        // 시스템에서 불러오는 오늘 날짜는 두자리수로 표현되기에 한자리수로 맞추기위해 따로 처리해줌
        nowDate = nowDate+nowDay+"일"



        // 데이터 불러오기
        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                recordList.clear()
                todayRecordList.clear()

                for (dataModel in dataSnapshot.children) {
                    // 리싸이클러뷰 데이터에 항목 세개만 넣어서 추가하기
                    val item = dataModel.getValue(RecordData::class.java)
                    val todayItem = dataModel.getValue(TodayRecordData::class.java)

                    if (item != null) {
                        // uid 에 맞는 진료기록들을 불러오기
                        if(FBAuth.getUid() == item.uid){
                            recordList.add(item)
                            recordkeyList.add(dataModel.key.toString())

//                            Log.d("그냥기록 개수", recordList.size.toString())


                            // 날짜가 같은게 있다면, 따로 불러와서 todayRcRv 에도!! 추가해야함
                            if(item.date == nowDate){
                                if (todayItem != null) {
                                    todayRecordList.add(todayItem)
                                    todayRecordKeyList.add(dataModel.key.toString())
                                }
//                                Log.d("오늘기록 개수",todayRecordList.size.toString())
//                                Log.d("아이템", item.toString())

                            }

                        }
                    }

                }
                recordkeyList.reverse()
                recordList.reverse()
                RecordListAdapter.notifyDataSetChanged()
                TodayRecordAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("recordListTest", "취소되었습니다.", databaseError.toException())
            }
        }
        FBRef.recordRef.addValueEventListener(postListener)
    }


}