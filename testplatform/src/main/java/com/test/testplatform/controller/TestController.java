package com.test.testplatform.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.test.testplatform.model.Question;
import com.test.testplatform.model.QuestionAnswer;
import com.test.testplatform.model.Result;
import com.test.testplatform.model.TestConfig;
import com.test.testplatform.model.User;
import com.test.testplatform.repository.QuestionRepository;
import com.test.testplatform.repository.ResultRepository;
import com.test.testplatform.repository.TestConfigRepository;
import com.test.testplatform.repository.UserRepository;



@Controller
@SessionAttributes("user")
public class TestController {

    @Autowired
    private QuestionRepository questionRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ResultRepository resultRepo;
    @Autowired
    private TestConfigRepository configRepo;
    
    @GetMapping("/")
    public String landingPage() {
        return "landing";
    }
    
    @GetMapping("/start-test")
    public String home(@RequestParam String slug,Model model) {
    	TestConfig config = configRepo.findBySlug(slug)
	            .orElseThrow(() -> new RuntimeException("Test config not found"));
    	model.addAttribute("user", new User());
        model.addAttribute("slug", slug);
        return "home";
    }

    @PostMapping("/start-test")
    public String startTest(@ModelAttribute User user,@RequestParam String slug, Model model) {
    	 User savedUser = userRepo.save(user);
    	    model.addAttribute("user", savedUser);

    	    // ✅ Get test settings from admin
//    	    TestConfig config = configRepo.findAll().stream()
//    	        .findFirst()
//    	        .orElse(new TestConfig(5, 2, "")); // default 5 Qs, 2 min, no password
    	    
    	    TestConfig config = configRepo.findBySlug(slug)
    	            .filter(TestConfig::isPublished)
    	            .orElseThrow(() -> new RuntimeException("Test not found or not published"));

    	    List<Question> allQuestions = questionRepo.findAll();
    	    Collections.shuffle(allQuestions);

    	    // ✅ Pick only the configured number of questions
    	    List<Question> selectedQuestions = allQuestions.stream()
    	        .limit(config.getNumberOfQuestions())
    	        .toList();

    	    // ✅ Pass to view
    	    model.addAttribute("questions", selectedQuestions);
    	    model.addAttribute("durationInMinutes", config.getDurationInMinutes());

    	    return "test";
    }

    @PostMapping("/submit")
    public String submit(@RequestParam Map<String, String> params,
                         @ModelAttribute("user") User user,
                         Model model) {
    	List<Question> answeredQuestions = new ArrayList<>();
    	List<String> userAnswers = new ArrayList<>();
    	int score = 0;

    	for (Map.Entry<String, String> entry : params.entrySet()) {
    	    if (entry.getKey().startsWith("q")) {
    	        Long qid = Long.parseLong(entry.getKey().substring(1));
    	        String userAnswer = entry.getValue();

    	        Question q = questionRepo.findById(qid).get();
    	        answeredQuestions.add(q);
    	        userAnswers.add(userAnswer);

    	        if (userAnswer.equalsIgnoreCase(q.getCorrectAnswer())) {
    	            score++;
    	        }
    	    }
    	}
    	List<QuestionAnswer> questionAnswerList = new ArrayList<>();
    	for (int i = 0; i < answeredQuestions.size(); i++) {
    	    questionAnswerList.add(new QuestionAnswer(answeredQuestions.get(i),userAnswers.get(i)));
    	}
    	

    	Result result = new Result();
    	result.setUser(user);
    	result.setScore(score);
    	result.setDateTime(LocalDateTime.now());
    	result.setQuestions(answeredQuestions);
    	result.setUserAnswers(userAnswers);

    	resultRepo.save(result); // only saves to DB what’s required

    	model.addAttribute("score", score);
    	model.addAttribute("total", answeredQuestions.size());
    	model.addAttribute("result", result);
    	model.addAttribute("questions", questionAnswerList);
    	return "result";

    	
    }

    @GetMapping("/history")
    public String history(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("results", resultRepo.findByUser(user));
        return "history";
    }
}

