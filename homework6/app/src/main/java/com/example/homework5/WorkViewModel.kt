package com.example.homework5

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class WorkViewModel : ViewModel() {

    private var job: Job? = null

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> get() = _progress

    private val _status = MutableLiveData("Idle")
    val status: LiveData<String> get() = _status

    fun startWork() {
        if (job?.isActive == true) return

        _progress.value = 0
        _status.value = "準備中"

        job = CoroutineScope(Dispatchers.Default).launch {
            try {
                // 準備階段
                delay(1000)
                if (!isActive) return@launch
                _status.postValue("工作中")

                // 主要工作階段
                for (i in 1..100) {
                    if (!isActive) break
                    delay(50)
                    _progress.postValue(i)
                }

                // 完成處理 - 確保狀態正確更新
                if (isActive) {
                    _status.postValue("背景工作結束")
                    // 保持進度在 100%
                    _progress.postValue(100)
                }
            } catch (e: CancellationException) {
                _status.postValue("已取消")
                _progress.postValue(0)
            }
        }
    }

    fun cancelWork() {
        job?.cancel()
        _status.value = "已取消"
        _progress.value = 0
    }
}
