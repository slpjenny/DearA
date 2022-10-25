package com.jenny.deara.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.utils.CalendarUtil
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(private val context: Context,
                      private val dayList: ArrayList<Date>,
                      private val todoList : ArrayList<ToDoData>,
                      val todokeyList : MutableList<String>) :
    RecyclerView.Adapter<CalendarAdapter.ItemViewHolder>() {


    interface ItemClick {
        fun onClick(view : View, position : Int)
    }

    var itemClick : ItemClick? = null

    var selectedPosition = -1

    private val TAG = CalendarAdapter::class.java.simpleName

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

            val dayText : TextView = itemView.findViewById(R.id.dayText)

    }

    // 화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_item, parent, false)

        return ItemViewHolder(view)
    }

    // 데이터 설정
    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        var monthDate = dayList[holder.adapterPosition]

        // 초기화
        var dateCalendar = Calendar.getInstance()
        // 현재 월만 받아오기
        var monthNo = dateCalendar.get(Calendar.MONTH)+1


        // 날짜 캘린더에 담기
        dateCalendar.time = monthDate

        // 캘린더값 날짜 변수에 담기
        var dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH)

        holder.dayText.text = dayNo.toString()

        // 넘어온 날짜
        var iYear = dateCalendar.get(Calendar.YEAR)
        var iMonth = dateCalendar.get(Calendar.MONTH) + 1
        var iDay = dateCalendar.get(Calendar.DAY_OF_MONTH)

        Log.d(TAG, "iMonth : " + iMonth)
        Log.d(TAG, "iDay : " + iDay)
        Log.d(TAG, "iYear : " + iYear)

        // 현재 날짜
        var selectYear = CalendarUtil.selectedDate.get(Calendar.YEAR)
        var selectMonth = CalendarUtil.selectedDate.get(Calendar.MONTH)+1
        var selectDay = CalendarUtil.selectedDate.get(Calendar.DAY_OF_MONTH)
        Log.d(TAG, "selectYear : " + selectYear)
        Log.d(TAG, "selecMonth : " + selectMonth)
        Log.d(TAG, "selectDay : " + selectDay)


        // 넘어온 날짜와 현재 날짜 비교
        if (iYear == selectYear && iMonth == selectMonth){  // 같다면 검정 색상
            holder.dayText.setTextColor(Color.BLACK)

            // 날짜 선택할 시 밑줄
            if (itemClick != null) {
                holder.itemView.setOnClickListener { view ->
                    itemClick?.onClick(view, position)

                    var month = iMonth.toString()
                    var date = holder.dayText.text.toString()

                    if (selectedPosition != position) {
                        //getFBTodoData(month, date)
                        holder.itemView.setBackgroundResource(R.drawable.today_background)
                    }

                    notifyItemChanged(selectedPosition)
                    selectedPosition = position

                }

            }


            if(selectedPosition == selectedPosition){
                holder.itemView.setBackgroundColor(Color.parseColor("#00ff0000"))

            }
                // 현재 날짜 빨간색
            if(selectMonth == monthNo && selectDay == dayNo) {
                //holder.itemView.setBackgroundResource(R.drawable.today_background)
                holder.dayText.setTextColor(Color.parseColor("#ff0000"))

            }

        } else{ // 다르다면 하얀 색상
            holder.itemView.setBackgroundColor(Color.parseColor("#00ff0000"))
            holder.dayText.setTextColor(Color.parseColor("#ffffff"))
        }


    }

    override fun getItemCount(): Int {
        return dayList.size
    }


    // firebase에 저장된 투두리스트 목록 불러오기
    fun getFBTodoData(Month : String, date : String){

        val position = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                todoList.clear()

                for (dataModel in dataSnapshot.children){
                    Log.d("todoList", dataModel.toString())

                    val item = dataModel.getValue(ToDoData::class.java)
                    if (FBAuth.getUid() == item!!.uid){
                        if(item!!.month == Month && item!!.date == date ){
                            todoList.add(item)
                            Log.d (TAG, "monthhh : " + item!!.month)
                            Log.d (TAG, "monthhhh : " + Month)
                            Log.d (TAG, "datehhh : " + item!!.date)
                            Log.d (TAG, "datehhhh : " + date)
                        }
                    }

                    todokeyList.add(dataModel.key.toString())
                    todoList
                    Log.d(TAG, "todoList : " + todoList)

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("getTodoFB", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.todoRef.addValueEventListener(position)
    }
    }