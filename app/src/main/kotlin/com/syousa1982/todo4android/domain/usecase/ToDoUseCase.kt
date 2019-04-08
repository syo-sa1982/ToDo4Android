package com.syousa1982.todo4android.domain.usecase

import com.syousa1982.todo4android.data.repository.ITaskListRepository
import com.syousa1982.todo4android.domain.Result
import com.syousa1982.todo4android.domain.model.Task
import com.syousa1982.todo4android.domain.model.TaskList
import com.syousa1982.todo4android.domain.translator.TaskListTranslator
import com.syousa1982.todo4android.util.extention.toResult
import com.syousa1982.todo4android.util.rx.SchedulerProvider
import io.reactivex.Flowable

interface IToDoUseCase {

    fun getTaskLists(): Flowable<Result<List<TaskList>>>

    fun getTasks(taskListId: Int): Flowable<Result<List<Task>>>
}

class ToDoUseCase(private val repository: ITaskListRepository,
                  private val schedulerProvider: SchedulerProvider) : IToDoUseCase {
    override fun getTaskLists(): Flowable<Result<List<TaskList>>> {
        return repository.loadTaskListAndTasksByDB()
            .map {
                TaskListTranslator.toTodoLists(it)
            }
            .toResult(schedulerProvider)
    }

    override fun getTasks(taskListId: Int): Flowable<Result<List<Task>>> {
        return repository.loadTaskListAndTasksByDB(taskListId.toString())
            .map {
                TaskListTranslator.toTodoList(it).tasks
            }
            .toResult(schedulerProvider)
    }
}