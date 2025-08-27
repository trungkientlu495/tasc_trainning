package ntk.project;

public class Logger {
    // phai co static vi neu khong thi khi new object thi no se tao cac instance rieng
    private static Logger logger;

    private Logger() {

    }

    // thread safe
    public static Logger getLogger() {
        synchronized (Logger.class) {
            if (logger == null) {
                logger = new Logger();
            }
        }
        return logger;
    }

    // sout log
    public void log(String message) {
        System.out.println("Test write log by signleton: "+message);
    }
}
