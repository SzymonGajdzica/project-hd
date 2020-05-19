package pl.polsl.user

data class UserSimulatorInfo(
        val maxNumberOfUsers: Int,
        val fetchedElementsByUserRange: IntRange,
        val userSpawnTimeRange: LongRange,
        val userAskDelayRange: LongRange
)