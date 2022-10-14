package com.jenny.deara.board

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentBoardMainBinding
import com.jenny.deara.diary.DiaryData
import com.jenny.deara.diary.DiaryListAdapter
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

class BoardMainFragment : Fragment() {

    private lateinit var binding: FragmentBoardMainBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

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
        getFBBoardData()

        binding.myPageBtn.setOnClickListener {
            val intent = Intent(context, MyPageActivity::class.java)
            startActivity(intent)
        }

        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        binding.boardListText.setOnClickListener {
            setText()
            binding.boardSearch.visibility = View.VISIBLE
            binding.boardListText.background = context?.let { getDrawable(it, R.drawable.bottom_edge) }
            binding.boardListText.setTextColor(Color.BLACK)
        }

        binding.boardAlarmText.setOnClickListener {
            setText()
            binding.boardSearch.visibility = View.GONE
            binding.boardAlarmText.background = context?.let { getDrawable(it, R.drawable.bottom_edge) }
            binding.boardAlarmText.setTextColor(Color.BLACK)
        }

        binding.myBoard.setOnClickListener {
            setText()
            binding.boardSearch.visibility = View.GONE
            binding.myBoard.background = context?.let { getDrawable(it, R.drawable.bottom_edge) }
            binding.myBoard.setTextColor(Color.BLACK)
        }

        binding.myComment.setOnClickListener {
            setText()
            binding.boardSearch.visibility = View.GONE
            binding.myComment.background = context?.let { getDrawable(it, R.drawable.bottom_edge) }
            binding.myComment.setTextColor(Color.BLACK)
        }


        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        BoardListAdapter = BoardListAdapter(requireContext())

        val rv : RecyclerView = binding.rvBoard
        rv.adapter= BoardListAdapter

        BoardListAdapter.datas = boardList
        BoardListAdapter.notifyDataSetChanged()
    }

    //파이어베이스 데이터 불러오기 _ 글 목록
    private fun getFBBoardData(){

        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardList.clear()

                for (dataModel in dataSnapshot.children) {

                    Log.d("boardListTest", dataModel.toString())

                    val item = dataModel.getValue(BoardModel::class.java)
                    if (item != null) {
                        boardList.add(item)
                    }
                    boardkeyList.add(dataModel.key.toString())

                }
                boardkeyList.reverse()
                boardList.reverse()
                BoardListAdapter.notifyDataSetChanged()

                Log.d("boardListTest", boardList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("boardListTest", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }

    //파이어베이스 데이터 불러오기 _ 내 글 목록
//    private fun getFBMyBoardData(){
//
//        val postListener = object : ValueEventListener {
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                boardList.clear()
//
//                for (dataModel in dataSnapshot.children) {
//
//                    Log.d("boardListTest", dataModel.toString())
//
//                    val item = dataModel.getValue(BoardModel::class.java)
//                    // Auth 초기화
//                    auth = Firebase.auth
//
//                    // 실시간 database reference 불러오기
//                    database = Firebase.database.reference
//                    val user = auth.currentUser
//
//                    if (item != null) {
//                        if (item.uid == user.toString()) {
//                            boardList.add(item)
//                        }
//                    }
//                    boardkeyList.add(dataModel.key.toString())
//
//                }
//                boardkeyList.reverse()
//                boardList.reverse()
//                BoardListAdapter.notifyDataSetChanged()
//
//                Log.d("boardListTest", boardList.toString())
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.w("boardListTest", "loadPost:onCancelled", databaseError.toException())
//            }
//        }
//        FBRef.boardRef.addValueEventListener(postListener)
//    }

    private fun setText(){
        binding.boardListText.setTextColor(Color.parseColor("#CFCFCF"))
        binding.boardAlarmText.setTextColor(Color.parseColor("#CFCFCF"))
        binding.myBoard.setTextColor(Color.parseColor("#CFCFCF"))
        binding.myComment.setTextColor(Color.parseColor("#CFCFCF"))

        binding.boardListText.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.boardAlarmText.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.myBoard.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.myComment.setBackgroundColor(Color.parseColor("#00ff0000"))
    }
}