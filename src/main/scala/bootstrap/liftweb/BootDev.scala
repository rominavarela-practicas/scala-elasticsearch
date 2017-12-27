package bootstrap.liftweb

import net.liftweb.common.Loggable
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext

/**
 * Run the Jetty Embebed server with mvn:scala run
 * 
 * @see https://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty
 */
object BootDev extends App with Loggable {
  var server = None: Option[Server]
  var devMode = false;
  
  override def main(args: Array[String]): Unit = {
      devMode = true
      server = Some(new Server(9000))
      
      val context = new WebAppContext()
      context.setServer(server.get)
      context.setWar("src/main/webapp")
    
      val context0: ContextHandler = new ContextHandler()
      context0.setHandler(context)
      server.get.setHandler(context0)
      
      server.get.start()
      server.get.join()
  }
}