package pl.polsl.data.manager

import pl.polsl.data.source.DataSource
import pl.polsl.strategy.LoadStrategy
import java.util.*

class Connector(
        private val dataSource: DataSource,
        private val loadStrategy: LoadStrategy
) {

    fun connect(startDate: Date): DataManager {
        return DataManager(dataSource, loadStrategy, startDate)
    }

}