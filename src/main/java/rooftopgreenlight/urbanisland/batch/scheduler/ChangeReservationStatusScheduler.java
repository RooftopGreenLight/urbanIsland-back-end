package rooftopgreenlight.urbanisland.batch.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class ChangeReservationStatusScheduler {
    private final Job changeReservationStatusJob;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "0 0 0 * * *") // 자정 기준
    public void executeChangeReservationStatusJob() {
        try {
            jobLauncher.run(
                    changeReservationStatusJob,
                    new JobParametersBuilder().addString("currentDateTime", LocalDateTime.now().toString())
                            .toJobParameters()
            );
        } catch (JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e);
        } catch (JobRestartException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}
