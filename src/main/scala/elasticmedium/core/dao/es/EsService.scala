package elasticmedium.core.dao.es

import elasticmedium.core.Env

import com.amazonaws.{DefaultRequest, ClientConfiguration}
import com.amazonaws.http.AmazonHttpClient
import com.amazonaws.auth.AWS4Signer
import com.amazonaws.services.elasticsearch.AWSElasticsearchClientBuilder
import com.amazonaws.services.elasticsearch.model.DescribeElasticsearchDomainRequest
import com.amazonaws.regions.DefaultAwsRegionProviderChain
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain

import scala.collection.immutable.Map
import scala.collection.JavaConversions._
import net.liftweb.json.JsonAST.{JObject => Json }

private object EsService {
  def env = Env.getInstance()

  val credentials = DefaultAWSCredentialsProviderChain.getInstance.getCredentials
  val region = new DefaultAwsRegionProviderChain().getRegion

  val httpClient = new AmazonHttpClient(new ClientConfiguration())
  val resHandler = new EsResponseHandler[Json]()
  val errHandler = new EsErrorHandler()
  val context = new com.amazonaws.http.ExecutionContext()

  val endpoint: String = {
    val esClient = AWSElasticsearchClientBuilder.defaultClient()
    val req = new DescribeElasticsearchDomainRequest();
        req.setDomainName(env.AwsEsDomain)
    val res = esClient.describeElasticsearchDomain(req)
        res.getDomainStatus.getEndpoint
  }
}

abstract class EsService {
  protected val endpoint = {
    EsService.endpoint
  }

  protected def exec(req:DefaultRequest[Json]) = {
    val signer = new AWS4Signer()
        signer.setServiceName("es")
        signer.setRegionName(EsService.region)
        signer.sign(req, EsService.credentials);

    EsService.httpClient.execute(req, EsService.resHandler, EsService.errHandler, EsService.context)
  }
}
