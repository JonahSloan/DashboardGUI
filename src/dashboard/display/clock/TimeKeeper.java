package dashboard.display.clock;

import java.text.SimpleDateFormat;
import java.util.Date;
import dashboard.CommonConsts;
import dashboard.display.ScheduledTask;
import dashboard.interfaces.Destroyable;

/**
 * Keeps track of the current time.
 * All instances of this class will contain the same time.
 * @author jonah.sloan
 * @author jenna3715
 *
 */
public final class TimeKeeper implements Destroyable
{
	/**
	 * This is consistant across all TimeKeeper objects
	 * automatically increments by {@link dashboard.CommonConsts#tupdateint tupdateint}
	 */
	protected static volatile long t=0;
	private long lastsync=0;
	private long offset=0;
	private static boolean syncsucceeded=false;
	private static ScheduledTask timer;
	private static ScheduledTask timer2;
	private static boolean timerset=false;
	public String strformat;
	/**Constructs a new TimeKeeper object with the specified format
	 * Formats
	 * <pre>
	 * | Letter | Description                       | Type              | Example                               |
	 * +--------+-----------------------------------+-------------------+---------------------------------------+
	 * |   G    | Era designator                    | Text              | AD                                    |
	 * |   y    | Year                              | Year              | 1996; 96                              |
	 * |   Y    | Week year                         | Year              | 2009; 09                              |
	 * |   M    | Month in year (context sensitive) | Month             | July; Jul; 07                         |
	 * |   L    | Month in year (standalone form)   | Month             | July; Jul; 07                         |
	 * |   D    | Day in year                       | Number            | 189                                   |
	 * |   d    | Day in month                      | Number            | 10                                    |
	 * |   E    | Day name in week                  | Text              | Tuesday; Tue                          |
	 * |   a    | Am/pm marker                      | Text              | PM                                    |
	 * |   H    | Hour in day (0-23)                | Number            | 0                                     |
	 * |   k    | Hour in day (1-24)                | Number            | 24                                    |
	 * |   K    | Hour in am/pm (0-11)              | Number            | 0                                     |
	 * |   h    | Hour in am/pm (1-12)              | Number            | 12                                    |
	 * |   m    | Minute in hour                    | Number            | 30                                    |
	 * |   s    | Second in minute                  | Number            | 55                                    |
	 * |   z    | Time zone                         | General time zone | Pacific Standard Time; PST; GMT-08:00 |
	 * </pre>
	 *
	 * Examples:<br><pre>
	 * | Format String         | Output                 |
	 * +-----------------------+------------------------+
	 * | "d/m/y"               | 25/3/16                |
	 * | "EEEE, MMMMM d, yyyy" | Friday, March 25, 2016 |
	 * | "h:mm a"              | 3:57 PM                |
	 * | "h:mm:ss a"           | 3:57:26 PM             |
	 * </pre>
	 */
	public TimeKeeper(String format)
	{
		this.strformat=format;
		//t=new Date();
		if(!timerset)
		{
			timer=new ScheduledTask(CommonConsts.ZERO, CommonConsts.tsynccheck){
				public void run(){
					CommonConsts.log.println("Checking the time...");
					synctime();
					CommonConsts.log.println("Time check complete.");
				}
			};
			timer2=new ScheduledTask(CommonConsts.ZERO, CommonConsts.tupdateint){
				public void run(){
					t=t+CommonConsts.tupdateint;
				}
			};
			timerset=true;
		}
	}
	public String toString()
	{
		return new SimpleDateFormat(strformat).format(new Date(t));
	}
	/**<b>Don't ask the server too often</b>
	 **/
	private void synctime()
	{
		//hard coded minimum times 1h, 15m
		if(lastsync+(syncsucceeded?Math.max(CommonConsts.tsyncsucceeded,CommonConsts.HOUR):
			Math.max(CommonConsts.tsyncfailed,CommonConsts.MINUTE*15)) < System.currentTimeMillis()+offset)
		{
			try
			{
				long locoffset = System.currentTimeMillis()+(offset = SntpClient.getSystemTimeOffset(CommonConsts.timeserver));
				t=locoffset+CommonConsts.timeoffset;
				lastsync=System.currentTimeMillis();
				syncsucceeded=true;
			}
			catch(Exception e)
			{
				syncsucceeded=false;
			}
		}
	}
	@Override
	public void destroy()
	{
		timer.cancel();
		timer2.cancel();
	}
	private long getTimeRaw()
	{
		return t;
	}
	public static long getTime()
	{
		if(!timerset)
			new TimeKeeper(null).getTimeRaw();
		return t;
	}
}//TimeKeeper