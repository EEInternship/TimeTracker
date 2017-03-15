package Data;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Klemen on 13. 03. 2017.
 */

public class UploadRepository {
    public Time startingTime;
    public Time finishTime;
    public Time workingTime;
    public Time overHoursTime;
    public Time remoteTime;
    public String description;
    public Calendar date;

    public UploadRepository(){
    }

    public void setWorkingTime() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date date1 = format.parse(startingTime.toString());
        java.util.Date date2 = format.parse(finishTime.toString());
        long difference = date2.getTime() - date1.getTime();
        Time editedTime = new Time(difference);
        workingTime = new Time(editedTime.getHours()-1,editedTime.getMinutes(),editedTime.getSeconds());

        if(workingTime.getHours()>8){
            overHoursTime = new Time(workingTime.getHours()-8,workingTime.getMinutes(),workingTime.getSeconds());
            workingTime = new Time(8,0,0);
        }



    }
}
