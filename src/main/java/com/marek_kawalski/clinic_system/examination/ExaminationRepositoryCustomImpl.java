package com.marek_kawalski.clinic_system.examination;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ExaminationRepositoryCustomImpl implements ExaminationRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Examination> getPagedExaminations(final ExaminationRequestParams examinationRequestParams) {
        final int pageNumber = examinationRequestParams.pageNum();
        final int pageSize = examinationRequestParams.pageSize();
        final String search = examinationRequestParams.search();

        final Pageable pageable = PageRequest.of(pageNumber, pageSize);

        final Query query = new Query();

        if (examinationRequestParams.doctorIds() != null && !examinationRequestParams.doctorIds().isEmpty()) {
            final List<ObjectId> doctorIds = examinationRequestParams.doctorIds();
            query.addCriteria(Criteria.where("doctors").in(doctorIds));
        }

        if (search != null && !search.isEmpty()) {
            Criteria nameCriteria = Criteria.where("name").regex(search, "i");
            Criteria descriptionCriteria = Criteria.where("description").regex(search, "i");
            Criteria priceCriteria = Criteria.where("price").regex(search, "i");

            ObjectId id = null;
            try {
                id = new ObjectId(search);
            } catch (IllegalArgumentException ignored) {
            }

            if (id == null) {
                query.addCriteria(new Criteria().orOperator(nameCriteria, descriptionCriteria, priceCriteria));
            } else {
                Criteria idCriteria = Criteria.where("id").is(id);
                query.addCriteria(new Criteria().orOperator(nameCriteria, descriptionCriteria, priceCriteria, idCriteria));
            }
        }

        long totalCount = mongoTemplate.count(query, Examination.class);
        query.with(pageable);

        List<Examination> examinations = mongoTemplate.find(query, Examination.class);
        return PageableExecutionUtils.getPage(examinations, pageable, () -> totalCount);
    }

    @Override
    public List<Examination> findAllByDoctorsId(final ObjectId doctorId) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("doctors.id").is(doctorId));

        return mongoTemplate.find(query, Examination.class);
    }
}
