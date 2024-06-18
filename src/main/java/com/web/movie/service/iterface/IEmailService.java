package com.web.movie.service.iterface;

import java.util.Map;

public interface IEmailService {
    void sendMailConfirmRegistration(Map<String, String> data);

    void sendMailResetPassword(Map<String, String> data);

    void sendMailConfirmOrder(Map<String, Object> data);
}
