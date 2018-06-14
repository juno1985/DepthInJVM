package simulation.blocking.queue;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class MyQueue<E> {
	//��������Ϊ0
	private final int min = 0;
	//����������ͨ����������ֵ
	private final int max;
	//��¼������Ԫ�ظ���,��֤�������Լ���ԭ�Ӳ���
	private AtomicInteger count = new AtomicInteger(0);
	//�����ڴ洢��Ԫ��
	private final LinkedList<E> eArray;
	//�����ߺ�������ʹ�õ���
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
			//���������ѭ��˵������Ԫ�ظ���С��max
			eArray.add(e);
			count.incrementAndGet();
			//֪ͨ������
			obj.notify();
			return eArray;
		}
	}
	
	public E takeAnElement() throws InterruptedException {
		synchronized(obj) {
			while(count.get() == min) {
				obj.wait();
			}
			//�������ѭ��˵����������Ԫ�ؿ�ȡ
			E e = eArray.removeFirst();
			count.decrementAndGet();
			//֪ͨ������
			obj.notify();
			return e;
		}
	}

}
