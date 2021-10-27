package ru.diasoft.micro.service;

import lombok.AllArgsConstructor;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.diasoft.micro.domain.*;
import ru.diasoft.micro.repository.SmsVerificationRepository;
import ru.diasoft.micro.util.Status;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;


@AllArgsConstructor
@Service("smsVerificationSecond")
@Primary
public class SmsVerificationServiceImpl implements SmsVerificationService {

    private final SmsVerificationRepository smsVerificationRepository;

    @Override
    public SmsVerificationCheckResponse dsSmsVerificationCheck(SmsVerificationCheckRequest request) {
        Optional<SmsVerification> smsVerification = smsVerificationRepository.findBySecretCodeAndProcessGuid(request.getCode(), request.getProcessGUID());
        SmsVerificationCheckResponse response = new SmsVerificationCheckResponse();
        response.setCheckResult(smsVerification.isPresent());
        return response;
    }

    @Override
    public SmsVerificationPostResponse dsSmsVerificationCreate(SmsVerificationPostRequest request) {
        String processGUID = UUID.randomUUID().toString();
        String secretCode = String.format("%04d", new Random().nextInt(10000));
        SmsVerification smsVerification = SmsVerification.builder()
                .phoneNumber(request.getPhoneNumber())
                .processGuid(processGUID)
                .status(Status.OK.name())
                .secretCode(secretCode)
                .build();

        smsVerificationRepository.save(smsVerification);
        SmsVerificationPostResponse response = new SmsVerificationPostResponse();
        response.setProcessGUID(processGUID);
        return response;
    }
}
