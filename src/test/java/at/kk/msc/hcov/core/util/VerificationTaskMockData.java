package at.kk.msc.hcov.core.util;

import static at.kk.msc.hcov.core.util.VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION;

import at.kk.msc.hcov.core.endpoint.dto.VerificationTaskDto;
import at.kk.msc.hcov.core.service.templating.model.ResolvedVariablesWrapper;
import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

public class VerificationTaskMockData {


  /* MOCK DATA WHICH THE PLUGIN MOCK CAN BE USED FOR! */
  public static final UUID FIRST_MOCK_UUID = UUID.fromString("903e7448-591b-4b70-948a-9b7d6cc0de8e");
  public static final UUID SECOND_MOCK_UUID = UUID.fromString("a8c192f6-5971-4812-bc2a-d0d4aae5d19e");
  public static final OntModel FIRST_MOCK_EXTRACTED_ELEMENTS = ModelFactory.createOntologyModel();
  public static final OntModel SECOND_MOCK_EXTRACTED_ELEMENTS = ModelFactory.createOntologyModel();


  public static final Map<UUID, OntModel> MOCKED_EXTRACTED_MODEL_ELEMENTS() {
    Map<UUID, OntModel> extractedElements = new HashMap<>();
    extractedElements.put(FIRST_MOCK_UUID, FIRST_MOCK_EXTRACTED_ELEMENTS);
    extractedElements.put(SECOND_MOCK_UUID, SECOND_MOCK_EXTRACTED_ELEMENTS);
    return extractedElements;
  }

  public static final Map<UUID, ProvidedContext> MOCKED_PROVIDED_CONTEXTS() {
    Map<UUID, ProvidedContext> providedContextMap = new HashMap<>();
    providedContextMap.put(FIRST_MOCK_UUID, new ProvidedContext(FIRST_MOCK_UUID, "FIRST-CONTEXT"));
    providedContextMap.put(SECOND_MOCK_UUID, new ProvidedContext(SECOND_MOCK_UUID, "SECOND-CONTEXT"));
    return providedContextMap;
  }

  /* EXPECTED TEMPLATES FOR MOCK DATA */
  public static final Map<UUID, String> EXPECTED_TEMPLATES_WITH_CONTEXT() {
    Map<UUID, String> exepctedTemplatesWithContextMap = new HashMap<>();
    exepctedTemplatesWithContextMap.put(FIRST_MOCK_UUID, EXPECTED_FIRST_TEMPLATE_WITH_CONTEXT);
    exepctedTemplatesWithContextMap.put(SECOND_MOCK_UUID, EXPECTED_SECOND_TEMPLATE_WITH_CONTEXT);
    return exepctedTemplatesWithContextMap;
  }

  public static final Map<UUID, String> EXPECTED_TEMPLATES_WITHOUT_CONTEXT() {
    Map<UUID, String> expectedTemplatesWithoutContextMap = new HashMap<>();
    expectedTemplatesWithoutContextMap.put(FIRST_MOCK_UUID, EXPECTED_FIRST_TEMPLATE_WITHOUT_CONTEXT);
    expectedTemplatesWithoutContextMap.put(SECOND_MOCK_UUID, EXPECTED_SECOND_TEMPLATE_WITHOUT_CONTEXT);
    return expectedTemplatesWithoutContextMap;
  }

  private static final String EXPECTED_FIRST_TEMPLATE_WITH_CONTEXT =
      "Ontology with name <span>ONTOLOGY-TEST-NAME</span> from <span>FIRST-CONTEXT</span> requires verification for <span>FIRST-ELEMENT</span>  is verified";

  private static final String
      EXPECTED_SECOND_TEMPLATE_WITH_CONTEXT =
      "Ontology with name <span>ONTOLOGY-TEST-NAME</span> from <span>SECOND-CONTEXT</span> requires verification for <span>SECOND-ELEMENT</span>  is verified";

  private static final String EXPECTED_FIRST_TEMPLATE_WITHOUT_CONTEXT =
      "Ontology with name <span>ONTOLOGY-TEST-NAME</span> requires verification for <span>FIRST-ELEMENT</span>  is verified";

