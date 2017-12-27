package elasticmedium.core.dao.es.impl

import elasticmedium.core.Env
import elasticmedium.core.dao.es.EsService

import java.net.URI
import java.io.ByteArrayInputStream

import com.amazonaws.AmazonServiceException
import com.amazonaws.DefaultRequest
import com.amazonaws.http.HttpMethodName

import org.apache.http.HttpStatus

import scala.collection.immutable.Map
import scala.collection.JavaConversions._
import net.liftweb.json.compactRender
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonDSL._

/**
 * Elastic Search Services: Model/Type<br/>
 * Forms signed requests for the type operations of a hosted AmazonES
 * @see https://github.com/aws/aws-sdk-java/issues/861
 * @see https://aws.amazon.com/es/blogs/security/how-to-control-access-to-your-amazon-elasticsearch-service-domain/
 */
class EsTypeServices extends EsService {
  def env = Env.getInstance()
  
  /**
   * DELETE Verb
   * @return 200 Deleted
   */
  def DELETE(indexName:String, typeName:String): Int = {
    val req = new DefaultRequest[JObject]("es")
        req.setHttpMethod(HttpMethodName.DELETE)
        req.setEndpoint(new URI("http://" + endpoint + "/" + indexName + "/" + typeName));
    return this.exec(req)
          .getAwsResponse()
          .\("status-code")
          .asInstanceOf[JInt].values.toInt;
  }
}