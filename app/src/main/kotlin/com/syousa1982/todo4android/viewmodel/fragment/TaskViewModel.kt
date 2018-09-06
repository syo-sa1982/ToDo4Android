package com.syousa1982.todo4android.viewmodel.fragment

import android.databinding.Bindable
import com.syousa1982.todo4android.BR
import com.syousa1982.todo4android.model.entity.Task
import com.syousa1982.todo4android.viewmodel.BaseListViewModel
import com.syousa1982.todo4android.viewmodel.BaseViewModel

/**
 * タスク一覧画面 ViewModel
 */
class TaskViewModel : BaseViewModel()

/**
 * タスク一覧画面 リストアイテム ViewModel
 */
class TaskListViewModel : BaseListViewModel(){

    override fun getItemId(): String? {
        return task?.id
    }

    /**
     * タスク
     */
    @Bindable
    var task: Task? = null
    set(value) {
        field = value
        notifyPropertyChanged(BR._all)
    }
}