package bootstrap.liftweb

import org.apache.http.HttpStatus

import net.liftweb.http.LiftRules
import net.liftweb.http.Req

class Boot {
    def boot
    {
      LiftRules.supplementalHeaders.default.set(List(
          ("Access-Control-Allow-Origin", "*"),
          ("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE"),
          ("Access-Control-Allow-Headers", "Content-Type")
      ))
      
      /**
       * Lift rules that indicate which routes are handled by the RestHelper
       */
      LiftRules.liftRequest.prepend {
        case Req("api":: _, _, _) => true
      }
      
      /**
       * Lift rules that indicate which routes are handled by the JEE Servlet
       */
      LiftRules.liftRequest.append {
        case Req("swagger" :: _, _, _) => false
      }
      
      /**
       * The Rest Services
       */
      LiftRules.dispatch.append(elasticmedium.api.Routes)
    }
}