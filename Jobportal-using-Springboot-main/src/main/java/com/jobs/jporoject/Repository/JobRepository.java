package com.jobs.jporoject.Repository;

import com.jobs.jporoject.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobRepository extends MongoRepository<Job, String> {


}
