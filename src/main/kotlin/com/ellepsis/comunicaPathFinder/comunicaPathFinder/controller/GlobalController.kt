import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by EllepsisRT on 12.12.2015.
 */
@Controller
class GlobalController {

    @RequestMapping("/")
    fun index(): String {
        return "web/index"
    }
}
