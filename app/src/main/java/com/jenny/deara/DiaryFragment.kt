package com.jenny.deara

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
import com.jenny.deara.databinding.FragmentDiaryBinding
import com.jenny.deara.diary.*
import com.jenny.deara.utils.FBRef

class DiaryFragment(var iMonth: Int, var iYear: Int) : Fragment() {

    private lateinit var binding: FragmentDiaryBinding

    lateinit var DiaryListAdapter: DiaryListAdapter

    val diaryList = mutableListOf<DiaryData>()
    val diarykeyList = mutableListOf<String>()

    // 날짜 변수
    val stringMonth : List<String> = listOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
    val intYear = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false)

        // init setting! //
        for (i in 2000 .. 2030){
            intYear.add(i)
        }
        initRecycler()
        showMonth(iMonth)
        showYear(iYear)
        getFBDiaryData()



        //마이페이지 버튼
        binding.myPageBtn.setOnClickListener {
            val intent = Intent(context, MyPageActivity::class.java)
            startActivity(intent)
        }

        //일기 작성하기
        binding.diaryplusBtn.setOnClickListener{
            val intent = Intent(context, DiaryWriteActivity::class.java)
            startActivity(intent)
        }

        //날짜 선택하기
        binding.datapicker.setOnClickListener {
            showDatePickerDialog(iMonth, iYear)
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        DiaryListAdapter = DiaryListAdapter(requireContext(), diarykeyList)

        val rv : RecyclerView = binding.rvDiary
        rv.adapter= DiaryListAdapter

//        random 질문 데이터 삽입 test
//        FBRef.randomQuestionRef.push().setValue(RandomQuestionModel("최근 가장 몰입했던 일은 무엇인가요?"))

        DiaryListAdapter.datas = diaryList
        DiaryListAdapter.notifyDataSetChanged()
    }

    // 날짜 선택 다이얼로그 띄우기
    private fun showDatePickerDialog(iMonth: Int, iYear: Int){
        val dialog = DatePickerFragment(iMonth, iYear)
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

    // 년도 띄우기
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
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                diaryList.clear()

                for (dataModel in dataSnapshot.children) {

                    Log.d("diaryListTest", dataModel.toString())

                    val item = dataModel.getValue(DiaryData::class.java)
                    // 선택한 날짜 일기 리스트 띄우기
                    if(item!!.month == iMonth && item.year == iYear){
                        diaryList.add(item)
                        diarykeyList.add(dataModel.key.toString())
                    }

                }
                diarykeyList.reverse()
                diaryList.reverse()
                DiaryListAdapter.notifyDataSetChanged()

                Log.d("diaryListTest", diaryList.toString())


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("diaryListTest", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.diaryRef.addValueEventListener(postListener)
    }
}