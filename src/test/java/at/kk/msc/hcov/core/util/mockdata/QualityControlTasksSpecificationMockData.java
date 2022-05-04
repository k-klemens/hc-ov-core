package at.kk.msc.hcov.core.util.mockdata;

import static at.kk.msc.hcov.core.util.mockdata.QualityControlTaskMockData.FIRST_QC_MOCK_UUID;
import static at.kk.msc.hcov.core.util.mockdata.QualityControlTaskMockData.SECOND_QC_MOCK_UUID;

import at.kk.msc.hcov.core.endpoint.dto.QualityControlTaskCreationDto;
import at.kk.msc.hcov.core.endpoint.dto.QualityControlTasksSpecificationDto;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTaskCreation;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTasksSpecification;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QualityControlTasksSpecificationMockData {

  public static QualityControlTasksSpecification MOCKED_QUALITY_CONTROL_TASK_SPECIFICATION() {
    List<QualityControlTaskCreation> qualityControlTaskCreations = new ArrayList<>();
    qualityControlTaskCreations.add(
        new QualityControlTaskCreation(
            FIRST_QC_MOCK_UUID,
            Map.of("ontologyName", "ONTOLOGY-TEST-NAME", "element", "FIRST-QC-ELEMENT", "context", "FIRST-QC-CONTEXT"),
            "FIRST-ANSWER"
        )
    );
    qualityControlTaskCreations.add(
        new QualityControlTaskCreation(
            SECOND_QC_MOCK_UUID,
            Map.of("ontologyName", "ONTOLOGY-TEST-NAME", "element", "SECOND-QC-ELEMENT", "context", "SECOND-QC-CONTEXT"),
            "SECOND-ANSWER"
        )
    );

    return new QualityControlTasksSpecification(qualityControlTaskCreations);
  }

  public static QualityControlTasksSpecificationDto MOCKED_QUALITY_CONTROL_TASK_SPECIFICATION_DTO() {
    List<QualityControlTaskCreationDto> qualityControlTaskCreations = new ArrayList<>();
    qualityControlTaskCreations.add(
        new QualityControlTaskCreationDto(
            FIRST_QC_MOCK_UUID,
            Map.of("ontologyName", "ONTOLOGY-TEST-NAME", "element", "FIRST-QC-ELEMENT", "context", "FIRST-QC-CONTEXT"),
            "FIRST-ANSWER"
        )
    );
    qualityControlTaskCreations.add(
        new QualityControlTaskCreationDto(
            SECOND_QC_MOCK_UUID,
            Map.of("ontologyName", "ONTOLOGY-TEST-NAME", "element", "SECOND-QC-ELEMENT", "context", "SECOND-QC-CONTEXT"),
            "SECOND-ANSWER"
        )
    );

    return new QualityControlTasksSpecificationDto(qualityControlTaskCreations);
  }

}
