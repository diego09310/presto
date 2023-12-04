package diego09310.presto.controllers

import diego09310.presto.services.GameService
import diego09310.presto.services.QRService
import diego09310.presto.utils.NetworkUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/kiosk")
class KioskController(
    val gameService: GameService,
    val networkUtil: NetworkUtil,
    val qrService: QRService,

    @Value("\${server.port}") private val port: String,
) {

    @GetMapping
    fun getKiosk(model: Model): String {
        model["teams"] = gameService.getTeams()
        val ip = networkUtil.getLocalIpAddress()
        val url = "http://$ip:$port/"
        model["url"] = url
        model["qr"] = qrService.generateQrString(url)
        return "pages/kiosk"
    }
}
