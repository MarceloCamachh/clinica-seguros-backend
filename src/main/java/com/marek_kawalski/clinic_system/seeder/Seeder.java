package com.marek_kawalski.clinic_system.seeder;

import com.marek_kawalski.clinic_system.examination.Examination;
import com.marek_kawalski.clinic_system.examination.ExaminationRepository;
import com.marek_kawalski.clinic_system.examination.ExaminationStatus;
import com.marek_kawalski.clinic_system.user.Address;
import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.UserRepository;
import com.marek_kawalski.clinic_system.user.UserRole;
import com.marek_kawalski.clinic_system.user.doctor.DoctorDetails;
import com.marek_kawalski.clinic_system.user.doctor.schedule.DailySchedule;
import com.marek_kawalski.clinic_system.user.doctor.schedule.Schedule;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class Seeder {
    private final UserRepository userRepository;
    private final ExaminationRepository examinationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${seeder.generic-password:Password1234!}")
    private String genericPassword;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    private static final Map<String, List<String>> SPECIALIZATION_EXAMINATIONS = new HashMap<>();
    private static final Map<String, List<String>> EXAMINATION_SYMPTOMS = new HashMap<>();
    private static final List<Double> EXAMINATION_PRICES = Arrays.asList(
            50.0, 75.0, 100.0, 150.0, 200.0, 250.0, 300.0, 350.0, 400.0, 450.0, 500.0
    );

    static {
        SPECIALIZATION_EXAMINATIONS.put("Cardiology", Arrays.asList(
                "Electrocardiogram (ECG)", "Echocardiogram", "Stress Test", "Holter Monitoring"
        ));
        SPECIALIZATION_EXAMINATIONS.put("Dermatology", Arrays.asList(
                "Skin Biopsy", "Patch Testing", "Dermatoscopy", "Cryotherapy"
        ));
        SPECIALIZATION_EXAMINATIONS.put("Neurology", Arrays.asList(
                "Electroencephalogram (EEG)", "MRI of the Brain", "Nerve Conduction Study", "Lumbar Puncture"
        ));
        SPECIALIZATION_EXAMINATIONS.put("Orthopedics", Arrays.asList(
                "X-ray", "MRI of Joints", "Bone Density Scan", "Arthroscopy"
        ));
        SPECIALIZATION_EXAMINATIONS.put("Gastroenterology", Arrays.asList(
                "Endoscopy", "Colonoscopy", "Liver Function Test", "Abdominal Ultrasound"
        ));

        EXAMINATION_SYMPTOMS.put("Electrocardiogram (ECG)", Arrays.asList(
                "dolor en el pecho", "dificultad para respirar", "palpitaciones", "mareos"
        ));
        EXAMINATION_SYMPTOMS.put("Echocardiogram", Arrays.asList(
                "soplo cardíaco", "dolor en el pecho", "dificultad para respirar", "hinchazón en las piernas"
        ));
        EXAMINATION_SYMPTOMS.put("Stress Test", Arrays.asList(
                "dolor en el pecho durante el ejercicio", "dificultad para respirar con actividad", "latido cardíaco irregular"
        ));
        EXAMINATION_SYMPTOMS.put("Holter Monitoring", Arrays.asList(
                "palpitaciones", "desmayos", "mareos", "fatiga inexplicable"
        ));
        EXAMINATION_SYMPTOMS.put("Skin Biopsy", Arrays.asList(
                "lunar sospechoso", "lesión en la piel", "sarpullido", "crecimiento inexplicable en la piel"
        ));
        EXAMINATION_SYMPTOMS.put("Patch Testing", Arrays.asList(
                "erupción cutánea", "picazón", "dermatitis por contacto", "eccema"
        ));
        EXAMINATION_SYMPTOMS.put("Dermatoscopy", Arrays.asList(
                "lunar cambiante", "decoloración de la piel", "crecimiento irregular en la piel"
        ));
        EXAMINATION_SYMPTOMS.put("Cryotherapy", Arrays.asList(
                "verrugas", "acrocordones", "queratosis actínica", "queratosis seborreica"
        ));
    }

    @EventListener
    @Transactional
    public void seedTables(ContextRefreshedEvent ignoredEvent) {
        seedUsers();
        seedExaminations();
    }

    private void seedUsers() {
        if (userRepository.count() > 0) return;

        // Create hardcoded users
        createHardcodedUser(UserRole.ROLE_ADMIN, "Admin", "Admin", "admin@admin.com");
        createHardcodedUser(UserRole.ROLE_DOCTOR, "Doctor", "Doctor", "doctor@doctor.com");
        createHardcodedUser(UserRole.ROLE_REGISTRAR, "Registrar", "Registrar", "registrar@registrar.com");
        createHardcodedUser(UserRole.ROLE_PATIENT, "Patient", "Patient", "patient@patient.com");

        // Seed additional users
        seedAdditionalUsers(UserRole.ROLE_DOCTOR, Constants.NUMBER_OF_DOCTOR_USERS - 1, this::createDoctor);
        seedAdditionalUsers(UserRole.ROLE_ADMIN, Constants.NUMBER_OF_ADMIN_USERS - 1, this::createAdmin);
        seedAdditionalUsers(UserRole.ROLE_PATIENT, Constants.NUMBER_OF_PATIENT_USERS - 1, this::createPatient);
        seedAdditionalUsers(UserRole.ROLE_REGISTRAR, Constants.NUMBER_OF_REGISTRAR_USERS - 1, this::createRegistrar);
    }

    private void createHardcodedUser(UserRole role, String name, String surname, String email) {
        User user;
        if (role == UserRole.ROLE_DOCTOR) {
            user = createDoctor(name, surname, email);
        } else {
            user = createUserWithRole(role, name, surname, email);
        }
        userRepository.save(user);
    }

    private void seedAdditionalUsers(UserRole role, int count, UserCreator creator) {
        IntStream.range(0, count).forEach(i -> userRepository.save(creator.create(null, null, null)));
    }

    private User.UserBuilder seedCommonUserData() {
        String name = faker.name().firstName();
        String surname = faker.name().lastName();
        return User.builder()
                .name(name)
                .surname(surname)
                .email(name.toLowerCase(Locale.ROOT) + "." + surname.toLowerCase(Locale.ROOT) + "@example.com")
                .password(bCryptPasswordEncoder.encode(genericPassword))
                .isEnabled(true)
                .pesel(faker.numerify("###########"))
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .address(createAddress());
    }

    private Address createAddress() {
        return Address.builder()
                .apartmentNumber(String.valueOf(faker.random().nextInt(1, 100)))
                .city(faker.address().city())
                .houseNumber(String.valueOf(faker.random().nextInt(1, 100)))
                .postalCode(faker.address().zipCode())
                .street(faker.address().streetName())
                .country(faker.address().country())
                .build();
    }

    private User createDoctor(String name, String surname, String email) {
        User.UserBuilder userBuilder = seedCommonUserData();
        if (name != null) {
            userBuilder.name(name);
        }
        if (surname != null) {
            userBuilder.surname(surname);
        }
        if (email != null) {
            userBuilder.email(email);
        }
        return userBuilder
                .userRole(UserRole.ROLE_DOCTOR)
                .doctorDetails(getDoctorDetails())
                .build();
    }

    private User createAdmin(String name, String surname, String email) {
        return createUserWithRole(UserRole.ROLE_ADMIN, name, surname, email);
    }

    private User createPatient(String name, String surname, String email) {
        return createUserWithRole(UserRole.ROLE_PATIENT, name, surname, email);
    }

    private User createRegistrar(String name, String surname, String email) {
        return createUserWithRole(UserRole.ROLE_REGISTRAR, name, surname, email);
    }

    private User createUserWithRole(UserRole role, String name, String surname, String email) {
        User.UserBuilder userBuilder = seedCommonUserData();
        if (name != null) {
            userBuilder.name(name);
        }
        if (surname != null) {
            userBuilder.surname(surname);
        }
        if (email != null) {
            userBuilder.email(email);
        }
        return userBuilder
                .userRole(role)
                .build();
    }

    private DoctorDetails getDoctorDetails() {
        String specialization = faker.options().option(SPECIALIZATION_EXAMINATIONS.keySet().toArray(new String[0]));
        return DoctorDetails.builder()
                .specialization(specialization)
                .education(faker.educator().university())
                .description(faker.lorem().paragraph())
                .schedule(Schedule.builder()
                        .dailySchedules(initializeDailySchedules())
                        .build())
                .build();
    }

    private Map<DayOfWeek, DailySchedule> initializeDailySchedules() {
        return Arrays.stream(DayOfWeek.values())
                .collect(Collectors.toMap(
                        day -> day,
                        day -> DailySchedule.builder()
                                .startTime(LocalTime.of(faker.random().nextInt(8, 12), 0))
                                .endTime(LocalTime.of(faker.random().nextInt(12, 18), 0))
                                .build(),
                        (v1, v2) -> v1,
                        () -> new EnumMap<>(DayOfWeek.class)
                ));
    }

    private void seedExaminations() {
        if (examinationRepository.count() > 0) return;

        List<Examination> examinations = new ArrayList<>();
        SPECIALIZATION_EXAMINATIONS.forEach((specialization, examinationNames) -> {
            examinationNames.forEach(name -> {
                Examination examination = createExamination(name, specialization);
                examinations.add(examination);
            });
        });
        examinationRepository.saveAll(examinations);

        assignExaminationsToDoctors(examinations);
    }

    private Examination createExamination(String name, String specialization) {
        return Examination.builder()
                .name(name)
                .description(generateExaminationDescription(name, specialization))
                .price(generateExaminationPrice())
                .duration(List.of(15, 30, 45, 60).get(random.nextInt(4)))
                .status(ExaminationStatus.AVAILABLE)
                .doctors(new ArrayList<>())
                .build();
    }


    private double generateExaminationPrice() {
        return EXAMINATION_PRICES.get(random.nextInt(EXAMINATION_PRICES.size()));
    }

    private String generateExaminationDescription(String name, String specialization) {
        List<String> relevantSymptoms = EXAMINATION_SYMPTOMS.getOrDefault(name,
                Collections.singletonList(faker.medical().symptoms()));
        String symptoms = faker.options().option(relevantSymptoms.toArray(new String[0]));
        return String.format("Un examen de %s en el campo de %s. Se utiliza para diagnosticar o monitorear pacientes con %s.",
                name, specialization, symptoms);
    }

    private void assignExaminationsToDoctors(List<Examination> examinations) {
        userRepository.findAllByUserRole(UserRole.ROLE_DOCTOR).forEach(doctor -> {
            String specialization = doctor.getDoctorDetails().getSpecialization();
            List<Examination> doctorExaminations = examinations.stream()
                    .filter(e -> e.getDescription().contains(specialization))
                    .toList();
            doctor.getDoctorDetails().setExaminations(new ArrayList<>(doctorExaminations));
            userRepository.save(doctor);

            doctorExaminations.forEach(examination -> examination.getDoctors().add(doctor));
            examinationRepository.saveAll(doctorExaminations);
        });
    }


    @FunctionalInterface
    private interface UserCreator {
        User create(String name, String surname, String email);
    }
}