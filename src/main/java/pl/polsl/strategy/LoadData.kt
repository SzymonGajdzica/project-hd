package pl.polsl.strategy

import java.util.*

data class LoadData(
        val date: Date,
        val waitTime: Long,
        val loadedPages: Int,
        val initialBufferSize: Int,
        val bufferSize: Int,
        val initialIsLoadingData: Boolean,
        val isLoadingData: Boolean
)