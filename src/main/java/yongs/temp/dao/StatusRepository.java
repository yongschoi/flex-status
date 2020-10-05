package yongs.temp.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import yongs.temp.model.Status;

public interface StatusRepository extends ReactiveMongoRepository<Status, String> {	
}