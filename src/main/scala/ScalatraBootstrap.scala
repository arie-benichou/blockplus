

import org.scalatra.LifeCycle
import javax.servlet.ServletContext
import services.BlokusContextContainer

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new BlokusContextContainer(), "/*")
  }
}
