package tb.paymentTracker;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class WriterJob implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("UUU 1000");
	}

}
