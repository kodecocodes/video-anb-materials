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

package com.yourcompany.android.taskie.ui.notes

import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.yourcompany.android.taskie.databinding.ItemTaskBinding
import com.yourcompany.android.taskie.model.PriorityColor
import com.yourcompany.android.taskie.model.Task

/**
 * Holder to display the Task item in a list.
 */
class TaskHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

  fun bindData(task: Task, onItemLongClick: (String) -> Unit) {
    binding.root.setOnLongClickListener {
      onItemLongClick(task.id)
      true
    }

    binding.taskTitle.text = task.title
    binding.taskContent.text = task.content

    val drawable = binding.taskPriority.drawable
    val wrapDrawable = DrawableCompat.wrap(drawable)

    val priorityColor = when (task.taskPriority) {
      1 -> PriorityColor.LOW
      2 -> PriorityColor.MEDIUM
      else -> PriorityColor.HIGH
    }

    DrawableCompat.setTint(wrapDrawable,
        ContextCompat.getColor(binding.root.context, priorityColor.getColor()))
  }
}