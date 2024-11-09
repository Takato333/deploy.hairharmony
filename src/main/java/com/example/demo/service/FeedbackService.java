package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.model.FeedbackRequest;
import com.example.demo.model.FeedbackResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.ServiceRepository;
import com.example.demo.repository.StylistRepository;
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
    StylistRepository stylistRepository;

    @Autowired
    ServiceRepository serviceRepository;


    public Feedback createNewFeedback(FeedbackRequest feedbackRequest) {
        Stylist stylist = stylistRepository.findById(feedbackRequest.getStylistId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thợ!"));

        // Get the Account associated with this stylist
        Account stylistAccount = stylist.getAccount();



        ServiceEntity service = serviceRepository.findById(feedbackRequest.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy dịch vụ!"));

        Feedback feedback = new Feedback();
        feedback.setContent(feedbackRequest.getContent());
        feedback.setRating(feedbackRequest.getRating());
        feedback.setCustomer(authenticationService.getCurrentAccount());
        feedback.setService(service);
        feedback.setStylist(stylistAccount);
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
