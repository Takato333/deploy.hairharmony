package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Feedback;
import com.example.demo.model.FeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Feedback findFeedbackById(Long feedbackId);

    @Query("SELECT new com.example.demo.model.FeedbackResponse(f.id, f.content, f.rating, a.email, s.name) " +
            "FROM Feedback f" +
            " join Account a  on f.stylist.id = a.id" +
            " join ServiceEntity s on f.service.id = s.id" +
            " where f.stylist.id =:stylistID ")
    List<FeedbackResponse> findFeedbackByStylistId(@Param("stylistID") Long id);


    List<Feedback> findByStylistAndRatingGreaterThanEqualAndIsDeletedFalse(Account stylist, int rating);

}
