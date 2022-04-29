package at.kk.msc.hcov.core.util;

import static at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData.MOCKED_ONTOLOGY_NAME;
import static at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_NAME;

import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTask;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QualityControlTaskMockData {

  public static final UUID FIRST_QC_MOCK_UUID = UUID.fromString("6689ad5c-e543-4eb9-9517-398e23dfccfd");
  public static final UUID SECOND_QC_MOCK_UUID = UUID.fromString("25e93d16-50e6-4a93-82b5-9998b664b3c8");

  private static final String EXPECTED_FIRST_QC_TEMPLATE_WITHOUT_CONTEXT =
      "Ontology with name <span>ONTOLOGY-TEST-NAME</span> requires verification for <span>FIRST-QC-ELEMENT</span>  is verified";

  private static final String EXPECTED_SECOND_QC_TEMPLATE_WITHOUT_CONTEXT =
      "Ontology with name <span>ONTOLOGY-TEST-NAME</span> requires verification for <span>SECOND-QC-ELEMENT</span>  is verified";

  public static List<QualityControlTask> EXPECTED_QC_TASKS_WITHOUT_CONTEXT() {
    List<QualityControlTask> expectedQualityControlTasks = new ArrayList<>();
    expectedQualityControlTasks.add(
        new QualityControlTask(
            MOCKED_VERIFICATION_NAME,
            MOCKED_ONTOLOGY_NAME,
            FIRST_QC_MOCK_UUID,
            EXPECTED_FIRST_QC_TEMPLATE_WITHOUT_CONTEXT,
            "FIRST-ANSWER")
    );
    expectedQualityControlTasks.add(
        new QualityControlTask(
            MOCKED_VERIFICATION_NAME,
            MOCKED_ONTOLOGY_NAME,
            SECOND_QC_MOCK_UUID,
            EXPECTED_SECOND_QC_TEMPLATE_WITHOUT_CONTEXT,
            "SECOND-ANSWER")
    );
    return expectedQualityControlTasks;
  }

  public static List<VerificationTask> EXPECTED_QC_TASKS_AS_VERIFICATION_WITHOUT_CONTEXT() {
    List<VerificationTask> expectedQualityControlTasks = new ArrayList<>();
    expectedQualityControlTasks.add(
        new QualityControlTask(
            MOCKED_VERIFICATION_NAME,
            MOCKED_ONTOLOGY_NAME,
            FIRST_QC_MOCK_UUID,
            EXPECTED_FIRST_QC_TEMPLATE_WITHOUT_CONTEXT,
            "FIRST-ANSWER")
    );
    expectedQualityControlTasks.add(
        new QualityControlTask(
            MOCKED_VERIFICATION_NAME,
            MOCKED_ONTOLOGY_NAME,
            SECOND_QC_MOCK_UUID,
            EXPECTED_SECOND_QC_TEMPLATE_WITHOUT_CONTEXT,
            "SECOND-ANSWER")
    );
    return expectedQualityControlTasks;
  }

}