  private static final String EXPECTED_SECOND_TEMPLATE_WITHOUT_CONTEXT =
      "Ontology with name <span>ONTOLOGY-TEST-NAME</span> requires verification for <span>SECOND-ELEMENT</span>  is verified";

  private static final VerificationTask.VerificationTaskBuilder VERIFICATION_TASK_BUILDER = VerificationTask.builder()
      .verificationName(MOCKED_VERIFICATION_TASK_SPECIFICATION().getVerificationName())
      .ontologyName(MOCKED_VERIFICATION_TASK_SPECIFICATION().getOntologyName());

  private static final VerificationTaskDto.VerificationTaskDtoBuilder VERIFICATION_TASK_DTO_BUILDER = VerificationTaskDto.builder()
      .verificationName(MOCKED_VERIFICATION_TASK_SPECIFICATION().getVerificationName())
      .ontologyName(MOCKED_VERIFICATION_TASK_SPECIFICATION().getOntologyName());


  public static final List<VerificationTask> EXPECTED_TASKS_WITH_CONTEXT() {
    List<VerificationTask> expectedTasksWithContext = new ArrayList<>();
    expectedTasksWithContext.add(
        VERIFICATION_TASK_BUILDER.ontologyElementId(FIRST_MOCK_UUID).taskHtml(EXPECTED_FIRST_TEMPLATE_WITH_CONTEXT).build()
    );
    expectedTasksWithContext.add(
        VERIFICATION_TASK_BUILDER.ontologyElementId(SECOND_MOCK_UUID).taskHtml(EXPECTED_SECOND_TEMPLATE_WITH_CONTEXT).build()
    );

    return expectedTasksWithContext;
  }

  public static final List<VerificationTask> EXPECTED_TASKS_WITHOUT_CONTEXT() {
    List<VerificationTask> expectedTasksWithoutContext = new ArrayList<>();
    expectedTasksWithoutContext.add(
        VERIFICATION_TASK_BUILDER.ontologyElementId(FIRST_MOCK_UUID).taskHtml(EXPECTED_FIRST_TEMPLATE_WITHOUT_CONTEXT).build()
    );
    expectedTasksWithoutContext.add(
        VERIFICATION_TASK_BUILDER.ontologyElementId(SECOND_MOCK_UUID).taskHtml(EXPECTED_SECOND_TEMPLATE_WITHOUT_CONTEXT).build()
    );
    return expectedTasksWithoutContext;
  }

  public static List<VerificationTaskDto> EXPECTED_TASKS_WITHOUT_CONTEXT_DTOS() {
    List<VerificationTaskDto> expectedTaskDtosWithoutContext = new ArrayList<>();
    expectedTaskDtosWithoutContext.add(
        VERIFICATION_TASK_DTO_BUILDER.ontologyElementId(FIRST_MOCK_UUID).taskHtml(EXPECTED_FIRST_TEMPLATE_WITHOUT_CONTEXT).build()
    );
    expectedTaskDtosWithoutContext.add(
        VERIFICATION_TASK_DTO_BUILDER.ontologyElementId(SECOND_MOCK_UUID).taskHtml(EXPECTED_SECOND_TEMPLATE_WITHOUT_CONTEXT).build()
    );
    return expectedTaskDtosWithoutContext;
  }

  public static List<ResolvedVariablesWrapper> MOCKED_RESOLVED_VARIABLE_WRAPPERS_WITHOUT_CONTEXT() {
    List<ResolvedVariablesWrapper> givenVariables = new ArrayList<>();
    givenVariables.add(
        new ResolvedVariablesWrapper(FIRST_MOCK_UUID,
            Map.of(
                "ontologyName", "ONTOLOGY-TEST-NAME",
                "element", "FIRST-ELEMENT"
            )
        )
    );
    givenVariables.add(
        new ResolvedVariablesWrapper(SECOND_MOCK_UUID,
            Map.of(
                "ontologyName", "ONTOLOGY-TEST-NAME",
                "element", "SECOND-ELEMENT"
            )
        )
    );
    return givenVariables;
  }

}
