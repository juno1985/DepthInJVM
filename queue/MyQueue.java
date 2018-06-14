package simulation.blocking.queue;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class MyQueue<E> {
	//队列最少为0
	private final int min = 0;
	//队列最大可以通过构造器赋值
	private final int max;
	//记录队列中元素个数,保证自增或自减的原子操作
	private AtomicInteger count = new AtomicInteger(0);
	//队列内存储的元素
	private final LinkedList<E> eArray;
	//消费者和生产者使用的锁
	private final Object obj = new Object();
	
	public MyQueue(int max) {
		this.max = max;
		eArray = new LinkedList<E>();
	}
	
	public LinkedList<E> addElement(E e) throws InterruptedException {
		synchronized(obj) {
			while(count.get() == max) {
				obj.wait();
			}
			//如果能跳出循环说明队列元素个数小于max
			eArray.add(e);
			count.incrementAndGet();
			//通知消费者
			obj.notify();
			return eArray;
		}
	}
	
	public E takeAnElement() throws InterruptedException {
		synchronized(obj) {
			while(count.get() == min) {
				obj.wait();
			}
			//如果跳出循环说明队列中有元素可取
			E e = eArray.removeFirst();
			count.decrementAndGet();
			//通知生产者
			obj.notify();
			return e;
		}
	}

}
