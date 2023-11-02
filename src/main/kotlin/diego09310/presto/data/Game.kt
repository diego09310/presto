package diego09310.presto.data

object Game {
    val teams = mutableListOf<Team>()
    var state = GameState.WAITING
    val buzzes = linkedSetOf<String>()
}