package pl.polsl.main

import pl.polsl.data.manager.Connector
import pl.polsl.data.source.DataSource
import pl.polsl.data.source.DataSourceInfo
import pl.polsl.user.UserSimulator
import pl.polsl.user.UserSimulatorInfo

object Main {

    var pageSize = 10
        private set

    @JvmStatic
    fun main(args: Array<String>) {
        val dataSourceInfo = DataSourceInfo(
                maxTimeOfInactivity = 5000L,
                produceTimeRange = 1L..4L,
                connectionTimeRange = 200L..400L,
                singleDataLoadRange = 10L..20L
        )
        val userSourceInfo = UserSimulatorInfo(
                fetchedElementsByUserRange = 100..200,
                maxNumberOfUsers = 100,
                userSpawnTimeRange = 0L..100L,
                userAskDelayRange = 10L..20L
        )

        val dataSource = DataSource(dataSourceInfo)
        val connector = Connector(dataSource)
        val userSimulator = UserSimulator(connector, userSourceInfo)
        userSimulator.startSimulation()
    }

}