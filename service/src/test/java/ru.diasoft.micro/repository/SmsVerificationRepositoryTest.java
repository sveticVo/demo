package ru.diasoft.micro.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.junit4.SpringRunner;
import ru.diasoft.micro.domain.SmsVerification;
import ru.diasoft.micro.util.Status;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsVerificationRepositoryTest {

    @Autowired
    private SmsVerificationRepository repository;


    @Test
    public void smsVerificationCreateTest() {
        SmsVerification smsVerification = SmsVerification.builder()
                .processGuid(java.util.UUID.randomUUID().toString())
                .phoneNumber("89204557896")
                .secretCode("1289")
                .status(Status.OK.name())
                .build();

        SmsVerification smsVerificationCreated = repository.save(smsVerification);
        assertThat(smsVerification).isEqualToComparingOnlyGivenFields(smsVerificationCreated, "verificationID");
        assertThat(smsVerificationCreated.getVerificationID()).isNotNull();

    }

    @Test
    public void findBySecretCodeAndProcessGuidAndStatusTest() throws Exception {
        String secretCode = "1289";
        String processGuid = java.util.UUID.randomUUID().toString();
        SmsVerification smsVerification = SmsVerification.builder()
                .processGuid(processGuid)
                .phoneNumber("89204557896")
                .secretCode(secretCode)
                .status(Status.OK.name())
                .build();

        SmsVerification smsVerificationCreated = repository.save(smsVerification);

        assertThat(repository.findBySecretCodeAndProcessGuid(secretCode, processGuid)
                             .orElse(SmsVerification.builder().build()))
                .isEqualTo(smsVerificationCreated);
        assertThat(repository.findBySecretCodeAndProcessGuid("5252", processGuid)).isEmpty();
    }
}
