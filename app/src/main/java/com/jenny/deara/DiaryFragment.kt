package com.jenny.deara

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.databinding.FragmentDiaryBinding
import com.jenny.deara.diary.*
import com.jenny.deara.utils.FBRef

class DiaryFragment(override var iMonth: Int) : Fragment(),  DatePickerFragment.DatePickerListener {

    private lateinit var binding: FragmentDiaryBinding

    lateinit var DiaryListAdapter: DiaryListAdapter
    val datas = mutableListOf<DiaryData>()
    val stringMonth : List<String> = listOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false)

        initRecycler()
        getFBDiaryData()
        showMonth(iMonth)


        //마이페이지 버튼
        binding.myPageBtn.setOnClickListener {
            val intent = Intent(context, MyPageActivity::class.java)
            startActivity(intent)
        }

        //일기 작성하기
        binding.diaryplusBtn.setOnClickListener{
            val intent = Intent(context, DiaryEditActivity::class.java)
            startActivity(intent)
        }

        //날짜 선택하기
        binding.datapicker.setOnClickListener {
            showDatePickerDialog(iMonth)
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        DiaryListAdapter = DiaryListAdapter(requireContext(), diarykeyList)

        binding.rvDiary.adapter = DiaryListAdapter

        //test 데이터
<<<<<<< HEAD
//        FBRef.diaryRef
//            .push()
//            .setValue(DiaryData
//                ("맛있는 음식을 먹어서 행복했다",
//                "오늘도 늦잠을 자서 또 택시를 탔다.",
//                "내일은 꼭 제시간에 일어나서 택시 타지 않기",
//                "[랜덤질문] 가장 좋았던 여행은 어떤 여행인가요?",
//                "작년에 친구들과 갔던 제주도 여행",
//                "ver1",
//                ))

        DiaryListAdapter.datas = diaryList
        DiaryListAdapter.notifyDataSetChanged()
    }

    // 날짜 선택 다이얼로그 띄우기
    private fun showDatePickerDialog(iMonth: Int, iYear: Int){
        val dialog = DatePickerFragment(iMonth, iYear)
=======
        datas.apply {
            add(DiaryData(
                "좋았던일",
                "오늘은 맛있는 음식을 많이 먹어서 좋았다.",
                "안 좋았던 일",
                "늦잠을 자서 지각할 뻔",
                "나의 다짐",
                "내일은 꼭 일찍 일어나기",
                "[랜덤질문] 가장 좋았던 여행은 어떤 여행인가요?",
                "작년 여름에 가족들과 갔던 부산여행")
            )
            add(DiaryData(
                "좋았던일",
                "일찍 일어났다",
                "안 좋았던 일",
                "늦게 자서 피곤해",
                "나의 다짐",
                "내일은 꼭 일찍 자기",
                "[랜덤질문] 가장 좋았던 여행은 어떤 여행인가요?",
                "작년 여름에 가족들과 갔던 부산여행")
            )
            DiaryListAdapter.datas = datas
            DiaryListAdapter.notifyDataSetChanged()

        }
    }

    private fun showDatePickerDialog(iMonth: Int){
        val dialog = DatePickerFragment(iMonth)
>>>>>>> 1bdcbd3b4cbde86ddcf170596b63b58565232df3
        dialog.show(childFragmentManager, "DatePickerDialog")
    }

    //달 띄우기
    private fun showMonth(iMonth: Int){
        for (i in 1 .. 12){
            if (iMonth == i){
                binding.month.text = stringMonth[i-1]
            }
        }
    }
<<<<<<< HEAD

    //년도 띄우기
    private fun showYear(iYear: Int){
        for (i in 0 .. 30){
            if (iYear == i+2000){
                binding.year.text = intYear[i].toString()
            }
        }
    }

    //파이어베이스 데이터 불러오기
    private fun getFBDiaryData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                diaryList.clear()

                for (dataModel in dataSnapshot.children) {

                    Log.d("diaryListtest", dataModel.toString())

                    val item = dataModel.getValue(DiaryData::class.java)
                    diaryList.add(item!!)
                    diarykeyList.add(dataModel.key.toString())

                }
                diarykeyList.reverse()
                diaryList.reverse()
                DiaryListAdapter.notifyDataSetChanged()

                Log.d("supportListtest", diaryList.toString())


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("supportListtest", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.diaryRef.addValueEventListener(postListener)
    }
}
=======
}
>>>>>>> 1bdcbd3b4cbde86ddcf170596b63b58565232df3
