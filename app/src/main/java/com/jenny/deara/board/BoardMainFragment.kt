package com.jenny.deara.board

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.board.comment.CommentModel
import com.jenny.deara.board.comment.MyCommentListAdapter
import com.jenny.deara.databinding.FragmentBoardMainBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

class BoardMainFragment : Fragment() {

    private lateinit var binding: FragmentBoardMainBinding

    lateinit var BoardListAdapter: BoardListAdapter
    lateinit var myCommentListAdapter: MyCommentListAdapter

    var boardList = mutableListOf<BoardModel>()
    var boardkeyList = mutableListOf<String>()
    var searchList = mutableListOf<BoardModel>()
    var searchKeyList = mutableListOf<String>()
    var commentList = mutableListOf<CommentModel>()
    var menu: String = "boardList"
    var sort: String = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_main, container, false)

        initRecycler()
        getFBBoardData()
        binding.searchBtn.setColorFilter(Color.parseColor("#F3F3F3"))

        binding.myPageBtn.setOnClickListener {
//            val intent = Intent(context, MyPageActivity::class.java)
//            startActivity(intent)
            val intent = Intent(context, BoardInsideActivity::class.java)
            startActivity(intent)
        }

        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }


        //// menu ////
        binding.boardList.setOnClickListener {
            setText()
            binding.boardSearch.visibility = View.VISIBLE
            binding.menu2.visibility = View.VISIBLE
            binding.writeBtn.visibility = View.VISIBLE
            binding.boardList.background = context?.let { getDrawable(it, R.drawable.bottom_edge_bold) }
            binding.boardList.setTextColor(Color.BLACK)
            menu = "boardList"
            sort = "All"
            getFBBoardData()
            initRecycler()
        }

        binding.boardAlarm.setOnClickListener {
            setText()
//            menu = "boardAlarm"
//            getFBBoardData(menu)
            binding.boardSearch.visibility = View.GONE
            binding.menu2.visibility = View.GONE
            binding.boardAlarm.background = context?.let { getDrawable(it, R.drawable.bottom_edge_bold) }
            binding.boardAlarm.setTextColor(Color.BLACK)
        }

        binding.myBoard.setOnClickListener {
            setText()
            binding.boardSearch.visibility = View.GONE
            binding.menu2.visibility = View.GONE
            binding.writeBtn.visibility = View.GONE
            binding.myBoard.background = context?.let { getDrawable(it, R.drawable.bottom_edge_bold) }
            binding.myBoard.setTextColor(Color.BLACK)
            menu = "myBoard"
            getFBBoardData()
            initRecycler()
        }

        binding.myComment.setOnClickListener {
            setText()
            menu = "myComment"
            initCommentRecycler()
            getCommentData()
            binding.boardSearch.visibility = View.GONE
            binding.menu2.visibility = View.GONE
            binding.writeBtn.visibility = View.GONE
            binding.myComment.background = context?.let { getDrawable(it, R.drawable.bottom_edge_bold) }
            binding.myComment.setTextColor(Color.BLACK)
        }


        // sort //
        binding.sortAll.setOnClickListener {
            sort = "All"
            setSortText()
            binding.sortAll.background = context?.let { getDrawable(it, R.drawable.radius_purple_right) }
            binding.sortAll.setTextColor(R.color.purple2)
            getFBBoardData()
        }

        binding.sortFree.setOnClickListener {
            sort = "Free"
            setSortText()
            binding.sortFree.background = context?.let { getDrawable(it, R.drawable.radius_purple_right) }
            binding.sortFree.setTextColor(R.color.purple2)
            getFBBoardData()
        }

        binding.sortQuestion.setOnClickListener {
            sort = "Question"
            setSortText()
            binding.sortQuestion.background = context?.let { getDrawable(it, R.drawable.radius_purple_right) }
            binding.sortQuestion.setTextColor(R.color.purple2)
            getFBBoardData()
        }

        binding.sortInfor.setOnClickListener {
            sort = "Information"
            setSortText()
            binding.sortInfor.background = context?.let { getDrawable(it, R.drawable.radius_purple_right) }
            binding.sortInfor.setTextColor(R.color.purple2)
            getFBBoardData()
        }

        binding.searchET.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {

                }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (binding.searchET.text.toString() == ""){
                        binding.searchBtn.setColorFilter(Color.parseColor("#F3F3F3"))
                        BoardListAdapter.setItems(boardList, boardkeyList)
                    }else{
                        binding.searchBtn.setColorFilter(Color.parseColor("#B8B8F0"))
                        search(binding.searchET.text.toString())
                    }
                }
            }
        )

        binding.delSearchBtn.setOnClickListener {
            binding.searchET.text = null
        }

        binding.searchBtn.setOnClickListener {
            if (TextUtils.isEmpty(binding.searchET.text)){
                Log.d("searchTest", "입력 값 없음")
            }else{
                Log.d("searchTest", "not null + " + binding.searchET.text.toString())
                search(binding.searchET.text.toString())
            }
        }
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        BoardListAdapter = BoardListAdapter(requireContext(), boardkeyList, menu)

        val rv : RecyclerView = binding.rvBoard
        rv.adapter= BoardListAdapter

