package pl.polsl.data.model

import pl.polsl.main.Main
import java.util.*

data class Page(val dataList: List<Data>) {

    val date: Date
        get() = dataList.first().date

    init {
        if(dataList.size != Main.pageSize)
            throw IllegalStateException()
    }

}