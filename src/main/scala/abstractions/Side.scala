package abstractions

sealed case class Side[A](values: A)(implicit input: (A, Any) => A, output: A => Boolean) {

  lazy val isIn: Boolean = output(values)
  lazy val isOut: Boolean = !isIn

  def apply(data: Any): Side[A] = copy(input(values, data))(input, output)

}