package diego09310.presto.data

import java.util.UUID

data class Player (val name: String) {
    val id = UUID.randomUUID().toString()
    lateinit var team: String
}