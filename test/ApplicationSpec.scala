import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beSome.which (status(_) == NOT_FOUND)
    }

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Your new application is ready.")
    }

    "return a json from hello endpoint" in new WithApplication {
      val json = Json.obj(
        "name" -> JsString("Eduardo")
      )
      val helloEndpoint = FakeRequest(
                            method = "POST",
                            uri = "/hello",
                            headers = FakeHeaders(Seq("Content-type" -> "application/json")),
                            body =  json
                        )

      val result = route(helloEndpoint).get
      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")

      val responseNode = Json.parse(contentAsString(result))
      (responseNode \ "message").as[String] must equalTo("Hello Eduardo")
    }

    "return a json from hello-async endpoint" in new WithApplication {
      val json = Json.obj(
        "name" -> JsString("Eduardo ASync")
      )
      val helloAsyncEndpoint = FakeRequest(
        method = "POST",
        uri = "/hello-async",
        headers = FakeHeaders(Seq("Content-type" -> "application/json")),
        body =  json
      )

      val result = route(helloAsyncEndpoint).get
      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")

      val responseNode = Json.parse(contentAsString(result))
      (responseNode \ "message").as[String] must equalTo("Hello Eduardo ASync")
    }
  }
}