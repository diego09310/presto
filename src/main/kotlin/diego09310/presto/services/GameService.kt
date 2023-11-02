package diego09310.presto.services

import diego09310.presto.data.Game
import diego09310.presto.data.GameState
import diego09310.presto.data.Player
import diego09310.presto.data.Team
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service

data class BuzzData(val player: String, val team: String, val position: Int)

@Service
class GameService(
    val spotifyService: SpotifyService,
    val webSocketsService: WebSocketsService,
) {

    private val log = LogManager.getLogger()!!

    private val game = Game

    private val mutex = Mutex()

    // TODO: Remove
    init{
        addTeam("1")
        addTeam("2")
        addTeam("3")
    }

    fun addTeam(name: String) {
        if (game.teams.find {it.name == name} != null) {
            return
        }
        val team = Team(name)
        game.teams.add(team)
        webSocketsService.sendAll(MessageType.NEW_TEAM.type, team)
    }

    fun deleteTeam(teamId: String) {
        game.teams.removeIf { it.id == teamId }
        // TODO: Create new type or generalize message
        webSocketsService.sendAll(MessageType.NEW_TEAM.type, null)
    }

    fun getTeams(): List<Team> {
        return game.teams
    }

    fun getTeam(id: String): Team? {
        return game.teams.find{id == it.id}
    }

    fun addPlayer(name: String, teamId: String): Player {
        val player = Player(name)
        getTeam(teamId)?.players?.add(player)
        player.team = teamId
        webSocketsService.sendAll(MessageType.NEW_USER.type, player)
        return player
    }

    fun deletePlayer(playerId: String, teamId: String) {
        game.teams.find { it.id == teamId }?.players?.removeIf { it.id == playerId }
        // TODO: Create new type or generalize message
        webSocketsService.sendAll(MessageType.NEW_USER.type, null)
    }

    fun buzz(playerId: String, teamId: String) {
        val position = Game.buzzes.size + 1
        Game.buzzes.add(playerId)
        if (Game.buzzes.size != position) {
            return
        }
        if (position == 1) {
            spotifyService.pause()
        }
        val team = getTeam(teamId)
        val player = team?.players?.find{it.id == playerId}
        if (player == null) {
            log.error("Couldn't find player that buzzed. playerId: ${playerId}, teamId: ${teamId}")
            return
        }
        webSocketsService.sendAll(MessageType.BUZZ_RESULTS.type, BuzzData(player.name, team.name, position))
    }

    fun startGame() {
        Game.buzzes.clear()
        Game.state = GameState.PLAYING
        webSocketsService.sendAll(MessageType.GAME.type, "start")
    }

    fun getGameState(): GameState {
        return Game.state
    }
}