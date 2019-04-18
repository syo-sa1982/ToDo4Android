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

    /**
     * タスクリストのListを取得
     */
    fun getTaskLists(): Flowable<Result<List<TaskList>>>

    /**
     * タスクのListを取得
     */
    fun getTasks(taskListId: Int): Flowable<Result<List<Task>>>

    /**
     * タスクリストを追加
     *
     * @param name
     */
    fun addTaskList(name: String): Flowable<Result<Boolean>>

    /**
     * タスクを追加
     *
     * @param name
     */
    fun addTask(name: String): Flowable<Result<Boolean>>

    /**
     * タスクリストを削除
     * memo:タスクリスト削除時はリレーションしているタスクも削除する
     *
     * @param id
     */
    fun removeTaskList(id: Int): Flowable<Result<Boolean>>

    /**
     * タスクを削除
     *
     * @param id
     */
    fun removeTask(id: Int): Flowable<Result<Boolean>>

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

    override fun addTaskList(name: String): Flowable<Result<Boolean>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addTask(name: String): Flowable<Result<Boolean>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeTaskList(id: Int): Flowable<Result<Boolean>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeTask(id: Int): Flowable<Result<Boolean>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}