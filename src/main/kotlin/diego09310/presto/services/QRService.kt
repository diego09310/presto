package diego09310.presto.services

import net.glxn.qrgen.core.image.ImageType
import net.glxn.qrgen.javase.QRCode
import org.springframework.stereotype.Service
import java.util.Base64

@Service
class QRService {

    val DEFAULT_SIZE = 700

    fun generateQrString(text: String, size: Int = DEFAULT_SIZE): String {
        val image = QRCode
            .from(text)
            .to(ImageType.PNG)
            .withSize(size, size)
            .stream()
            .toByteArray()
        return Base64.getEncoder().encodeToString(image)
    }

}