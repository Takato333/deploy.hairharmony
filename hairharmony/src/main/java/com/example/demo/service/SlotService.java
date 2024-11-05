package com.example.demo.service;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.AppointmentDetail;
import com.example.demo.entity.Slot;
import com.example.demo.entity.Stylist;
import com.example.demo.entity.enums.StylistStatus;
import com.example.demo.exception.InvalidAppointmentTimeException;
import com.example.demo.repository.SlotRepository;
import com.example.demo.repository.StylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class SlotService {

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    StylistRepository stylistRepository;

    public void createSlotForAppointment(Long stylistId, LocalDateTime startTime, LocalDateTime endTime, AppointmentDetail appointmentDetail) {
        // Create a new slot for the stylist during the appointment time
        Slot slot = new Slot();
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setStylistStatus(StylistStatus.BOOKED); // Set the status to BOOKED when booked
        slot.setSlotStylist(stylistRepository.findStylistByIdAndIsDeletedFalse(stylistId)); // Link to the stylist
        slot.setAppointmentDetail(appointmentDetail); // Link to the appointment detail

        // Save the new slot
        slotRepository.save(slot);
    }

    // Check if the stylist is available during the requested slot
    private final Lock stylistLock = new ReentrantLock();

    public boolean checkSlotAvailability(Long stylistId, LocalDateTime startTime, LocalDateTime endTime) {
        stylistLock.lock(); // Lock trước khi kiểm tra
        try {
            // Kiểm tra trùng lịch như trước
            LocalDateTime earliestTime = startTime.withHour(8).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime latestTime = startTime.withHour(20).withMinute(0).withSecond(0).withNano(0);

            if (startTime.isBefore(earliestTime) || startTime.isAfter(latestTime)) {
                throw new InvalidAppointmentTimeException("Xin vui lòng chọn trong khung giờ làm việc: 8:00 ~ 20:00");
            }

            List<Slot> existingSlots = slotRepository.findBySlotStylistIdAndIsDeletedFalse(stylistId);

            for (Slot slot : existingSlots) {
                LocalDateTime existingStartTime = slot.getStartTime();
                LocalDateTime existingEndTime = slot.getEndTime();

                if ((startTime.isBefore(existingEndTime) && endTime.isAfter(existingStartTime)) ||
                        (endTime.isBefore(existingEndTime) && endTime.isAfter(existingStartTime)) ||
                        startTime.isEqual(existingStartTime) || startTime.isEqual(existingEndTime) ||
                        endTime.isEqual(existingStartTime) || endTime.isEqual(existingEndTime)) {
                    return false; // Stylist không có sẵn
                }
            }
            return true; // Stylist có sẵn
        } finally {
            stylistLock.unlock(); // Unlock sau khi kiểm tra xong
        }
    }

    public synchronized void updateSlotStatus(Appointment appointment) {
        List<AppointmentDetail> details = appointment.getAppointmentDetails();
        for (AppointmentDetail detail : details) {
            // Get the stylist from the appointment detail
            Stylist stylist = detail.getStylist();
            if (stylist == null) {
                continue;
            }
            // Assuming you have a method to find the slot by stylist and appointment times
            List<Slot> slots = slotRepository.findBySlotStylistIdAndIsDeletedFalse(stylist.getId());
            for (Slot slot : slots) {
                // Nếu Slot hiện tại đã có trạng thái BOOKED, không thay đổi trạng thái
                if (slot.getStylistStatus() == StylistStatus.BOOKED) {
                    continue; // Skip this slot if it's already booked
                }
                // Check if the slot overlaps with the appointment's time
                if (detail.getStartTime().isBefore(slot.getEndTime()) && detail.getEndTime().isAfter(slot.getStartTime())) {
                    // If there is an overlap, set the stylist status to UNAVAILABLE (or your equivalent)
                    slot.setStylistStatus(StylistStatus.BOOKED);
                    slotRepository.save(slot); // Save the updated slot
                } else {
                    // If there is no overlap, set the stylist status to AVAILABLE
                    slot.setStylistStatus(StylistStatus.AVAILABLE);
                }
                slotRepository.save(slot); // Save the updated slot
            }
        }
    }

    public List<LocalDateTime> findAvailableSlots(Long stylistId, LocalDate date) {
        // Giới hạn thời gian làm việc (ví dụ: từ 8 giờ sáng đến 8 giờ tối)
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(20, 0);

        // Tạo danh sách các thời gian 30 phút/lần từ giờ mở cửa đến giờ đóng cửa
        List<LocalDateTime> potentialTimes = new ArrayList<>();
        LocalDateTime current = date.atTime(openingTime);
        while (current.isBefore(date.atTime(closingTime))) {
            potentialTimes.add(current);
            current = current.plusMinutes(30); // Khoảng cách 30 phút
        }

        // Lấy tất cả các slot của stylist trong ngày đã chọn
        List<Slot> bookedSlots = slotRepository.findBySlotStylistIdAndIsDeletedFalse(stylistId).stream()
                .filter(slot -> slot.getStartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());

        // Lọc ra các giờ đã đặt trước
        List<LocalDateTime> availableTimes = potentialTimes.stream()
                .filter(time -> bookedSlots.stream().noneMatch(
                        slot -> time.isBefore(slot.getEndTime()) && time.plusMinutes(30).isAfter(slot.getStartTime())
                ))
                .collect(Collectors.toList());

        return availableTimes;
    }
}
