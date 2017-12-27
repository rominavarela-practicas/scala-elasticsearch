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
class EsDocumentServices extends EsService {
  def env = Env.getInstance()
  
  /**
   * GET Verb
   * @return The document
   */
  def GET(indexName:String, typeName:String, id:String): JObject = {
    val req = new DefaultRequest[JObject]("es")
        req.setHttpMethod(HttpMethodName.GET)
        req.setEndpoint(new URI("http://" + endpoint + "/" + indexName + "/" + typeName + "/" + id));
    return this.exec(req)
          .getAwsResponse()
          .\("content")
          .\("_source")
          .asInstanceOf[JObject];
  }
  
  
  /**
   * Search Operation
   * @param query Following the QueryDSL definition
   * @see https://www.elastic.co/guide/en/elasticsearch/reference/5.1/_the_search_api.html
   * @see https://www.elastic.co/guide/en/elasticsearch/reference/5.1/query-dsl.html
   * @return The hits object
   */
  def SEARCH(indexName:String, typeName:String, query:JObject): JObject = {
    val req = new DefaultRequest[JObject]("es")
        req.setHttpMethod(HttpMethodName.POST)
        req.setEndpoint(new URI("http://" + endpoint + "/" + indexName + "/" + typeName + "/_search"))
        req.setContent(new ByteArrayInputStream(compactRender(query).getBytes));
    return this.exec(req)
          .getAwsResponse()
          .\("content")
          .\("hits")
          .asInstanceOf[JObject];
  }
  
  /**
   * DELETE Verb
   * @return 200 Deleted
   */
  def DELETE(indexName:String, typeName:String, id:String): Int = {
    val req = new DefaultRequest[JObject]("es")
        req.setHttpMethod(HttpMethodName.DELETE)
        req.setEndpoint(new URI("http://" + endpoint + "/" + indexName + "/" + typeName + "/" + id));
    return this.exec(req)
          .getAwsResponse()
          .\("status-code")
          .asInstanceOf[JInt].values.toInt;
  }
}