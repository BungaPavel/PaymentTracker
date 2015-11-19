package tb.paymentTracker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PaymentTracker {
	
	private static ConcurrentHashMap<String, Integer> paymentMap;
	
	private static String initialFile = "";
	
	private static final String EXIT_CODE = "quit";
	private static final String END_FILE = ".txt";
	
	 private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Not enough parameters");
			return;
		} else {
				initialFile = args[0];
		}
		System.out.println("Initial file is " + initialFile);
		PaymentTracker paymentTracker = new PaymentTracker();
		paymentTracker.run();
	}
	
	public void run() {
		paymentMap = new ConcurrentHashMap<String, Integer>();
		String path = "c:\\projects\\TB\\aa\\" + initialFile;
		readFile(path);
		
		initScheduler();
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			String payment = sc.nextLine();
			if (payment.equalsIgnoreCase(EXIT_CODE)) {
				break;
			} else if (payment.endsWith(END_FILE)) {
				
				readFile("c:\\projects\\TB\\aa\\" + payment);
				continue;
			} 
			String errorMessage = addToMap(payment, paymentMap);
			if (errorMessage != null) {
				System.out.println(errorMessage);
			}
		}
		scheduler.shutdown();
	}
	
	
	public void initScheduler() {
		
		final Runnable writer = new Runnable() {
			public void run() {
				for (Entry<String, Integer> entry : paymentMap.entrySet()) {
					if (entry.getValue() != 0) {
						System.out.println(entry.getKey() + " " + entry.getValue());
					}
				}
			}
		};
		
		final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(writer, 5, 5, TimeUnit.SECONDS);
	}
	
	private void readFile(String path) {
		FileInputStream file;
		boolean errorOccured = false;
		ConcurrentHashMap<String, Integer> temporaryMap = new ConcurrentHashMap<String, Integer>();
		try {
			file = new FileInputStream(path);
			
			String line;
			InputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			int lineCounter = 1;
			while ((line = br.readLine()) != null) {
				String errorMessage = addToMap(line, temporaryMap);
				if (errorMessage != null) {
					System.out.println("Line " + lineCounter + ": " + errorMessage);
					errorOccured = true;
				}
				lineCounter++;
			}
			file.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error while reading file");
		}
		
		if (!errorOccured) {
			for (Entry<String, Integer> entry : temporaryMap.entrySet()) {
				if (paymentMap.get(entry.getKey()) == null) {
					paymentMap.put(entry.getKey(), entry.getValue());
				} else {
					paymentMap.put(entry.getKey(), entry.getValue() + paymentMap.get(entry.getKey()));
				}
			}
		}
	}
	
	/**
	 * Return error message or null
	 * @param payment input from console or file
	 * @return
	 */
	private String addToMap(String payment, ConcurrentHashMap<String, Integer> map) {
		String validateError = validatePayment(payment);
		if (validateError != null) {
			return validateError;
		}
		String[] paymentParts = payment.split(" ");
		String currency = paymentParts[0].trim();
		String value = paymentParts[1].trim();
		
		Integer oldValue = map.get(currency) == null ? 0 : map.get(currency);
		map.put(currency, oldValue + Integer.parseInt(value));
		return null;
	}
	
	private String validatePayment(String payment) {
		String[] paymentParts = payment.split(" ");
		if (paymentParts.length == 0) {
			return "No input";
		} else if (paymentParts.length > 2) {
			return "There is too much information for payment: " + payment;
		} else if (paymentParts.length < 2) {
			return "Missing value for currency: " + payment;
		}
		String currency = paymentParts[0].trim();
		String value = paymentParts[1].trim();
		if (currency.length() != 3) {
			return "Currency must be 3 character long -> " + currency;
		} else if (!currency.equals(currency.toUpperCase())) {
			return "Currency must be upper case ->" + currency;
		}
		try {
			Integer.parseInt(value);
		} catch (Exception e) {
			return "Value for payment isn´t number -> " + value;
		}
		return null;
	}
}
