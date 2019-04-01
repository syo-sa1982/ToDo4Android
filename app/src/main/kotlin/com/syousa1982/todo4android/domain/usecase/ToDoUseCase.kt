package com.syousa1982.todo4android.domain.usecase

import com.syousa1982.todo4android.data.repository.ITaskListRepository
import com.syousa1982.todo4android.domain.Result
import com.syousa1982.todo4android.domain.model.Task
import com.syousa1982.todo4android.domain.model.TaskList
import io.reactivex.Flowable

interface IToDoUseCase {

    fun getTaskLists(): Flowable<Result<List<TaskList>>>

    fun getTasks(): Flowable<Result<List<Task>>>
}

class ToDoUseCase(private val repository: ITaskListRepository) : IToDoUseCase {
    override fun getTaskLists(): Flowable<Result<List<TaskList>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTasks(): Flowable<Result<List<Task>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}