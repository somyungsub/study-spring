package common;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DebugSubscriber<T> implements Subscriber<T> {

  private String label;

  public DebugSubscriber() {
  }

  public DebugSubscriber(String label) {
    this.label = label;
  }

  @Override
  public void onSubscribe(Subscription subscription) {
    subscription.request(Long.MAX_VALUE);
  }

  @Override
  public void onNext(T t) {
    String name = Thread.currentThread().getName();
    System.out.println(name + " : " + t);
  }

  @Override
  public void onError(Throwable throwable) {
    String name = Thread.currentThread().getName();
    System.out.println(name + " = " + throwable);
  }

  @Override
  public void onComplete() {
    String name = Thread.currentThread().getName();
    System.out.println(name + " : 완료!!");
  }
}
