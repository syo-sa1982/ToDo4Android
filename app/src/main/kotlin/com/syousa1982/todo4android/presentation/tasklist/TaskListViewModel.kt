package com.syousa1982.todo4android.presentation.tasklist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.syousa1982.todo4android.domain.Result
import com.syousa1982.todo4android.domain.model.TaskList
import com.syousa1982.todo4android.domain.usecase.IToDoUseCase
import com.syousa1982.todo4android.presentation.BaseViewModel
import com.syousa1982.todo4android.util.extention.className
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

/**
 * タスクリスト一覧 ViewModel
 *
 * @param todoUseCase
 */
class TaskListViewModel(private val todoUseCase: IToDoUseCase) : BaseViewModel() {

    /**
     * タスクリスト一覧
     */
    val taskLists = MutableLiveData<Result<List<TaskList>>>()

    /**
     * 更新結果
     */
    val updateResult = MutableLiveData<Result<Boolean>>()

    override fun onResume() {
        super.onResume()
        fetchTasks()
    }

    fun fetchTasks() {
        todoUseCase.getTaskLists().subscribeBy(
            onNext = { taskLists.value = it },
            onError = { e -> Log.e(className(), "エラー発生", e) }
        ).addTo(disposable)
    }

    fun delete(taskList: TaskList) {
        todoUseCase.removeTaskListAndTasks(taskList).subscribeBy(
            onNext = { updateResult.value = it },
            onError = { e -> Log.e(className(), "エラー発生", e) }
        ).addTo(disposable)
    }
}