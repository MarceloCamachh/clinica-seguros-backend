package com.marek_kawalski.clinic_system.examination;

import com.marek_kawalski.clinic_system.examination.dto.CreateUpdateExaminationDTO;
import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.UserRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {
    private final ExaminationRepository examinationRepository;
    private final UserRepository userRepository;

    @Override
    public Page<Examination> getPagedExaminations(final ExaminationRequestParams examinationRequestParams) {
        return examinationRepository.getPagedExaminations(examinationRequestParams);
    }

    @Override
    public Optional<Examination> createUpdateExamination(final String id, final CreateUpdateExaminationDTO examinationDTO) {
        Examination examination;

        if (id != null) {
            examination = examinationRepository.findById(id).orElse(null);
            if (examination == null) {
                return Optional.empty();
            }
        } else {
            examination = new Examination();
        }

        updateExaminationDetails(examination, examinationDTO);

        if (id == null) {
            examination = examinationRepository.save(examination);
        }


        // Update doctors with the new examination
        updateDoctorsExaminations(examination, examinationDTO);

        return Optional.of(examination);
    }

    @Override
    public List<Examination> getDoctorsExaminations(final ObjectId doctorId) {
        return examinationRepository.findAllByDoctorsId(doctorId);
    }

    private void updateExaminationDetails(Examination examination, CreateUpdateExaminationDTO examinationDTO) {
        examination.setDescription(examinationDTO.description());
        examination.setDuration(examinationDTO.duration());
        examination.setName(examinationDTO.name());
        examination.setPrice(examinationDTO.price());
        examination.setStatus(examinationDTO.status());
    }

    private void updateDoctorsExaminations(Examination examination, CreateUpdateExaminationDTO examinationDTO) {
        List<String> doctorIds = examinationDTO.doctorIds();
        List<User> allDoctors = userRepository.findAll();

        // Remove the examination from doctors that no longer have it
        for (User doctor : allDoctors) {
            if (doctor.getDoctorDetails() == null) continue; // Skip users that are not doctors (e.g. patients
            if (doctor.getDoctorDetails().getExaminations().stream().map(Examination::getId).toList().contains(examination.getId()) && !doctorIds.contains(doctor.getId())) {
                doctor.getDoctorDetails().getExaminations().removeIf(e -> e.getId().equals(examination.getId()));
                userRepository.save(doctor); // Save the doctor to persist the changes
            }
        }

        List<User> tempDoctors = new ArrayList<>();

        // Add the examination to new doctors
        for (String doctorId : doctorIds) {
            User doctor = userRepository.findById(doctorId).orElse(null);
            if (doctor != null && !doctor.getDoctorDetails().getExaminations().stream().map(Examination::getId).toList().contains(examination.getId())) {
                doctor.getDoctorDetails().getExaminations().add(examination);
                userRepository.save(doctor); // Save the doctor to persist the changes
                tempDoctors.add(doctor);
            }
        }

        examination.setDoctors(tempDoctors);
        examinationRepository.save(examination);

    }

}
