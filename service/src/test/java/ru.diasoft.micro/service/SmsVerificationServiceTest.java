package ru.diasoft.micro.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.diasoft.micro.domain.*;
import ru.diasoft.micro.repository.SmsVerificationRepository;
import ru.diasoft.micro.util.Status;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsVerificationServiceTest {

    private final String PHONE_NUMBER = "+891565510020";
    private final String VALID_SECRET_CODE = "5698";
    private final String INVALID_SECRET_CODE = "5020";
    private final String GUID = UUID.randomUUID().toString();

    @Mock
    private SmsVerificationRepository repository;

    private SmsVerificationService smsService;


    @Before
    public void init() {
        smsService = new SmsVerificationServiceImpl(repository);

        SmsVerification smsVerification = SmsVerification.builder()
                .processGuid(java.util.UUID.randomUUID().toString())
                .phoneNumber(PHONE_NUMBER)
                .secretCode(VALID_SECRET_CODE)
                .status(Status.OK.name())
                .build();
        when(repository.findBySecretCodeAndProcessGuid(VALID_SECRET_CODE, GUID)).thenReturn(Optional.of(smsVerification));
        when(repository.findBySecretCodeAndProcessGuid(INVALID_SECRET_CODE, GUID)).thenReturn(Optional.empty());
    }

    @Test
    public void dsSmsVerificationCheck_Valid() throws Exception {
        SmsVerificationCheckRequest request = new SmsVerificationCheckRequest();
        request.setCode(VALID_SECRET_CODE);
        request.setProcessGUID(GUID);
        SmsVerificationCheckResponse response = smsService.dsSmsVerificationCheck(request);
        assertThat(response.getCheckResult()).isEqualTo(true);
    }

    @Test
    public void dsSmsVerificationCheck_Invalid() throws Exception {
        SmsVerificationCheckRequest request = new SmsVerificationCheckRequest();
        request.setCode(INVALID_SECRET_CODE);
        request.setProcessGUID(GUID);
        SmsVerificationCheckResponse response = smsService.dsSmsVerificationCheck(request);
        assertThat(response.getCheckResult()).isEqualTo(false);
    }

    @Test
    public void dsSmsVerificationCreate_Post() throws Exception {
        SmsVerificationPostRequest request = new SmsVerificationPostRequest();
        request.setPhoneNumber(PHONE_NUMBER);
        SmsVerificationPostResponse response = smsService.dsSmsVerificationCreate(request);
        assertThat(response.getProcessGUID()).isNotEmpty();
    }
}
