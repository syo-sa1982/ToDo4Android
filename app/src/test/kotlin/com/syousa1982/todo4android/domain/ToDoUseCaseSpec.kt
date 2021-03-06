package com.syousa1982.todo4android.domain

import com.syousa1982.todo4android.data.db.entity.TaskEntity
import com.syousa1982.todo4android.data.db.entity.TaskListAndTasks
import com.syousa1982.todo4android.data.db.entity.TaskListEntity
import com.syousa1982.todo4android.data.repository.ITaskListRepository
import com.syousa1982.todo4android.domain.model.Task
import com.syousa1982.todo4android.domain.model.TaskList
import com.syousa1982.todo4android.domain.usecase.ToDoUseCase
import com.syousa1982.todo4android.util.rx.TestSchedulerProvider
import io.mockk.*
import io.reactivex.Single
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


/**
 * ToDoUseCaseのテスト
 */
class ToDoUseCaseSpec : Spek({
    describe("ToDoUseCase") {
        lateinit var taskListRepository: ITaskListRepository
        lateinit var todoUseCase: ToDoUseCase
        /**
         * 正常系テスト
         */
        context("Success") {
            beforeEachTest {
                taskListRepository = mockk<ITaskListRepository>().also {
                    every { it.loadTaskListAndTasksByDB() } answers {
                        val taskListAndTasks = TaskListAndTasks()
                        taskListAndTasks.taskList = TaskListEntity(1, "todo")
                        taskListAndTasks.tasks = listOf(
                            TaskEntity(1, 1, "aaaaa", Task.Status.DONE.value.toLowerCase()),
                            TaskEntity(2, 1, "aaaaa", Task.Status.TODO.value.toLowerCase()),
                            TaskEntity(3, 1, "aaaaa", Task.Status.TODO.value.toLowerCase())
                        )
                        Single.fromCallable {
                            listOf(taskListAndTasks)
                        }
                    }
                    every { it.loadTaskListAndTasksByDB("1") } answers {
                        val taskListAndTasks = TaskListAndTasks()
                        taskListAndTasks.taskList = TaskListEntity(1, "todo")
                        taskListAndTasks.tasks = listOf(
                            TaskEntity(1, 1, "aaaaa", Task.Status.DONE.value.toLowerCase()),
                            TaskEntity(2, 1, "aaaaa", Task.Status.TODO.value.toLowerCase()),
                            TaskEntity(3, 1, "aaaaa", Task.Status.TODO.value.toLowerCase())
                        )
                        Single.fromCallable {
                            taskListAndTasks
                        }
                    }

                }
                todoUseCase = ToDoUseCase(taskListRepository, TestSchedulerProvider())
            }

            it("getTaskLists()") {
                val expectedValue =
                    Result.success(listOf(
                        TaskList(1, "todo", listOf(
                            Task(1, "aaaaa", Task.Status.DONE),
                            Task(2, "aaaaa", Task.Status.TODO),
                            Task(3, "aaaaa", Task.Status.TODO)
                        ))
                    ))
                todoUseCase
                    .getTaskLists()
                    .test()
                    .assertValues(Result.Progress(), expectedValue)
            }

            it("addTaskList()") {
                val expect = "hoge"
                val taskListEntity = TaskListEntity(name = expect)

                every { taskListRepository.insertTaskListByDB(taskListEntity) } answers {
                    Single.fromCallable {
                        1L
                    }
                }
                todoUseCase.addTaskList(expect)
                    .skip(1)
                    .test()
                    .assertValue {
                        it is Result.Success
                    }
                verify { taskListRepository.insertTaskListByDB(taskListEntity) }
                confirmVerified(taskListRepository)
            }

            it("updateTaskList()") {
                val updatedValue =
                    TaskList(1, "UpdatedTodoList", listOf(
                        Task(1, "aaaaa", Task.Status.DONE),
                        Task(2, "aaaaa", Task.Status.TODO),
                        Task(3, "aaaaa", Task.Status.TODO)
                    ))

                val taskListEntity = TaskListEntity(updatedValue.id, updatedValue.name)

                every { taskListRepository.updateTaskListByDB(taskListEntity) } answers {
                    Single.fromCallable {
                        1
                    }
                }

                todoUseCase.updateTaskList(updatedValue)
                    .skip(1)
                    .test()
                    .assertValue {
                        it is Result.Success
                    }
                verify { taskListRepository.updateTaskListByDB(taskListEntity) }
                confirmVerified(taskListRepository)
            }

            it("getTasks()") {
                val expectedValue = Result.success(listOf(
                    Task(1, "aaaaa", Task.Status.DONE),
                    Task(2, "aaaaa", Task.Status.TODO),
                    Task(3, "aaaaa", Task.Status.TODO)
                ))
                todoUseCase.getTasks(1)
                    .test()
                    .assertValues(Result.Progress(), expectedValue)
            }

            it("addTasks()") {
                val expect = "fuga"
                val taskEntity = TaskEntity(name = expect, taskListId = 1, status = Task.Status.TODO.value)
                every { taskListRepository.insertTaskByDB(taskEntity) } answers {
                    Single.fromCallable {
                        1L
                    }
                }
                todoUseCase.addTask(1, expect)
                    .skip(1)
                    .test()
                    .assertValue {
                        it is Result.Success
                    }
                verify { taskListRepository.insertTaskByDB(taskEntity) }
                confirmVerified(taskListRepository)
            }

            it("updateTask()") {
                val updatedValue = Task(2, "UpdatedTask", Task.Status.DONE)
                val entity = TaskEntity(updatedValue.id, 1, updatedValue.name, updatedValue.status.value)

                every { taskListRepository.updateTaskByDB(entity) } answers {
                    Single.fromCallable {
                        1
                    }
                }

                todoUseCase.updateTask(1, updatedValue)
                    .skip(1)
                    .test()
                    .assertValue {
                        it is Result.Success
                    }

                verify { taskListRepository.updateTaskByDB(entity) }
                confirmVerified(taskListRepository)
            }

            afterEachTest {
                unmockkAll()
            }
        }

        /**
         * 異常系テスト
         */
        context("Failure Test") {
            beforeEachTest {
                taskListRepository = mockk<ITaskListRepository>().also {
                    every { it.loadTaskListAndTasksByDB() } answers {
                        Single.fromCallable {
                            listOf(TaskListAndTasks())
                        }
                    }
                    every { it.loadTaskListAndTasksByDB("1") } answers {
                        Single.fromCallable {
                            TaskListAndTasks()
                        }
                    }
                }
                todoUseCase = ToDoUseCase(taskListRepository, TestSchedulerProvider())
            }

            it("getTaskLists()") {
                todoUseCase.getTaskLists().skip(1).test()
                    .assertNoErrors()
                    .assertComplete()
                    .assertValueCount(1)
                    .assertValue {
                        it is Result.Failure
                    }
            }

            it("getTasks()") {
                todoUseCase.getTasks(1).skip(1).test()
                    .assertNoErrors()
                    .assertComplete()
                    .assertValueCount(1)
                    .assertValue {
                        it is Result.Failure
                    }
            }

            afterEachTest {
                unmockkAll()
            }
        }
    }
})