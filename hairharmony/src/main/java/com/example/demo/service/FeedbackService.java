package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.Manager;
import com.example.demo.entity.ServiceEntity;
import com.example.demo.model.FeedbackRequest;
import com.example.demo.model.FeedbackResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.ServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ServiceRepository serviceRepository;


    public Feedback createNewFeedback(FeedbackRequest feedbackRequest) {
        Account stylist = accountRepository.findById(feedbackRequest.getStylistId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thợ!"));

        ServiceEntity service = serviceRepository.findById(feedbackRequest.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy dịch vụ!"));

        Feedback feedback = new Feedback();
        feedback.setContent(feedbackRequest.getContent());
        feedback.setRating(feedbackRequest.getRating());
        feedback.setCustomer(authenticationService.getCurrentAccount());
        feedback.setService(service);
        feedback.setStylist(stylist);
        return feedbackRepository.save(feedback);
    }

    public List<FeedbackResponse> getFeedback(){
        return feedbackRepository.findFeedbackByStylistId(authenticationService.getCurrentAccount().getId());
    }


    public Feedback deleteFeedback(long id) {
        Feedback feedback = feedbackRepository.findFeedbackById(id);
        feedback.setDeleted(true);
        return feedbackRepository.save(feedback);
    }
}
