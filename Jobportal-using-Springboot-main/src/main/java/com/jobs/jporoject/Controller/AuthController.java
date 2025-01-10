package com.jobs.jporoject.Controller;

import com.jobs.jporoject.Repository.JobRepository;
import com.jobs.jporoject.Repository.QuestionRepository;
import com.jobs.jporoject.Repository.UserRepository;
import com.jobs.jporoject.model.Job;
import com.jobs.jporoject.model.Question;
import com.jobs.jporoject.model.User;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/")
    public String showLoginForm(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("user", new User());
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, @RequestParam String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "redirect:/home";
        } else {
            return "redirect:/?error=true";
        }
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(User user) {
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/add-jobs")
    public String showAddJobsForm(Model model) {
        model.addAttribute("job", new Job());
        return "add-jobs";
    }

    @PostMapping("/save-job")
    public String saveJob(@ModelAttribute Job job, RedirectAttributes redirectAttributes) {
        jobRepository.save(job);
        redirectAttributes.addFlashAttribute("success", "Job added successfully");
        return "redirect:/add-jobs";
    }
    @GetMapping("/find-jobs")
    public String findJobs(Model model) {
        List<Job> jobs = jobRepository.findAll();
        model.addAttribute("jobs", jobs);
        return "find-jobs";
    }
    @PostMapping("/apply-job")
    public String applyJob(@RequestParam("jobId") String jobId, RedirectAttributes redirectAttributes) {


        redirectAttributes.addFlashAttribute("successMessage", "Job applied successfully");
        return "redirect:/find-jobs";
    }


    @GetMapping("/admin-login")
    public String showAdminLoginForm() {
        return "admin-login";
    }

    @PostMapping("/admin-login")
    public String processAdminLogin(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        if (username.equals("admin") && password.equals("admin@123")) {
            return "redirect:/admin-home";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/admin-login";
        }
    }


    @GetMapping("/admin-home")
    public String adminHome(Model model) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("users", userList);
        return "admin-home";
    }
    @GetMapping("/manage-job")
    public String manageJobs(Model model) {
        List<Job> jobs = jobRepository.findAll();
        model.addAttribute("jobs", jobs);
        return "manage-job";
    }

    @GetMapping("/delete-job/{id}")
    public String deleteJob(@PathVariable String id) {
        jobRepository.deleteById(id);
        return "redirect:/manage-job";
    }

    @GetMapping("/update-job/{id}")
    public String updateJob(@PathVariable String id, Model model) {

        Optional<Job> optionalJob = jobRepository.findById(id);


        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();


            model.addAttribute("job", job);

            return "update-job";
        } else {

            return "/";
        }
    }
    @PostMapping("/save-updated-job")
    public String saveUpdatedJob(@ModelAttribute Job updatedJob, RedirectAttributes redirectAttributes) {

        if (updatedJob.getId() == null || updatedJob.getId().isEmpty()) {

            redirectAttributes.addFlashAttribute("errorMessage", "Job ID is required for updating a job");
            return "redirect:/manage-job";
        }


        Job existingJob = jobRepository.findById(updatedJob.getId()).orElse(null);
        if (existingJob == null) {

            redirectAttributes.addFlashAttribute("errorMessage", "Job not found for ID: " + updatedJob.getId());
            return "redirect:/manage-job";
        }


        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setSkills(updatedJob.getSkills());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setSalary(updatedJob.getSalary());
        existingJob.setType(updatedJob.getType());


        jobRepository.save(existingJob);


        redirectAttributes.addFlashAttribute("successMessage", "Job updated successfully");
        return "redirect:/manage-job";
    }

    @GetMapping("/add-question")
    public String showAddQuestionForm(Model model) {
        model.addAttribute("question", new Question());
        return "add-question";
    }
    @PostMapping("/save-question")
    public String saveQuestion(@ModelAttribute Question question, RedirectAttributes redirectAttributes) {
        // Save the question to the database
        questionRepository.save(question);


        redirectAttributes.addFlashAttribute("successMessage", "Question added successfully");
        return "redirect:/add-question";
    }
    @GetMapping("/interview-preparation")
    public String showQuizQuestions(Model model) {
        List<Question> questions = questionRepository.findAll();
        model.addAttribute("questions", questions);
        return "interview-preparation";
    }


    @PostMapping("/check-answer")
    public String checkAnswer(@RequestParam("questionId") String questionId,
                              @RequestParam("selectedOption") String selectedOption,
                              Model model) {
        Question question = questionRepository.findById(questionId).orElse(null);

        if (question != null) {
            String correctOption = question.getCorrectOption();
            String feedback;
            System.out.print(correctOption);
            System.out.print(selectedOption);
            if (selectedOption.equals(correctOption)) {
                feedback = "Correct!";
            } else {
                feedback = "Wrong! The correct option is: " + correctOption;
            }
            model.addAttribute("question", question);
            model.addAttribute("selectedOption", selectedOption);
            model.addAttribute("feedback", feedback);
        } else {
            model.addAttribute("feedback", "Question not found!");
        }
        return "check-answer";
    }




}
