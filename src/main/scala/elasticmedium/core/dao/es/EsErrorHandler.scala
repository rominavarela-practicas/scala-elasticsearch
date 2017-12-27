package elasticmedium.core.dao.es

import com.amazonaws.AmazonServiceException
import com.amazonaws.http.{HttpResponse, HttpResponseHandler}

class EsErrorHandler extends HttpResponseHandler[AmazonServiceException] {
  def handle(res: HttpResponse): AmazonServiceException = {
    new AmazonServiceException(res.getStatusText)
  }

  def needsConnectionLeftOpen(): Boolean = {
    return false;
  }
}
