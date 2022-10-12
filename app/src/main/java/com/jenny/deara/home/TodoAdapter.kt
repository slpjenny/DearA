package com.jenny.deara.home

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.databinding.TodolistviewItemBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

private val TAG = TodoAdapter::class.java.simpleName

class TodoAdapter(val items: ArrayList<ToDoModel>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewholder {

        val binding =  TodolistviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewholder(binding).also { holder ->
            binding.clearCheck.setOnCheckedChangeListener{ buttonView, isChecked ->
                items.getOrNull(holder.adapterPosition)?.clear = isChecked
            }
        }

    }


    override fun onBindViewHolder(holder: TodoViewholder, position: Int) {

        holder.bind(items[position])

        // 투두리스트 아이템 삭제버튼 클릭 시
        holder.binding.tododel.setOnClickListener {
            items.removeAt(position)
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size);
        }

    }

    override fun getItemCount(): Int = items.size

    class TodoViewholder(val binding: TodolistviewItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(todo : ToDoModel) {

            binding.todowrite.text = todo.write
            binding.clearCheck.isChecked = todo.clear

//            val time = FBAuth.getTimeDiary()
//            val uid = FBAuth.getUid()
//
//            FBRef.todoRef
//                .push()
//                .setValue(ToDoData(binding.todowrite.toString(), binding.clearCheck, time, uid))


        }

    }

}