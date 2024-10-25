package com.pkteq.JPOSServer.task.participants;

import com.pkteq.JPOSServer.task.configs.SpringContext;
import com.pkteq.JPOSServer.task.entities.business.AccountBalanceDetail;
import com.pkteq.JPOSServer.task.entities.business.AccountDetail;
import com.pkteq.JPOSServer.task.repositories.AccountDetailRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;


@Transactional
public class ComputeBalanceParticipant implements TransactionParticipant {
    private AccountDetailRepo accountDetailRepo;

    public ComputeBalanceParticipant() {
        // Access the bean using the helper class
        this.accountDetailRepo = SpringContext.getBean(AccountDetailRepo.class);
    }
    @Transactional
    public Optional<AccountDetail> getAccountDetailWithTransactions(String accountNumber) {
        Optional<AccountDetail> accountDetail = accountDetailRepo.findAccountDetailByAccountNumber(accountNumber);
        accountDetail.ifPresent(acc -> Hibernate.initialize(acc.getTransactionDetails())); // Initialize lazy collection
        return accountDetail;
    }

    @Override
    public int prepare(long l, Serializable serializable) {
        Context context = (Context) serializable;
        ISOMsg msg = context.get("REQUEST_KEY");

        try {
            msg.setResponseMTI();

            String acc = msg.getString(2);
            Optional<AccountDetail> accountDetailOptional = accountDetailRepo.findAccountDetailByAccountNumber(acc);
            if (accountDetailOptional.isPresent()) {
                msg.set(39,"000");
                AccountDetail accountDetail = accountDetailOptional.get();
                AccountBalanceDetail accountBalanceDetail = accountDetail.getAccountBalanceDetails();
                msg.set(54,String.valueOf(accountBalanceDetail.getBalance()));
                context.put("RESPONSE_KEY",msg);
                return PREPARED;
            }
        }catch (Exception e) {

            System.out.println(e.getMessage());
            try {
                msg.setResponseMTI();
            } catch (ISOException ex) {
                throw new RuntimeException(ex);
            }
            msg.set(39,"907");
            context.put("RESPONSE_KEY",msg);
        }
        return ABORTED;
    }

    @Override
    public void commit(long id, Serializable serializable) {
        Log.getLog("Q2","participant").info("ComputeBalanceParticipant: commit");
        sendMessage((Context) serializable);
    }

    @Override
    public void abort(long id, Serializable serializable) {
        Log.getLog("Q2","participant").info("ComputeBalanceParticipant: abort");
        sendMessage((Context) serializable);
    }

    private void sendMessage(Context context){
        ISOSource source = context.get("RESOURCE_KEY");
        ISOMsg msgResp = context.get("RESPONSE_KEY");
        try {
            source.send(msgResp);
        } catch (IOException | ISOException e) {
            System.out.println(e.getMessage());
        }
    }
}
