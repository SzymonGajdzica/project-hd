package pl.polsl.user

import pl.polsl.data.manager.Connector
import java.util.*

class UserSimulator(
        private val connector: Connector,
        private val userSimulatorInfo: UserSimulatorInfo
) {

    private var numberOfUsers = 0

    private val userCreatorThread = Thread {
        while (numberOfUsers < userSimulatorInfo.maxNumberOfUsers) {
            Thread.sleep(userSimulatorInfo.userSpawnTimeRange.random())
            numberOfUsers++
            createNewUserThread()
        }
    }

    fun startSimulation() {
        userCreatorThread.start()
    }

    private fun createNewUserThread() {
        Thread {
            val dataManager = connector.connect(Date())
            dataManager.initialize()
            val maxNumberOfElements = userSimulatorInfo.fetchedElementsByUserRange.random()
            var numberOfElements = 0
            while (numberOfElements < maxNumberOfElements) {
                Thread.sleep(userSimulatorInfo.userAskDelayRange.random())
                dataManager.next
                numberOfElements++
            }
            dataManager.finish()
        }.start()
    }

}