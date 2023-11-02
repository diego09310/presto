package diego09310.presto.data

data class Team (val name: String) {
    val id = name.lowercase()
    val players = mutableListOf<Player>()
}