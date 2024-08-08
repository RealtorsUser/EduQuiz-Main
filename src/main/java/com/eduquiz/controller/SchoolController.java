package com.eduquiz.controller;

import com.eduquiz.model.School;
import com.eduquiz.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    @GetMapping
    @ResponseBody
    public List<School> getAllSchools() {
        return schoolService.getAllSchools();
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> createSchool(@RequestBody School school) {
        schoolService.createSchool(school);
        return ResponseEntity.ok("School created successfully");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<School> getSchoolById(@PathVariable Long id) {
        School school = schoolService.getSchoolById(id);
        if (school != null) {
            return ResponseEntity.ok(school);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> updateSchool(@PathVariable Long id, @RequestBody School school) {
        String result = schoolService.updateSchool(id, school);
        if (result.equals("School updated successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteSchool(@PathVariable Long id) {
        String result = schoolService.deleteSchool(id);
        if (result.equals("School deleted successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // Add this method to handle the retrieval of the school view
    @GetMapping("/view")
    public String viewSchools(Model model) {
        List<School> schools = schoolService.getAllSchools();
        model.addAttribute("schools", schools);
        return "schools"; // Ensure school.html exists in src/main/resources/templates
    }
}
