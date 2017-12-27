package elasticmedium.api

import elasticmedium.api.controller._
import net.liftweb.http.rest.RestHelper
import net.liftweb.http.S
import net.liftweb.http.Req
import net.liftweb.http.OkResponse
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonDSL._

object Routes extends RestHelper {
  serve {
    case "api" :: "query" :: Nil JsonGet req => QueryController.getRoot()
    case "api" :: "query" :: "_mapping" :: Nil JsonGet req => QueryController.getMapping()
    case "api" :: "query" :: indexName :: "_mapping" :: Nil JsonGet req => QueryController.getMapping(indexName)
    case "api" :: "query" :: indexName :: "_mapping" :: typeName :: Nil JsonGet req => QueryController.getMapping(indexName, tail(req))
    case "api" :: "query" :: indexName :: typeName :: Nil JsonDelete req => QueryController.deleteType(indexName, tail(req))
    case "api" :: "query" :: indexName :: typeName :: id :: Nil JsonGet req => QueryController.getDocument(indexName, typeName, tail(req))
    case "api" :: "query" :: indexName :: typeName :: id :: Nil JsonDelete req => QueryController.deleteDocument(indexName, typeName, tail(req))
    case "api" :: "query" :: indexName:: typeName :: "_search" :: Nil JsonPost req => QueryController.searchDocumentByTerms(indexName, typeName, body(req).asInstanceOf[JObject], Integer.parseInt(S.param("from").getOrElse("0")), Integer.parseInt(S.param("size").getOrElse("20")))
    case "api" :: "query" :: indexName:: typeName :: "_search" :: fieldName :: Nil JsonGet req => QueryController.searchDocumentByMappedField(indexName, typeName, tail(req), Integer.parseInt(S.param("from").getOrElse("0")), Integer.parseInt(S.param("size").getOrElse("20")))
    case Options(_, req) => new OkResponse
  }

  /**
   * Extract the last param without removing file extensions
   */
  def tail(req:(Any,Req)):String = {
    req._2.path.wholePath.last
  }

  /**
   * Extract the last param without removing file extensions
   */
  def tail(req:Req):String = {
    req.path.wholePath.last
  }

  /**
   * Extract the request body
   */
  def body(req:(JValue,Any)):JValue = {
    req._1
  }
}
