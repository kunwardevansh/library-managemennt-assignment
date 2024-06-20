package librarymanagementsystem.assignment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import librarymanagementsystem.assignment.entity.OrderDetail;
import librarymanagementsystem.assignment.entity.User;
import librarymanagementsystem.assignment.model.Mail;
import librarymanagementsystem.assignment.repository.OrderDetailRepository;

@Service
public class ReminderService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
	private MailService mailService;

    @Scheduled(cron = "0 0 9 * * *")
    public void sendReminders(){
        try{
            LocalDateTime today = LocalDateTime.now();
            LocalDateTime dueSoonDate = today.plusDays(3);

            Optional<List<OrderDetail>> foundUnreturnedOrderDetails = orderDetailRepository.findAllUnreturnedOrderDetails();
            
            if(!foundUnreturnedOrderDetails.isPresent()){
                System.out.println("No reminder mails required to be sent. Date: " + today);
            }
            List<OrderDetail> unreturnedOrderDetails = foundUnreturnedOrderDetails.get();

            Map<User, List<OrderDetail>> orderDetailsByUser = unreturnedOrderDetails.stream()
                .collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getUser()));

            for (Map.Entry<User, List<OrderDetail>> entry : orderDetailsByUser.entrySet()) {
                User user = entry.getKey();
                List<OrderDetail> overdueOrders = entry.getValue().stream()
                    .filter(orderDetail -> orderDetail.getDue_date().isBefore(today))
                    .collect(Collectors.toList());

                List<OrderDetail> dueSoonOrders = entry.getValue().stream()
                    .filter(orderDetail -> orderDetail.getDue_date().isBefore(dueSoonDate) && !overdueOrders.contains(orderDetail))
                    .collect(Collectors.toList());

                if (!overdueOrders.isEmpty() || !dueSoonOrders.isEmpty()) {
                    remindUser(user, overdueOrders, dueSoonOrders);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to run reminder service",e);
        }
    }

    public void remindUser(User user, List<OrderDetail> overdueOrders, List<OrderDetail> dueSoonOrders){
        try{
            String subject = "Library Due Date Reminder";
            StringBuilder message = new StringBuilder("Dear user,\n\n"); 

            if (!overdueOrders.isEmpty()) {
                message.append("The following products are overdue:\n");
                message.append("<table><tr><th>Title</th><th>Due Date</th></tr>");
                for (OrderDetail detail : overdueOrders) {
                    message.append("<tr><td>").append(detail.getProduct().getName())
                           .append("</td><td>").append(detail.getDue_date()).append("</td></tr>");
                }
                message.append("</table><br>");
            }

            if (!dueSoonOrders.isEmpty()) {
                message.append("The following books are due in 2 days:\n");
                message.append("<table><tr><th>Title</th><th>Due Date</th></tr>");
                for (OrderDetail detail : dueSoonOrders) {
                    message.append("<tr><td>").append(detail.getProduct().getName())
                           .append("</td><td>").append(detail.getDue_date()).append("</td></tr>");
                }
                message.append("</table><br>");
            }

            message.append("Please return the products on time to avoid fines.\n\n");
            message.append("Thank you");

            Mail mail = new Mail();

            mail.setMailTo(user.getEmail());
            mail.setMailFrom("kunwardevansh@rocketmail.com");
            mail.setMailSubject(subject);
            mail.setMailContent(message.toString());
		    mailService.sendEmail(mail, user, "reminder_mail");
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to send reminder email",e);
        }
    }
}
