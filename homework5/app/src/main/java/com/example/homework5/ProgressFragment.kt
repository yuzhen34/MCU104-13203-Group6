package com.example.homework5

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ProgressFragment : Fragment(R.layout.frag_progress) {
    private val vm: WorkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val txtProgress = view.findViewById<TextView>(R.id.txtProgress)

        vm.progress.observe(viewLifecycleOwner) { progress ->
            txtProgress.text = if (progress == 0) "準備中..." else "完成 $progress%"
            if (progress == 100) txtProgress.text = "背景工作結束"
        }

        view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            vm.cancelWork()
        }
    }
}
