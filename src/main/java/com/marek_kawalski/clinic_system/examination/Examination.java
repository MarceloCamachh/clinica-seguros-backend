package com.marek_kawalski.clinic_system.examination;

import com.marek_kawalski.clinic_system.user.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Document(collection = "examinations")
public class Examination {
    @Id
    private String id;

    private String name;

    private String description;

    private double price;

    private int duration;

    private ExaminationStatus status;

    @DocumentReference(lazy = true)
    private List<User> doctors = new ArrayList<>();

}
