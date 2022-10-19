package com.jenny.deara.home

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R
import com.jenny.deara.databinding.TodolistviewItemBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

private val TAG = TodoAdapter::class.java.simpleName

class TodoAdapter(val context : Context, val items: ArrayList<ToDoData>,val todokeyList : MutableList<String>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewholder {

//        val binding =  TodolistviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return TodoViewholder(binding).also { holder ->
//            binding.clearCheck.setOnCheckedChangeListener{ buttonView, isChecked ->
//                items.getOrNull(holder.adapterPosition)?.clear = isChecked
//            }
//        }

        val view = LayoutInflater.from(context).inflate(R.layout.todolistview_item, parent,false)
        return TodoViewholder(view)

    }


    override fun onBindViewHolder(holder: TodoViewholder, position: Int) {
        holder.bind(items[position])

        // 투두리스트 아이템 삭제버튼 클릭 시
//        holder.binding.tododel.setOnClickListener {
//            items.removeAt(position)
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, items.size);
//        }

    }

    override fun getItemCount(): Int = items.size

   inner class TodoViewholder(view : View) : RecyclerView.ViewHolder(view){

       private val todowrite : TextView = itemView.findViewById(R.id.todowrite)
       private val clearcheck : CheckBox = itemView.findViewById(R.id.clear_check)
       private var tododel : ImageButton = itemView.findViewById(R.id.tododel)

        fun bind(todo : ToDoData) {

            todowrite.text = todo.todo
            clearcheck.isChecked = todo.check


            tododel.setOnClickListener {

//                Log.d(TAG, "keyaddno : " + key)
//                FBRef.todoRef.child(key).removeValue()
                //Log.d(TAG, "useruid : " +  FBRef.todoRef.child("todo").child("uid"))


                items.removeAt(position)

                notifyItemRemoved(position)
                notifyItemRangeChanged(position, items.size)

            }

            Log.d(TAG, "todokeyList : " + todokeyList)


        }

    }

}