package pl.polsl.data.model

import java.util.*

data class Page(val dataList: LinkedList<Data>) {

    val date: Date
        get() = dataList.first().date

}