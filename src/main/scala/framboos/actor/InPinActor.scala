package framboos.actor

import akka.actor._
import framboos._
import framboos.async._

class InPinActor(pinNumber: Int) extends Actor {

  val inPin = ObservableInPin(pinNumber)

  var listeners = Set.empty[ActorRef]

  val subscription = inPin.subscribe(newValue => listeners.foreach { _ ! NewValue(newValue) })

  def receive: Receive = {
    case AddListener(listener: ActorRef) => {
      listeners = listeners + listener
    }
    case RemoveListener(listener: ActorRef) => {
      listeners = listeners - listener
    }
  }
  
  override def postStop {
    subscription.unsubscribe
  }
}
