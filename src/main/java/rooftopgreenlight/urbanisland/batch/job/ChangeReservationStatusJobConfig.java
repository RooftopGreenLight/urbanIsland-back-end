package rooftopgreenlight.urbanisland.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import rooftopgreenlight.urbanisland.domain.reservation.PaymentStatus;
import rooftopgreenlight.urbanisland.domain.reservation.Reservation;
import rooftopgreenlight.urbanisland.domain.reservation.ReservationStatus;
import rooftopgreenlight.urbanisland.repository.reservation.ReservationRepository;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class ChangeReservationStatusJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ReservationRepository reservationRepository;

    @Bean
    public Job changeReservationStatusJob(final Step changeReservationStatusStep) {
        return jobBuilderFactory.get("changeReservationStatusJob")
                .incrementer(new RunIdIncrementer())
                .start(changeReservationStatusStep)
                .build();
    }

    @Bean
    @JobScope
    public Step changeReservationStatusStep(
            final RepositoryItemReader<Reservation> reservationRepositoryItemReader,
            final ItemWriter<Reservation> reservationItemWriter,
            final ItemProcessor<Reservation, Reservation> reservationItemProcessor
    ) {
        return stepBuilderFactory.get("changeReservationStatusStep")
                .<Reservation, Reservation>chunk(100)
                .reader(reservationRepositoryItemReader)
                .processor(reservationItemProcessor)
                .writer(reservationItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Reservation> reservationRepositoryItemReader(
            @Value("#{jobParameters[currentDateTime]}") String currentDateTime
    ) {
        return new RepositoryItemReaderBuilder<Reservation>()
                .name("reservationRepositoryItemReader")
                .repository(reservationRepository)
                .methodName("findReservationsByEndDateBeforeAndReservationStatusAndPaymentStatus")
                .pageSize(100)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .arguments(LocalDateTime.parse(currentDateTime).toLocalDate(), ReservationStatus.WAITING, PaymentStatus.PAYMENT_COMPLETED)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Reservation, Reservation> reservationItemProcessor() {
        return item -> {
            item.changeReservationStatus(ReservationStatus.COMPLETED);
            return item;
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<Reservation> reservationItemWriter(
            final EntityManagerFactory entityManagerFactory
            ) throws Exception {
        JpaItemWriter<Reservation> itemWriter = new JpaItemWriterBuilder<Reservation>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();

        itemWriter.afterPropertiesSet();
        return itemWriter;
    }
}
