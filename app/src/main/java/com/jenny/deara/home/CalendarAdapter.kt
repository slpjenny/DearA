package com.jenny.deara.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(private val dayList: ArrayList<Date>) :
    RecyclerView.Adapter<CalendarAdapter.ItemViewHolder>() {

    interface ItemClick {
        fun onClick(view : View, position : Int)
    }
    var itemClick : ItemClick? = null

    var selectedPosition = -1
    var oldPosition = 0

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
//        Log.d(TAG, "monthDate : " + monthDate)

        // 초기화
        var dateCalendar = Calendar.getInstance()
        // 현재 월만 받아오기
        var monthNo = dateCalendar.get(Calendar.MONTH)+1


        // 날짜 캘린더에 담기
        dateCalendar.time = monthDate
//        Log.d(TAG, "dataCalendar.time : " + dateCalendar.time)

        // 캘린더값 날짜 변수에 담기
        var dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH)

//        Log.d(TAG, "dayNo : " + dayNo)

        holder.dayText.text = dayNo.toString()

        // 넘어온 날짜
        var iYear = dateCalendar.get(Calendar.YEAR)
        var iMonth = dateCalendar.get(Calendar.MONTH) + 1
        var iDay = dateCalendar.get(Calendar.DAY_OF_MONTH)


        // 현재 날짜
        var selectYear = CalendarUtil.selectedDate.get(Calendar.YEAR)
        var selectMonth = CalendarUtil.selectedDate.get(Calendar.MONTH)+1
        var selectDay = CalendarUtil.selectedDate.get(Calendar.DAY_OF_MONTH)


        // 넘어온 날짜와 현재 날짜 비교
        if (iYear == selectYear && iMonth == selectMonth){  // 같다면 진한 색상
            holder.dayText.setTextColor(Color.BLACK)
//            Log.d(TAG, "iYear : " + iYear)
//            Log.d(TAG, "iMonth : " + iMonth)
//            Log.d(TAG, "selectYear : " + selectYear)
//            Log.d(TAG, "selectMonth : " + selectMonth)
            // 현재 날짜 밑줄
            if(selectMonth == monthNo && selectDay == dayNo) {
                //holder.itemView.setBackgroundResource(R.drawable.today_background)
                holder.dayText.setTextColor(Color.parseColor("#ff0000"))
//                Log.d(TAG, "Calendar Month : " + dateCalendar.get(Calendar.MONTH) + 1)
//                Log.d(TAG, "selectMonth : " + selectMonth)
//                Log.d(TAG, "monthNo : " + monthNo)
//                Log.d(TAG, "selectDay : " + selectDay)
//                Log.d(TAG, "dayNo : " + dayNo)
            }

        } else{ // 다르다면 하얀 색상
            holder.dayText.setTextColor(Color.parseColor("#ffffff"))
        }


        // 날짜 선택할 시 밑줄
        if (itemClick != null) {
            holder.itemView.setOnClickListener{ view ->
                itemClick?.onClick(view, position)

                if(selectedPosition != position){
                    holder.itemView.setBackgroundResource(R.drawable.today_background)
                    Log.d(TAG, "selectedPosition1 : " + selectedPosition)
                    Log.d(TAG, "position1 : " + position)
                    Log.d(TAG, "oldposition1 : " + oldPosition)

                }

//                notifyItemChanged(oldPosition, null)
//                notifyItemChanged(selectedPosition, null)
                Log.d(TAG, "selectedPositon2 : " + selectedPosition)
                Log.d(TAG, "oldposition2 : " + oldPosition +"\n")
                Log.d(TAG, "notify old : " + notifyItemChanged(oldPosition).toString())
                Log.d(TAG, "notify select : " + notifyItemChanged(selectedPosition).toString())


                //oldPosition = selectedPosition
                selectedPosition=position


            }

            if(selectedPosition == selectedPosition){
                holder.itemView.setBackgroundColor(Color.parseColor("#00ff0000"))

            }

        }


    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    }