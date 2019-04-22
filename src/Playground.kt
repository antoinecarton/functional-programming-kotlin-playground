package io.acarton.fp.playground

import io.acarton.fp.playground.Playground.compose
import io.acarton.fp.playground.Playground.curry
import io.acarton.fp.playground.Playground.partial1
import io.acarton.fp.playground.Playground.uncurry
import kotlin.test.assertEquals

object Playground {
    fun <A, B, C> partial1(a: A, f: (A, B) -> C): (B) -> C = { b: B -> f(a, b) }

    fun <A, B, C> curry(f: (A, B) -> C): (A) -> ((B) -> C) = { a -> { b -> f(a, b) } }

    fun <A, B, C> uncurry(f: (A) -> (B) -> C): (A, B) -> C = { a, b -> f(a)(b) }

    fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C = { a -> f(g(a)) }
}

fun main() {
    val partialAdd2: (Double) -> String = partial1(2) { a: Int, b: Double -> "${a + b}" }
    assertEquals("6.0", partialAdd2(4.0))

    val curryAdd: (Int) -> (Double) -> String = curry { a: Int, b: Double -> "${a + b}" }
    assertEquals("5.0", curryAdd(2)(3.0))

    assertEquals("6.0", uncurry(curryAdd)(2, 4.0))

    val compose = compose({ b: Int -> "${b + 1}" }, { a: Double -> a.toInt() * 2 })
    assertEquals("5", compose(2.0))
}