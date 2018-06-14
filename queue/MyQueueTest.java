package simulation.blocking.queue;

public class MyQueueTest {
	
	public static void main(String[] args) throws InterruptedException {
		
		MyQueue<String> myQueue = new MyQueue<String>(5);
		
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
					Integer i = 1;
					while(i<10) {
						try {
							myQueue.addElement(Integer.toString(i));
							System.out.println("���������Ԫ��: " + Integer.toString(i));
							i++;
							Thread.currentThread();
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
			}
		},"producer");
		
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
					Integer i = 1;
					while(i<10) {
						try {
							String ret = myQueue.takeAnElement();
							System.out.println("������ȡ��Ԫ��: " + ret);
							i++;
							Thread.currentThread();
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
			}
		},"consumer");
		
		t1.start();
		Thread.currentThread();
		Thread.sleep(8000);
		t2.start();
	}

}
