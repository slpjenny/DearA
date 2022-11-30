package com.jenny.deara.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.content.Intent.getIntentOld
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
                      val todokeyList : MutableList<String>,
                    val percent : Int) :
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

        Log.d(TAG, "percent : " + percent)
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
            //holder.itemView.setBackgroundResource(R.drawable.background_todolist_green)



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

            var day = dayNo.toString()
            if(day.length == 1){
                day = "0"+day
            }

            val position = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (dataModel in dataSnapshot.children) {
                        Log.d("todoList", dataModel.toString())

                        val item = dataModel.getValue(ToDoData::class.java)
                        if (FBAuth.getUid() == item!!.uid) {


                            if (item!!.year == iYear.toString() && item!!.month == iMonth.toString() && item!!.date == day) {

                                var percent = item!!.percent

                                Log.d(TAG, "firebaseper : " + percent)

                                if (percent>=25 && percent < 50){
                                    holder.itemView.setBackgroundResource(R.drawable.background_todolist_pink)
                                }
                                else if(percent>=50 && percent <75){
                                    holder.itemView.setBackgroundResource(R.drawable.background_todolist_yellow)
                                }
                                else if (percent>=75 && percent<=100){
                                    holder.itemView.setBackgroundResource(R.drawable.background_todolist_green)
                                }


                                Log.d(TAG, "percentpercent : " + percent)


                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
            FBRef.todoRef.addValueEventListener(position)



        } else{ // 다르다면 하얀 색상
            holder.itemView.setBackgroundColor(Color.parseColor("#00ff0000"))
            holder.dayText.setTextColor(Color.parseColor("#00ff0000"))
        }


    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    }