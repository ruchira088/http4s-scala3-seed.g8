package com.ruchij

import org.scalatest.FlatSpec

class ExampleTest extends FlatSpec
{
  "Sample test" should "pass nicely" in {
    assertResult(6)(App.add(1, 5))
  }
}
