# Scala / Elastic Search

To avoid namespace problems, this project is packaged as "Elastic Medium"

### Requirements

 * JDK 1.8
 * Maven 3
 * AWS
   * AmazonES 5.1

#### Getting Started

1. Install the project dependencies. Use the command `mvn install`

2. Setup the AWS Credentials and Region. Follow [this guide](http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html#config-settings-and-precedence)

3. Setup the `ELASTICMEDIUM_AWS_ES_DOMAIN` Environment Variable.

4. Run the Embebed Jetty Server. Use the command `mvn scala:run`

   *Note 1: To run the Jetty Server from EclipseIDE, set the Maven Build / Goal "scala:run"*

   *Note 2: Force stop the Jetty Server with the `api/dev/stop` endpoint*
