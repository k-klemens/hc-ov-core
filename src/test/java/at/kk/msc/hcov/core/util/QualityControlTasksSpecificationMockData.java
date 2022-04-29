package at.kk.msc.hcov.core.util;

import static at.kk.msc.hcov.core.util.QualityControlTaskMockData.FIRST_QC_MOCK_UUID;
import static at.kk.msc.hcov.core.util.QualityControlTaskMockData.SECOND_QC_MOCK_UUID;

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
            Map.of("ontologyName", "ONTOLOGY-TEST-NAME", "element", "FIRST-QC-ELEMENT"),
            "FIRST-ANSWER"
        )
    );
    qualityControlTaskCreations.add(
        new QualityControlTaskCreation(
            SECOND_QC_MOCK_UUID,
            Map.of("ontologyName", "ONTOLOGY-TEST-NAME", "element", "SECOND-QC-ELEMENT"),
            "SECOND-ANSWER"
        )
    );

    return new QualityControlTasksSpecification(qualityControlTaskCreations);
  }

}
