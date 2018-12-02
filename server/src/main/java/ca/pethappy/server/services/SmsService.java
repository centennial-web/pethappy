package ca.pethappy.server.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;

@Service
public class SmsService {
    private static final int MAX_VERIFICATION_CODE = 100000;
    private static final int MIN_VERIFICATION_CODE = 999999;

    @Value("${twilio.account-sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth-token}")
    private String twilioAuthToken;

    @Value("${twilio.sender-phone-number}")
    private String twilioSenderPhoneNumber;

    @PostConstruct
    public void init() {
        Twilio.init(twilioAccountSid, twilioAuthToken);
    }

    public void sendSms(String number, String message) {
        Message m = Message.creator(new PhoneNumber(number), new PhoneNumber(twilioSenderPhoneNumber), message).create();
        m.getSid();
    }

    public String generateVerificationCode() {
        Random rand = new Random();
        int code = rand.nextInt(MIN_VERIFICATION_CODE - MAX_VERIFICATION_CODE + 1) + MAX_VERIFICATION_CODE;
        return Integer.toString(code);
    }
}
