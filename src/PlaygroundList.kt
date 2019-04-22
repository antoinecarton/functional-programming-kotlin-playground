package io.acarton.fp.playground

import io.acarton.fp.playground.List.Companion.drop
import io.acarton.fp.playground.List.Companion.dropWhile
import io.acarton.fp.playground.List.Companion.foldRight
import io.acarton.fp.playground.List.Companion.setHead
import io.acarton.fp.playground.List.Companion.tail
import kotlin.test.assertEquals

typealias Cons<A> = List.Cons<A>
typealias Nil = List.Nil

sealed class List<out A> {
    object Nil : List<Nothing>()
    data class Cons<out A>(val head: A, val tail: List<A>) : List<A>()

    companion object {
        fun <A, B> foldRight(l: List<A>, b: B, f: (A, B) -> B): B = when (l) {
            is Nil -> b
            is Cons<A> -> f(l.head, foldRight(l.tail, b, f))
        }

        fun <A> tail(l: List<A>): List<A> = when (l) {
            Nil -> Nil
            is Cons<A> -> l.tail
        }

        fun <A> setHead(elem: A, l: List<A>): List<A> = when (l) {
            is Nil -> Cons(elem, Nil)
            is Cons<A> -> Cons(elem, l.tail)
        }

        fun <A> drop(l: List<A>, n: Int): List<A> = if (n <= 0) {
            l
        } else {
            when (l) {
                is Nil -> Nil
                is Cons<A> -> drop(l.tail, n - 1)
            }
        }

        fun <A> dropWhile(l: List<A>, f: (A) -> Boolean): List<A> = when (l) {
            is Cons<A> -> if (f(l.head)) dropWhile(l.tail, f) else l
            else -> l
        }
    }
}

fun main() {
    val l = Cons(1, Cons(2, Nil))
    assertEquals(Cons(2, Nil), l.tail)
    assertEquals(Cons(2, Nil), tail(l))
    assertEquals(1, l.head)
    assertEquals(Nil, tail(Nil))
    assertEquals(Cons(0, Nil), setHead(0, Nil))
    assertEquals(Cons(0, Cons(2, Nil)), setHead(0, l))
    assertEquals(Cons(2, Nil), drop(Cons(0, l), 2))
    assertEquals(Nil, drop(Nil, 2))
    assertEquals(Cons(2, Nil), dropWhile(Cons(0, l)) { it < 2 })
    assertEquals(Nil, dropWhile(Cons(0, Nil)) { it < 2 })
    assertEquals(13, foldRight(l, 10) { a, b -> a + b})
    assertEquals(10, foldRight(Nil, 10) { a: Int, b: Int -> a + b})
}