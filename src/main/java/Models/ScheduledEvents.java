package Models;

import com.google.api.client.util.DateTime;
import lombok.Getter;
import lombok.Setter;

import javax.management.ConstructorParameters;

@Setter
@Getter
public class ScheduledEvents {

    private DateTime startdate;
    private DateTime enddate;
    private String summary;

    public ScheduledEvents(DateTime startdate , DateTime enddate , String summary){
        this.startdate = startdate;
        this.enddate = enddate;
        this.summary = summary;
    }

    public DateTime getStartdate(){
        return this.startdate;
    }

    public DateTime getEnddate(){
        return this.enddate;
    }

}
