package com.nikita.otlojkabot.service;

import com.nikita.otlojkabot.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.nikita.otlojkabot.domain.Record;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    private final TelegramBotHandler botHandler;

    @Value("${schedule.postingInterval}")
    private long postingInterval;

    @Scheduled(fixedDelayString = "60000")
    private void run() {
        Optional<Record> recordToPost = recordRepository.getFirstRecordInQueue();
        if (recordToPost.isPresent()) {
            Optional<Record> lastPostedRecordOptional = recordRepository.getLastPostedRecord();
            if (lastPostedRecordOptional.isPresent()) {
                Record lastPostedRecord = lastPostedRecordOptional.get();
                Duration duration = Duration.between(lastPostedRecord.getPostDateTime(), LocalDateTime.now());
                if (duration.toMinutes() >= postingInterval) {
                    Record record = recordToPost.get();
                    doPost(record);
                }
            } else {
                Record record = recordToPost.get();
                doPost(record);
            }
        }
    }


    private void doPost(Record record) {
        switch (record.getDataType()) {
            case "PHOTO": {
                botHandler.sendPhoto(record);
                break;
            }
            case "VIDEO": {
                botHandler.sendVideo(record);
                break;
            }
            case "TEXT": {
                botHandler.sendMessage(record);
                break;
            }
            case "ANIMATION": {
                botHandler.sendAnimation(record);
                break;
            }
            case "DOCUMENT": {
                botHandler.sendDocument(record);
                break;
            }
        }
    }
}




