package gie.yabb


import biz.enef.angulate.core.HttpPromise

import scala.concurrent.duration.Duration
import scala.concurrent.{CanAwait, Promise, ExecutionContext, Future}
import scala.util.{Failure, Success, Try}


class ConnectionException(val status:Int, val data: Any) extends Exception(s"status: ${status}, data: ${data}")


class HttpPromiseWrapper[T](wrapped: HttpPromise[T]) extends Future[T] {
  private val m_promise = Promise[T]()

  wrapped.success( ((t:T)=> m_promise.success(t)) )
  wrapped.error{ (data:Any, status:Int) =>
    m_promise.failure( new ConnectionException(status, data) )
  }

  lazy val future = m_promise.future

  override def onComplete[U](f: (Try[T]) => U)(implicit executor: ExecutionContext): Unit = future.onComplete(f)

  override def isCompleted: Boolean = m_promise.isCompleted

  override def value: Option[Try[T]] = future.value

  override def result(atMost: Duration)(implicit permit: CanAwait): T = future.result(atMost)

  override def ready(atMost: Duration)(implicit permit: CanAwait): this.type = {future.ready(atMost);this}
}


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
