package yongs.temp.controller;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yongs.temp.dao.StatusRepository;
import yongs.temp.model.Status;

@RestController
@RequestMapping("/status")
public class StatusController {
	private Logger logger = LoggerFactory.getLogger(StatusController.class);
	@Autowired
    private StatusRepository repo;
	@Autowired
	private MongoOperations mongoOperation;
	
    @GetMapping("/all")
    public Flux<Status> findAll() {
    	logger.debug("flex-status|StatusController|findAll()");
    	// id 순으로 sort
    	return repo.findAll().sort( (a, b) -> new Integer(a.getId()) - new Integer(b.getId()) );
    }
 
    /* Status 데이터 생성을 위한 임시 method */
    @PostMapping("/init")
    public Mono<Long> init(@RequestBody Map<String, Long> numOfEmp) {
    	logger.debug("flex-status|StatusController|init({})", numOfEmp.get("num")); 	
 	
    	String[] project = {"RED", "BLUE", "GREEN", "YELLOW", "INDIGO", "WHITE", "BLACK"};
    	String[] skill = {"PM", "BA", "BD", "DA", "TA", "SWA", "PMO", "DEVELOPER"};
    	String[] level = {"junior", "senior", "intern"};
    	String[] grade = {"A", "B", "C"};
    	
    	Random r = new Random();
    	int R_project;
    	int R_skill;
    	int R_level;
    	int R_grade;
    	
    	for(int idx=0; idx <numOfEmp.get("num"); idx++) {   	
    		R_project = r.nextInt(project.length);
    		R_skill = r.nextInt(skill.length);
    		R_level = r.nextInt(level.length);
    		R_grade = r.nextInt(grade.length);
       	    		
    		Status e = new Status();
    		e.setId(new Integer(idx).toString());
    		e.setName("User-"+idx);
    		e.setProject("Project-" + project[R_project]);
    		e.setSkill(skill[R_skill]);
    		e.setLevel(level[R_level]); 
    		e.setGrade(grade[R_grade]);
    		
    		repo.save(e).subscribe();
    		
    		logger.debug(e.getId());
    	}
    	return Mono.just(numOfEmp.get("num"));
    }
    
    @DeleteMapping("/clean")
    public Mono<Void> clean() {
    	logger.debug("flex-status|StatusController|clean()");
    	mongoOperation.dropCollection("status");
    	return Mono.empty();
    }
}
