package ru.gruzoff.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.gruzoff.entity.Order;
import ru.gruzoff.entity.User;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    public String completeRegistrationEmail(String secondName, String lastName, String role, String activationCode) {
        String message = String.format(
                "Здравствуйте, %s %s! \n" +
                        "Мы рады приветствовать вас на сайте грузоперевозок Gruzoff.\nМы только что получили запрос на регистрацию вас, в качестве " +
                        "%s . \n\n" +
                        "Для подтверждения регистрации, пожалуйста, перейдите по ссылке ниже: \n" +
                        "http://127.0.0.1:8080/api/auth/activate/" + activationCode + " \n\n\n\n\n\n\n" +
                        "Если это были не вы, просто проигнорируйте это письмо." +
                        "С уважением, \nКоманда проекта Gruzoff.",
                secondName, lastName, role
        );

        return message;
    }

    public String creationOrderNotify(User user, Order order) {
        String message = String.format(
                "Здравствуйте, %s %s! \n" +
                    "Вы создали новый заказ №%s Дата: %s. Время в пути заказа: %s"+
                    "От %s до %s .\n" + "Сумма заказа грузоперевозки %s \n" +
                    "Рады сообщить, что уже работаем с поиском исполнителей." +
                    "Как только мы найдем подходящих кандмдатов, наш менеджер с вами свяжется.\n\n\n\n" +
                    "С уважением, \nКоманда проекта Gruzoff.",
                user.getSecondName(), user.getLastName(), order.getId(),
                order.getOrderDetails().getDateTime(), order.getOrderDetails().getTimeOnOrder(),
                order.getOrderDetails().getAdressFrom().toString(),
                order.getOrderDetails().getAdressTo().toString(),
                order.getPrice()
        );

        return message;
    }

    public String acceptedOrderNotify(User user, Order order) {
        String message = String.format(
                "Здравствуйте, %s %s! \n" +
                        "Ваш заказ №%s успешно подтвержден.Дата: %s. Время в пути заказа: %s"+
                        "Путь от %s до %s .\n" + "Сумма заказа грузоперевозки %s \n" +
                        "Ваш заказ уже принят на исполнение. В назначенную дату () ожидайте наших сотрудников!\n\n\n\n" +
                        "С уважением, \nКоманда проекта Gruzoff.",
                user.getSecondName(), user.getLastName(), order.getId(),
                order.getOrderDetails().getDateTime(), order.getOrderDetails().getTimeOnOrder(),
                order.getOrderDetails().getAdressFrom().toString(),
                order.getOrderDetails().getAdressTo().toString(),
                order.getPrice()
        );

        return message;
    }
}