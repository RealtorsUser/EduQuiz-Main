package com.eduquiz.service;

import com.eduquiz.model.School;
import com.eduquiz.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    public void createSchool(School school) {
        schoolRepository.save(school);
    }

    public School getSchoolById(Long id) {
        return schoolRepository.findById(id).orElse(null);
    }

    public String updateSchool(Long id, School school) {
        if (schoolRepository.existsById(id)) {
            school.setId(id);
            schoolRepository.save(school);
            return "School updated successfully";
        } else {
            return "School not found";
        }
    }

    public String deleteSchool(Long id) {
        if (schoolRepository.existsById(id)) {
            schoolRepository.deleteById(id);
            return "School deleted successfully";
        } else {
            return "School not found";
        }
    }
}
