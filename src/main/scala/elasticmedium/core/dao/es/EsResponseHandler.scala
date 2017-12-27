package elasticmedium.core.dao.es

import com.amazonaws.AmazonWebServiceResponse
import com.amazonaws.http.{HttpResponse, HttpResponseHandler}

import java.io.InputStreamReader
import scala.collection.immutable.Map
import scala.collection.JavaConversions._
import net.liftweb.json.JsonParser
import net.liftweb.json.JsonDSL._

class EsResponseHandler[Json] extends HttpResponseHandler[AmazonWebServiceResponse[Json]] {
  def handle(res: HttpResponse): AmazonWebServiceResponse[Json] = {
    val contentStream = res.getContent
    val jsonRes = if(contentStream !=null) {
      (
          ("status-code" -> res.getStatusCode) ~
          ("status-text" -> res.getStatusText) ~
          ("content" -> JsonParser.parse(new InputStreamReader(contentStream), true))
      )
    } else {
      (
        ("status-code" -> res.getStatusCode) ~
        ("status-text" -> res.getStatusText)
      )
    }

    val awsRes = new AmazonWebServiceResponse[Json]()
        awsRes.setResult(jsonRes.asInstanceOf[Json])

    return awsRes
  }

  def needsConnectionLeftOpen(): Boolean = {
    return false;
  }
}
