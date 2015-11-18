package tb.paymentTracker;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class Main {
	public static void main( String[] args ) {
		Main main = new Main();
		main.run();
	}
	
	public void run() {
		initScheduler();
	}
	
	public void initScheduler() {

		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("eachMinute", "group1")
				.withSchedule(
				    SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(5).repeatForever())
				.build();
		
		JobDetail job = JobBuilder.newJob(WriterJob.class)
				.withIdentity("writeJob", "group1").build();
		
		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			
			Thread.sleep(90L * 1000L);
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
			//sched.shutdown(true);
			
	}
	
}
