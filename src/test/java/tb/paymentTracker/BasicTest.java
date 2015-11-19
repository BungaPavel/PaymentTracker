package tb.paymentTracker;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.Test;


public class BasicTest {
	
	@Test
	public void testInsesrt() {
		PaymentTracker paymentTracker = new PaymentTracker();
		String first = "USD 100";
		String second = "XXX 50";
		String third = "USD -50";
		ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();

		executeAddToMap(first, map, paymentTracker);
		executeAddToMap(second, map, paymentTracker);
		executeAddToMap(third, map, paymentTracker);
		
		Assert.assertEquals(map.get("USD").toString(), "50");
		Assert.assertEquals(map.get("XXX").toString(), "50");
	}
	
	@Test
	public void testInsertBadInput() {
		PaymentTracker paymentTracker = new PaymentTracker();
		String first = "USD 100";
		String second = "xxx 50";
		String third = "USD -50z";
		ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();

		executeAddToMap(first, map, paymentTracker);
		executeAddToMap(second, map, paymentTracker);
		executeAddToMap(third, map, paymentTracker);
		
		Assert.assertEquals(map.get("USD").toString(), "100");
		Assert.assertEquals(map.size(), 1);
	}
	
	private void executeAddToMap(String payment, ConcurrentHashMap<String, Integer> map, PaymentTracker paymentTracker) {
		Class[] cArg = new Class[2];
		cArg[0] = String.class;
		cArg[1] = ConcurrentHashMap.class;
		Class PaymentTrackerClass = PaymentTracker.class;
		Method method;
		try {
			method = PaymentTrackerClass.getDeclaredMethod("addToMap", cArg);
			method.setAccessible(true);
			method.invoke(paymentTracker, payment, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
