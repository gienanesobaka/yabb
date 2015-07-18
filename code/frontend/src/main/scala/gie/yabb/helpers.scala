package gie.yabb

import biz.enef.angulate.core.HttpPromise
import biz.enef.angulate.core.impl.HttpPromiseWrapper

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object YHelpers {
  def tryToFuture[A](t: => Try[A])(implicit executor: ExecutionContext): Future[A] = {
    Future {
      t
    }.flatMap {
      case Success(s) => Future.successful(s)
      case Failure(fail) => Future.failed(fail)
    }
  }

  def futureFromJs[T](jsF: HttpPromise[T]) = (new HttpPromiseWrapper[T](jsF))
}
