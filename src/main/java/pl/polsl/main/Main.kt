package pl.polsl.main

import pl.polsl.data.manager.Connector
import pl.polsl.data.source.DataSource
import pl.polsl.data.source.DataSourceInfo
import pl.polsl.strategy.TriggerLoadStrategy
import pl.polsl.user.UserSimulator
import pl.polsl.user.UserSimulatorInfo

object Main {

    var maxBufferSize = 100
        private set
    var pageSize = 10
        private set

    @JvmStatic
    fun main(args: Array<String>) {
        val dataSourceInfo = DataSourceInfo(
                maxTimeOfInactivity = 5000L,
                numberOfProducers = 4,
                produceTimeRange = 2L..4L,
                connectionTimeRange = 80L..160L,
                singleDataLoadRange = 10L..20L
        )
        val userSourceInfo = UserSimulatorInfo(
                fetchedElementsByUserRange = 100..200,
                maxNumberOfUsers = 10,
                userSpawnTimeRange = 0L..100L,
                userAskDelayRange = 10L..20L
        )

        val dataSource = DataSource(dataSourceInfo)
        val loadStrategyCreator =  { TriggerLoadStrategy() }
        val connector = Connector(dataSource, loadStrategyCreator)
        val userSimulator = UserSimulator(connector, userSourceInfo)
        userSimulator.startSimulation()
    }

}