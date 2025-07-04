package com.test.testplatform.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.testplatform.dto.ParsedQuestion;
import com.test.testplatform.model.Admin;
import com.test.testplatform.model.Question;
import com.test.testplatform.model.TestConfig;
import com.test.testplatform.repository.AdminRepository;
import com.test.testplatform.repository.QuestionRepository;
import com.test.testplatform.repository.ResultRepository;
import com.test.testplatform.repository.TestConfigRepository;
import com.test.testplatform.service.GeminiService;




@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TestConfigRepository configRepo;

    @Autowired
    private QuestionRepository questionRepo;
    
    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ResultRepository resultRepo;
    
    @Autowired
    private GeminiService geminiService;

    // Show test config page
    @GetMapping
    public String showConfigForm(Model model) {
        TestConfig config = configRepo.findAll().stream().findFirst().orElse(new TestConfig());
        model.addAttribute("config", config);
        return "admin";
    }

    // Save test config
    @PostMapping
    public String saveConfig(@ModelAttribute TestConfig config,RedirectAttributes redirectAttributes) {
    	configRepo.deleteAll(); // optional: if you want one config only

        String baseSlug;
        if (config.getTestName() != null && !config.getTestName().isBlank()) {
            baseSlug = config.getTestName().toLowerCase()
                .replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
        } else {
            baseSlug = "test";
        }

        String slug = baseSlug + "-" + UUID.randomUUID().toString().substring(0, 6);
        config.setSlug(slug);
        config.setPublished(false); // ✅ Not published yet

        configRepo.save(config);

        redirectAttributes.addFlashAttribute("success", "Test saved. Now publish it when ready.");
        return "redirect:/admin";
    }

    // Show question list
    @GetMapping("/questions")
    public String listQuestions(Model model) {
        model.addAttribute("questions", questionRepo.findAll());
        return "questions";
    }

    // Show add form
    @GetMapping("/questions/add")
    public String addQuestionForm(Model model) {
        model.addAttribute("question", new Question());
        return "question-form";
    }

    // Handle add/edit
    @PostMapping("/questions/save")
    public String saveQuestion(@ModelAttribute Question question,@RequestParam(defaultValue="finish") String redirect) {
        questionRepo.save(question);
        if("add".equals(redirect)) {
        	return "redirect:/admin/questions/add";
        }
        return "redirect:/admin/questions";
    }

    // Edit
    @GetMapping("/questions/edit/{id}")
    public String editQuestion(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionRepo.findById(id).orElseThrow());
        return "question-form";
    }

    // Delete
    @GetMapping("/questions/delete/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        questionRepo.deleteById(id);
        return "redirect:/admin/questions";
    }
    
    @GetMapping("/publish")
    public String showPublishPage(Model model) {
        TestConfig config = configRepo.findAll().stream().findFirst().orElse(new TestConfig());// or fetch by user/session
        model.addAttribute("config", config);
        return "publish"; // Renders publish.html
    }

    @GetMapping("/publish/{id}")
    public String publishTest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        TestConfig config = configRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid test ID"));

        config.setPublished(true);
        configRepo.save(config);

        String link = "/start-test?slug=" + config.getSlug();
        redirectAttributes.addFlashAttribute("success", "Test published successfully!");
        redirectAttributes.addFlashAttribute("shareLink", link);

        return "redirect:/admin/publish";
    }

    @GetMapping("/register")
    public String showAdminRegistrationForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin-register";
    }

    @PostMapping("/register")
    public String handleAdminRegistration(@RequestParam String username, @RequestParam String password) {
        // check if admin already exists
        if (adminRepo.findByUsername(username).isPresent()) {
            return "redirect:/admin/register?error=exists";
        }

        // hash password before saving
        String hashedPassword = passwordEncoder.encode(password);
        Admin admin = new Admin(username, hashedPassword);
        adminRepo.save(admin);

        return "redirect:/login";
    }
    
    @GetMapping("/results")
    public String viewAllResults(Model model) {
        model.addAttribute("results", resultRepo.findAll());
        return "admin-results"; // this will be your Thymeleaf template
    }
   
    @GetMapping("/results/{id}")
    public String viewResultDetails(@PathVariable Long id, Model model) {
        model.addAttribute("result", resultRepo.findById(id));
        return "admin/result-details";
    }
    
    
    @GetMapping("/generate-questions")
    public String showForm() {
        return "generate-questions";
    }
    
    @PostMapping("/generate-questions")
    public String processForm(@RequestParam("inputText") String inputText, Model model) {
        List<ParsedQuestion> parsedQuestions = geminiService.parseQuestions(inputText);

        // Convert to DB-ready Question entities
        List<Question> questions = parsedQuestions.stream().map(pq -> {
            Question q = new Question();
            q.setText(pq.getQuestion());

            List<String> opts = pq.getOptions();
            if (opts != null && opts.size() == 4) {
                q.setOptionA(opts.get(0));
                q.setOptionB(opts.get(1));
                q.setOptionC(opts.get(2));
                q.setOptionD(opts.get(3));
            }

            q.setCorrectAnswer(pq.getAnswer());
            return q;
        }).toList();

        model.addAttribute("questions", questions);
        return "generate-questions";
    }
    

}

