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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentBoardMainBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

class BoardMainFragment : Fragment() {

    private lateinit var binding: FragmentBoardMainBinding

//    private lateinit var auth: FirebaseAuth
//    private lateinit var database: DatabaseReference

    lateinit var BoardListAdapter: BoardListAdapter

    val boardList = mutableListOf<BoardModel>()
    val boardkeyList = mutableListOf<String>()
    var menu: String = "boardList"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_main, container, false)

        initRecycler()
        getFBBoardData(menu)

        binding.myPageBtn.setOnClickListener {
            val intent = Intent(context, MyPageActivity::class.java)
            startActivity(intent)
        }

        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        binding.boardList.setOnClickListener {
            setText()
            binding.boardSearch.visibility = View.VISIBLE
            binding.boardList.background = context?.let { getDrawable(it, R.drawable.bottom_edge) }
            binding.boardList.setTextColor(Color.BLACK)
            menu = "boardList"
            getFBBoardData(menu)
        }

        binding.boardAlarm.setOnClickListener {
            setText()
//            menu = "boardAlarm"
//            getFBBoardData(menu)
            binding.boardSearch.visibility = View.GONE
            binding.boardAlarm.background = context?.let { getDrawable(it, R.drawable.bottom_edge) }
            binding.boardAlarm.setTextColor(Color.BLACK)
        }

        binding.myBoard.setOnClickListener {
            setText()
            binding.boardSearch.visibility = View.GONE
            binding.myBoard.background = context?.let { getDrawable(it, R.drawable.bottom_edge) }
            binding.myBoard.setTextColor(Color.BLACK)
            menu = "myBoard"
            getFBBoardData(menu)
        }

        binding.myComment.setOnClickListener {
            setText()
//            menu = "myComment"
//            getFBBoardData(menu)
            binding.boardSearch.visibility = View.GONE
            binding.myComment.background = context?.let { getDrawable(it, R.drawable.bottom_edge) }
            binding.myComment.setTextColor(Color.BLACK)
        }


        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        BoardListAdapter = BoardListAdapter(requireContext(), boardkeyList)

        val rv : RecyclerView = binding.rvBoard
        rv.adapter= BoardListAdapter

        BoardListAdapter.datas = boardList
        BoardListAdapter.notifyDataSetChanged()
    }

    //파이어베이스 데이터 불러오기 _ 글 목록
    private fun getFBBoardData(menu: String){

        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardList.clear()
                boardkeyList.clear()

                for (dataModel in dataSnapshot.children) {

                    Log.d("boardListTest", dataModel.toString())

                    val item = dataModel.getValue(BoardModel::class.java)
                    if (menu == "boardList"){
                        // 글 목록
                        if (item != null) {
                            boardList.add(item)
                            boardkeyList.add(dataModel.key.toString())
                        }
                    }else if( menu == "boardAlarm"){
                        // 알람 목록
                    }else if(menu == "myBoard"){
                        // 내가 쓴 글
                        if (item != null) {
                            if (FBAuth.getUid() == item.uid){
                                boardList.add(item)
                                boardkeyList.add(dataModel.key.toString())
                            }
                        }
                    }else{
                        //내가 쓴 댓글
                    }
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

    private fun setText(){
        binding.boardList.setTextColor(Color.parseColor("#CFCFCF"))
        binding.boardAlarm.setTextColor(Color.parseColor("#CFCFCF"))
        binding.myBoard.setTextColor(Color.parseColor("#CFCFCF"))
        binding.myComment.setTextColor(Color.parseColor("#CFCFCF"))

        binding.boardList.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.boardAlarm.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.myBoard.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.myComment.setBackgroundColor(Color.parseColor("#00ff0000"))
    }
}