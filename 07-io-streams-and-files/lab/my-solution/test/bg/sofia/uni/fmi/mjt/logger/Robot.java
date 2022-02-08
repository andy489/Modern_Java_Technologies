package bg.sofia.uni.fmi.mjt.logger;

import java.time.LocalDateTime;

public class Robot {
    private final Logger logger;

    private String name;
    private int battery;

    Robot(Logger logger, String name) {
        this.logger = logger;
        this.battery = 10;
        this.name = name;

        String info = String.format("Robot %s is created at full energy of %d.", name, 10);
        logger.log(Level.INFO, LocalDateTime.now(), info);
    }

    private void logEnergy() {
        String debug = String.format("%s current energy: %d.", name, battery);
        logger.log(Level.DEBUG, LocalDateTime.now(), debug);
    }

    public void recharge() {
        if (battery == 10) {
            String warn = String.format("%s is already at full energy.", name);
            logger.log(Level.WARN, LocalDateTime.now(), warn);
        } else {
            String info = String.format("%s is recharged.", name);
            logger.log(Level.INFO, LocalDateTime.now(), info);
            battery = 10;
        }
    }

    public void work() {
        if (battery >= 4) {
            String info = String.format("%s works.", name);
            logger.log(Level.INFO, LocalDateTime.now(), info);
            battery -= 4;
            if (battery <= 3) {
                String warn = String.format("%s needs to recharge. Energy: %d.", name, battery);
                logger.log(Level.WARN, LocalDateTime.now(), warn);
            }
        } else {
            String error = String.format("%s fails to work. Please recharge.", name);
            logger.log(Level.ERROR, LocalDateTime.now(), error);
            logEnergy();
        }
    }

    public Logger getLogger() {
        return logger;
    }
}
