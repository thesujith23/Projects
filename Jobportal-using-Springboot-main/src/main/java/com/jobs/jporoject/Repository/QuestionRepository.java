package com.jobs.jporoject.Repository;

import com.jobs.jporoject.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, String> {

}

