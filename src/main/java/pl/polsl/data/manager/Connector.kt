package pl.polsl.data.manager

import pl.polsl.data.source.DataSource
import pl.polsl.strategy.SampleLoadStrategy
import java.util.*

class Connector(private val dataSource: DataSource) {

    fun connect(startDate: Date): DataManager {
        return DataManager(dataSource, SampleLoadStrategy(), startDate)
    }

}