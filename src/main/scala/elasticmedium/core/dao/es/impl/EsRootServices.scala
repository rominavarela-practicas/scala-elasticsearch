package elasticmedium.core.dao.es.impl

import elasticmedium.core.dao.es.EsService

import java.net.URI

import com.amazonaws.DefaultRequest
import com.amazonaws.http.HttpMethodName

import scala.collection.immutable.Map
import scala.collection.JavaConversions._
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonDSL._

/**
 * Elastic Search Services: Root<br/>
 * Forms signed requests for the root operations of a hosted AmazonES
 * @see https://github.com/aws/aws-sdk-java/issues/861
 * @see https://aws.amazon.com/es/blogs/security/how-to-control-access-to-your-amazon-elasticsearch-service-domain/
 */
class EsRootServices extends EsService {
  val rootEndpoint = new URI("http://" + endpoint + "/")
  
  /**
   * GET Verb
   * <hr/> Fails on:
   * <li> Not found
   * <li> Illegal access
   * <li> Network error
   * @return The Json Content
   */
  def GET():JObject = {
    val getReq = new DefaultRequest[JObject]("es")
        getReq.setHttpMethod(HttpMethodName.GET)
        getReq.setEndpoint(rootEndpoint);
    return this.exec(getReq)
          .getAwsResponse()
          .\("content")
          .asInstanceOf[JObject];
  }
}