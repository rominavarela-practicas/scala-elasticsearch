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
import net.liftweb.json.JsonAST.JInt
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonDSL._

/**
 * Elastic Search Services: Index<br/>
 * Forms signed requests for the root operations of a hosted AmazonES
 * @see https://github.com/aws/aws-sdk-java/issues/861
 * @see https://aws.amazon.com/es/blogs/security/how-to-control-access-to-your-amazon-elasticsearch-service-domain/
 */
class EsMappingServices extends EsService {
  def env = Env.getInstance()
  val rootEndpoint = new URI("http://" + endpoint + "/")
  
  def GET():JObject = {
    val getReq = new DefaultRequest[JObject]("es")
        getReq.setHttpMethod(HttpMethodName.GET)
        getReq.setEndpoint(new URI("http://" + endpoint + "/_mapping"));
    return this.exec(getReq)
          .getAwsResponse()
          .\("content")
          .asInstanceOf[JObject];
  }
  
  def GET(indexName:String):JObject = {
    val getReq = new DefaultRequest[JObject]("es")
        getReq.setHttpMethod(HttpMethodName.GET)
        getReq.setEndpoint(new URI("http://" + endpoint + "/" + indexName + "/_mapping"));
    return this.exec(getReq)
          .getAwsResponse()
          .\("content")
          .asInstanceOf[JObject];
  }
  
  def GET(indexName:String, typeName:String):JObject = {
    val getReq = new DefaultRequest[JObject]("es")
        getReq.setHttpMethod(HttpMethodName.GET)
        getReq.setEndpoint(new URI("http://" + endpoint + "/" + indexName + "/_mapping/" + typeName));
    return this.exec(getReq)
          .getAwsResponse()
          .\("content")
          .asInstanceOf[JObject];
  }
  
  /**
   * @param mapping The mapping properties of a type
   * @see https://www.elastic.co/guide/en/elasticsearch/reference/5.1/properties.html
   * @see https://www.elastic.co/guide/en/elasticsearch/reference/5.1/mapping-params.html
   */
  def PUT(indexName:String, typeName:String, mapping:String):Int = {
    val req = new DefaultRequest[JObject]("es")
        req.setHttpMethod(HttpMethodName.PUT)
        req.setEndpoint(new URI("http://" + endpoint + "/" + indexName + "/_mapping/" + typeName))
        req.setContent(new ByteArrayInputStream(mapping.getBytes));
    return this.exec(req)
          .getAwsResponse()
          .\("status-code")
          .asInstanceOf[JInt].values.toInt;
  }
}