package Models;

import com.google.api.client.util.DateTime;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Slots {
    private DateTime startdate;
    private DateTime enddate;

    public DateTime getStartdate(){
        return this.startdate;
    }

    public DateTime getEnddate(){
        return this.enddate;
    }


    public Slots(DateTime startdate , DateTime  enddate){
        this.startdate = startdate;
        this.enddate = enddate;
    }
}
