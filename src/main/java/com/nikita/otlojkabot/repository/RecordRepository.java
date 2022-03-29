package com.nikita.otlojkabot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import  com.nikita.otlojkabot.domain.Record;

import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query("select r from Record r where r.createDateTime=(select min (r1.createDateTime) from  Record r1 where r1.postDateTime = null)")
    Optional<Record> getFirstRecordInQueue();

    @Query("select r from Record r where r.postDateTime=(select max (r1.postDateTime)from Record r1)")
    Optional<Record> getLastPostedRecord();

    @Query("select count(*) from Record r where r.postDateTime=null")
    long getNumberOfScheduledPosts();

    @Transactional
    @Modifying
    @Query("delete from Record r where r.postDateTime= null")
    void clear();
}
