/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yourcompany.android.taskie.ui.notes.dialog

import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.yourcompany.android.taskie.App
import com.yourcompany.android.taskie.R
import com.yourcompany.android.taskie.databinding.FragmentDialogNewTaskBinding
import com.yourcompany.android.taskie.model.PriorityColor
import com.yourcompany.android.taskie.model.Success
import com.yourcompany.android.taskie.model.Task
import com.yourcompany.android.taskie.model.request.AddTaskRequest
import com.yourcompany.android.taskie.networking.NetworkStatusChecker
import com.yourcompany.android.taskie.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Dialog fragment to create a new task.
 */
class AddTaskDialogFragment : DialogFragment() {

  private var _binding: FragmentDialogNewTaskBinding? = null
  private val binding  get() = _binding!!

  private var taskAddedListener: TaskAddedListener? = null
  private val remoteApi = App.remoteApi
  private val networkStatusChecker by lazy {
    NetworkStatusChecker(activity?.getSystemService(ConnectivityManager::class.java))
  }

  interface TaskAddedListener {
    fun onTaskAdded(task: Task)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NO_TITLE, R.style.FragmentDialogTheme)
  }

  fun setTaskAddedListener(listener: TaskAddedListener) {
    taskAddedListener = listener
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    _binding = FragmentDialogNewTaskBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onStart() {
    super.onStart()
    dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                              WindowManager.LayoutParams.WRAP_CONTENT)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initUi()
    initListeners()
  }

  private fun initUi() {
    context?.let {
      binding.prioritySelector.adapter =
        ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item,
                                    PriorityColor.values())
      binding.prioritySelector.setSelection(0)
    }
  }

  private fun initListeners() = binding.saveTaskAction.setOnClickListener { saveTask() }

  private fun saveTask() {
    if (isInputEmpty()) {
      context?.toast(getString(R.string.empty_fields))
      return
    }

    val title = binding.newTaskTitleInput.text.toString()
    val content = binding.newTaskDescriptionInput.text.toString()
    val priority = binding.prioritySelector.selectedItemPosition + 1

    networkStatusChecker.performIfConnectedToInternet {
      viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
        val result = remoteApi.addTask(AddTaskRequest(title, content, priority))
        withContext(Dispatchers.Main) {
          if (result is Success) {
            onTaskAdded(result.data)
          } else {
            onTaskAddFailed()
          }
        }
      }
      clearUi()
    }
  }


  private fun clearUi() {
    binding.newTaskTitleInput.text.clear()
    binding.newTaskDescriptionInput.text.clear()
    binding.prioritySelector.setSelection(0)
  }

  private fun isInputEmpty(): Boolean = TextUtils.isEmpty(
      binding.newTaskTitleInput.text) || TextUtils.isEmpty(binding.newTaskDescriptionInput.text)

  private fun onTaskAdded(task: Task) {
    taskAddedListener?.onTaskAdded(task)
    dismiss()
  }

  private fun onTaskAddFailed() {
    this.activity?.toast("Something went wrong!")
  }

  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }
}