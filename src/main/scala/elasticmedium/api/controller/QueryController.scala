package elasticmedium.api.controller

import bootstrap.liftweb.BootDev

import elasticmedium.core.dao.es.impl.EsRootServices
import elasticmedium.core.dao.es.impl.EsMappingServices
import elasticmedium.core.dao.es.impl.EsIndexServices
import elasticmedium.core.dao.es.impl.EsDocumentServices

import scala.collection.immutable.Map
import scala.collection.JavaConversions._
import net.liftweb.http.FileParamHolder
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonDSL._
import net.liftweb.http.{JsonResponse, OkResponse, CreatedResponse, BadRequestResponse}

object QueryController {
  protected lazy val esRootServices = new EsRootServices()
  protected lazy val esMappingServices = new EsMappingServices()
  protected lazy val esIndexServices = new EsIndexServices()
  protected lazy val esDocumentServices = new EsDocumentServices()
  
  def getRoot() = {
    try {
      JsonResponse(
          ("response" -> esRootServices.GET())
      )
    } catch {
      case exc:Exception => {
        if(BootDev.devMode) {
          exc.printStackTrace();
        }
        new BadRequestResponse(exc.getMessage)
      }
    }
  }
  
  def getMapping() = {
    try {
      JsonResponse(
          ("response" -> esMappingServices.GET())
      )
    } catch {
      case exc:Exception => {
        if(BootDev.devMode) {
          exc.printStackTrace();
        }
        new BadRequestResponse(exc.getMessage)
      }
    }
  }
  
  def getMapping(indexName:String) = {
    try {
      JsonResponse(
          ("response" -> esMappingServices.GET(indexName))
      )
    } catch {
      case exc:Exception => {
        if(BootDev.devMode) {
          exc.printStackTrace();
        }
        new BadRequestResponse(exc.getMessage)
      }
    }
  }
  
  def getMapping(indexName:String, typeName:String) = {
    try {
      JsonResponse(
          ("response" -> esMappingServices.GET(indexName, typeName))
      )
    } catch {
      case exc:Exception => {
        if(BootDev.devMode) {
          exc.printStackTrace();
        }
        new BadRequestResponse(exc.getMessage)
      }
    }
  }
  
  def deleteType(indexName:String) = {
    try {
      JsonResponse(
          ("response" -> esIndexServices.DELETE(indexName))
      )
    } catch {
      case exc:Exception => {
        if(BootDev.devMode) {
          exc.printStackTrace();
        }
        new BadRequestResponse(exc.getMessage)
      }
    }
  }
  
  def getDocument(indexName:String, typeName:String, id:String) = {
    try {
      JsonResponse(
          ("response" -> esDocumentServices.GET(indexName, typeName, id))
      )
    } catch {
      case exc:Exception => {
        if(BootDev.devMode) {
          exc.printStackTrace();
        }
        new BadRequestResponse(exc.getMessage)
      }
    }
  }
  
  def deleteDocument(indexName:String, typeName:String, id:String) = {
    try {
      JsonResponse(
          ("response" -> esDocumentServices.DELETE(indexName, typeName, id))
      )
    } catch {
      case exc:Exception => {
        if(BootDev.devMode) {
          exc.printStackTrace();
        }
        new BadRequestResponse(exc.getMessage)
      }
    }
  }
  
  def searchDocumentByTerms(indexName:String, typeName:String, termKeyVal: JObject, from:Int, size:Int) = {
    val termList = termKeyVal.values.map {
      case (key:String, sValue:String) => ("term" -> (key -> JString(sValue) ))
      case (key:String, bValue:Boolean) => ("term" -> (key -> JBool(bValue) ))
      case (key:String, iValue:BigInt) => ("term" -> (key -> JInt(iValue) ))
      case (key:String, dValue:Double) => ("term" -> (key -> JDouble(dValue) ))
    }.toList;
    
    val query = (
      (
          "query" -> (
              "bool" -> (
                  "filter" -> termList
              )
          )
      ) ~
      ("from" -> from) ~
      ("size" -> size)
    );
    
    try {
      JsonResponse(
          ("query" -> query) ~
          ("response" -> esDocumentServices.SEARCH(indexName, typeName, query))
      )
    } catch {
      case exc:Exception => {
        if(BootDev.devMode) {
          exc.printStackTrace();
        }
        new BadRequestResponse(exc.getMessage)
      }
    }
  }
}
