package abstractions

trait Move[A, B] {

  val side: A
  val data: B

}