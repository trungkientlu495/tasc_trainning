package ntk.project.utills;

import java.time.LocalDateTime;
import java.util.Set;

public abstract class DateTimeParser {
    public abstract LocalDateTime parseDataLogFile(String time) throws InterruptedException;
}
