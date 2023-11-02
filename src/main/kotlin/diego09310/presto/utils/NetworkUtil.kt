package diego09310.presto.utils

import org.springframework.stereotype.Service
import java.net.Inet4Address
import java.net.NetworkInterface
import kotlin.streams.asSequence

@Service
class NetworkUtil {
    fun getLocalIpAddress(): String {
        return NetworkInterface.networkInterfaces()
            .asSequence()
            .filter{ it.name.matches("wlp.*".toRegex()) }
            .first()
            .inetAddresses.asSequence()
            .filter{ it is Inet4Address }
            .first().toString().substring(1)
    }

}