package elasticmedium.core

import java.io.File

object Env {
  val ScalaTest:Boolean = {
    val envvalue = System.getenv("scalatest")
    (envvalue != null)
  }

  private lazy val instance = new Env()
  def getInstance() = instance
}

class Env {
  if(Env.ScalaTest) {
    throw new Error("Env instantiation during test execution");
  }
  
  val AwsEsDomain:String = {
    val envvalue = System.getenv("ELASTICMEDIUM_AWS_ES_DOMAIN")
    if(envvalue == null) {
      throw new Error("ELASTICMEDIUM_AWS_ES_DOMAIN is not defined")
    }
    envvalue
  }
}
