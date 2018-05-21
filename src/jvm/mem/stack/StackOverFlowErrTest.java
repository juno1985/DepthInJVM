package jvm.mem.stack;
/**
 * VM Args: -Xss128k
 * @author juno
 * 在递归中无限创建方法栈，最终会导致StackOverFlow
 */
public class StackOverFlowErrTest {
	
	private int stackLength = 1;
	public void stackLeak() {
		stackLength++;
		stackLeak();
	}

	public static void main(String[] args) {
		StackOverFlowErrTest sof = new StackOverFlowErrTest();
		try {
			sof.stackLeak();
		}catch(Throwable e) {
			System.out.println("stack length: " + sof.stackLength);
			throw e;
		}
	}

}


