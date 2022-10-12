package com.jenny.deara.board

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentBoardMainBinding
import com.jenny.deara.diary.DiaryData
import com.jenny.deara.diary.DiaryListAdapter

class BoardMainFragment : Fragment() {

    private lateinit var binding: FragmentBoardMainBinding

    lateinit var BoardListAdapter: BoardListAdapter

    val boardList = mutableListOf<BoardModel>()
    val boardkeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_main, container, false)


        initRecycler()


        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        BoardListAdapter = BoardListAdapter(requireContext())

        val rv : RecyclerView = binding.rvBoard
        rv.adapter= BoardListAdapter

        boardList.add(BoardModel("sdf","sdf","sdf","sdf"))

        //random 질문 데이터 삽입 test
//        FBRef.randomQuestionRef.push().setValue("지금 가장 보고 싶은 사람은 누구인가요?")

        BoardListAdapter.datas = boardList
        BoardListAdapter.notifyDataSetChanged()
    }
}