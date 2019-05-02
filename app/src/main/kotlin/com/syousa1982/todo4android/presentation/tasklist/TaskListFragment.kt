package com.syousa1982.todo4android.presentation.tasklist


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.syousa1982.todo4android.R
import com.syousa1982.todo4android.databinding.FragmentTaskListBinding
import com.syousa1982.todo4android.domain.Result
import com.syousa1982.todo4android.presentation.MainActivity
import com.syousa1982.todo4android.presentation.tasklist.item.TaskListItem
import com.syousa1982.todo4android.util.extention.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * タスクリスト一覧 [Fragment] subclass.
 *
 */
class TaskListFragment : Fragment() {

    private val viewModel: TaskListViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentTaskListBinding.inflate(inflater, container, false)
        lifecycle.addObserver(viewModel)
        (requireActivity() as MainActivity).setAppBarTitle("タスクリスト一覧")
        bindInput(binding)
        bindRecyclerView(binding, viewModel)
        return binding.root
    }

    private fun bindInput(binding: FragmentTaskListBinding) {
        binding.addButton.setOnClickPauseListener {
            Navigation.findNavController(it).navigate(R.id.action_tasksFragment_to_taskListAddFragment)
        }
    }

    /**
     * タスクリストを出力
     *
     * @param binding
     * @param viewModel
     */
    private fun bindRecyclerView(binding: FragmentTaskListBinding, viewModel: TaskListViewModel) {
        // Input
        binding.taskList.setGroupieAdapter()
        binding.taskList.setLinearLayoutManagerWithDivider()
        binding.taskList.setGroupieOnItemClickListener<TaskListItem> { item, view ->
            item.taskList?.let {
                Navigation.findNavController(view).navigate(
                    TaskListFragmentDirections.actionTasksFragmentToTaskFragment(it)
                )
            }
        }
        // Output
        viewModel.taskLists.observe(this) {
            when (it) {
                is Result.Progress -> {
                    Log.d(className(), "タスクリスト取得開始")
                }
                is Result.Success -> {
                    Log.d(className(), "タスクリスト取得成功")
                    val items = it.data.map { taskList ->
                        TaskListItem(taskList)
                    }
                    binding.taskList.getGroupieAdapter().update(items)
                }
                is Result.Failure -> {
                    Log.d(className(), "タスクリスト取得失敗", it.e)
                }
            }
        }
    }

}
