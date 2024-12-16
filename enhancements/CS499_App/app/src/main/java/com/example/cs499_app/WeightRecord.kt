
data class WeightRecord (
    val id: String = "",
    var date: Long,
    var weight: Double = 0.0,
    val userId: String
)
// Constructor needed for Firebase Realtime Database to work
{
    constructor() : this("", 0, 0.0, "")
}

