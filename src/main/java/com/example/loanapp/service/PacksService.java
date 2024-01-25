package com.example.loanapp.service;

import com.example.loanapp.exeptions.EmailExceptionByServer;
import com.example.loanapp.model.Packs;
import com.example.loanapp.model.ParcelLocker;
import com.example.loanapp.model.Status;
import com.example.loanapp.model.User;
import com.example.loanapp.repo.PacksRepo;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PacksService {
    private final PacksRepo packsRepo;
    private final UserService userService;
    private final ParcelLockerService parcelLockerService;

    private final EmailService emailService;

    public PacksService(PacksRepo packsRepo,
                        UserService userService,
                        ParcelLockerService parcelLockerService,
                        EmailService emailService) {
        this.packsRepo = packsRepo;
        this.userService = userService;
        this.parcelLockerService = parcelLockerService;
        this.emailService = emailService;
    }

    @Transactional
    public void sendParcel(Packs pack, User user) throws MessagingException {
        ParcelLocker parcelLocker = parcelLockerService.findFreeParcelLocker(pack.getSize());
        pack.setParcelLocker(parcelLocker);
        pack.setPickupCode(createPickupCode());
        pack.setUser(userService.findById(user.getId()));
        pack.setEmailSender(user.getEmail());
        pack.setDateOfPosting(LocalDateTime.now());
        pack.setExpirationDate(LocalDateTime.now().plusDays(7));
        pack.setStatus(Status.TO_RECEIVE);
        pack.setReminderMessageSend(false);
        parcelLockerService.updateStatusParcelLocker(parcelLocker.getId(), false);
        packsRepo.save(pack);

        emailService.sendEmailWhenPackIsSending(pack.getEmailReceiver(), pack.getPickupCode());




    }
    public String createPickupCode(){
        int size =8;
        String ascii = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return new Random().ints(size,0,ascii.length())
                .mapToObj(ascii::charAt)
                .collect(StringBuilder::new,StringBuilder::append,StringBuilder::append)
                .toString();
    }
    public List<Packs> checkExpirationDates() {
        return packsRepo.findByExpirationDateLessThan(LocalDateTime.now().plusDays(3))
                .stream().filter(reminder-> reminder.getReminderMessageSend()!=null
                        && reminder.getStatus().equals(Status.TO_RECEIVE))
                .collect(Collectors.toList());


    }

    @Scheduled(fixedRate = 3*60*60*1000) //co 3 godziny sprawdzenie i wysłanie przypomnienia
    public void sendReminder(){
       List <Packs> packsToReminder= checkExpirationDates();
       packsToReminder.stream().forEach(packs -> {
           try {
               emailService.sendReminderEmail(packs);
               packs.setReminderMessageSend(true);
           } catch (MessagingException e) {
               throw new EmailExceptionByServer();
           }
       });


    }

    @Transactional
    public String receivePack(String pickupCode){
        Packs receivePack = packsRepo.findByPickupCode(pickupCode);
        if(checkPackStatus(receivePack).equals(true)) {
            receivePack.setStatus(Status.RECEIVE);
            packsRepo.save(receivePack);
            parcelLockerService.updateStatusParcelLocker(receivePack.getParcelLocker().getId(), true);
            return "Paczka odebrana pomyślnie :))";
        }else {
            return "Paczka została już odebrana lub upłynęła data ważności";
        }
    }
    public Boolean checkPackStatus(Packs pack){
        Predicate<Packs> isNull= p -> p ==null;
        Predicate<Packs> isAlreadyReceived= p ->"receive".equals(p.getStatus());
        Predicate<Packs> isExpired= p-> LocalDateTime.now().isAfter(p.getExpirationDate());
        if (isNull.or(isAlreadyReceived).or(isExpired).test(pack)) {
            return false;
        }
        return true;
    }



}