//        boardList.add(BoardModel("bb","bb","아이디","시간","자유"))
//        boardList.add(BoardModel("aaa","bb","아이디","시간","자유"))
//        boardkeyList.add("sdf")
//        boardkeyList.add("sdfs")

        BoardListAdapter.datas = boardList
        BoardListAdapter.myComments = commentList
        BoardListAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initCommentRecycler() {
        myCommentListAdapter = MyCommentListAdapter(requireContext())

        val rv : RecyclerView = binding.rvBoard
        rv.adapter= myCommentListAdapter

//        commentList.add(CommentModel("댓글 내용입니다.","작성자","시간","부모키"))
//        commentList.add(CommentModel("댓글 내용입니다22.","작성자22","시간22","부모키22"))
//        boardkeyList.add("sdf")
//        boardkeyList.add("sdfs")

        myCommentListAdapter.datas = commentList
        myCommentListAdapter.notifyDataSetChanged()
    }

    //파이어베이스 데이터 불러오기 _ 글 목록
    private fun getFBBoardData(){

        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardList.clear()
                boardkeyList.clear()

                for (dataModel in dataSnapshot.children) {

                    Log.d("boardListTest", dataModel.toString())

                    val item = dataModel.getValue(BoardModel::class.java)
                    if (menu == "boardList"){
                        if (item != null) {
                            when (sort) {
                                "All" -> {
                                    boardList.add(item)
                                    boardkeyList.add(dataModel.key.toString())
                                }
                                "Free" -> {
                                    if (item.sort == "자유"){
                                        boardList.add(item)
                                        boardkeyList.add(dataModel.key.toString())
                                    }
                                }
                                "Question" -> {
                                    if (item.sort == "질문"){
                                        boardList.add(item)
                                        boardkeyList.add(dataModel.key.toString())
                                    }
                                }
                                else -> {
                                    if (item.sort == "정보"){
                                        boardList.add(item)
                                        boardkeyList.add(dataModel.key.toString())
                                    }
                                }
                            }
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

    // 댓글 가져오기
    private fun getCommentData(){
        menu = "myComment"
        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //boardList.clear()
                commentList.clear()
                //commentKeyList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    commentList.add(item!!)
                    //commentKeyList.add(dataModel.key.toString())
                    //Log.d("getCommentLog", "{${commentKeyList}}")

                    //getCommentReply(dataModel.key.toString()) //대댓글 리스트에 내용을 담는다.
                }
                BoardListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("getCommentData", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.addValueEventListener(postListener)
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

    private fun setSortText(){
        binding.sortAll.setTextColor(Color.parseColor("#AFAFAF"))
        binding.sortFree.setTextColor(Color.parseColor("#AFAFAF"))
        binding.sortQuestion.setTextColor(Color.parseColor("#AFAFAF"))
        binding.sortInfor.setTextColor(Color.parseColor("#AFAFAF"))

        binding.sortAll.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.sortFree.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.sortQuestion.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.sortInfor.setBackgroundColor(Color.parseColor("#00ff0000"))
    }

    // 검색
    private fun search(searchText: String){
        searchList.clear()
        searchKeyList.clear()

        for (a in 0 until boardList.size) {
            if (boardList[a].title.lowercase().contains(searchText) || boardList[a].content.lowercase().contains(searchText)) {
                searchList.add(boardList[a])
                searchKeyList.add(boardkeyList[a])
            }
            BoardListAdapter.setItems(searchList, searchKeyList)
        }
    }
}