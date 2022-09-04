package rooftopgreenlight.urbanisland.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rooftopgreenlight.urbanisland.domain.reservation.entity.Reservation;

@Configuration
@RequiredArgsConstructor
public class ChangeReservationStatusJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job changeReservationStatusJob(final Step changeReservationStatusStep) {
        return jobBuilderFactory.get("changeReservationStatusJob")
                .incrementer(new RunIdIncrementer())
                .start(changeReservationStatusStep)
                .build();
    }

    @Bean
    @JobScope
    public Step changeReservationStatusStep() {
        return stepBuilderFactory.get("changeReservationStatusStep")
                .<Reservation, Void>chunk(100)
                .reader()
                .processor()
                .writer()
                .build();
    }
}
