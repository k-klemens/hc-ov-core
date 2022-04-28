package at.kk.msc.hcov.core.util;

import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

public class VerificationTaskPluginMockData {


  /* MOCK DATA WHICH THE PLUGIN MOCK CAN BE USED FOR! */
  public static final UUID FIRST_MOCK_UUID = UUID.fromString("903e7448-591b-4b70-948a-9b7d6cc0de8e");
  public static final UUID SECOND_MOCK_UUID = UUID.fromString("a8c192f6-5971-4812-bc2a-d0d4aae5d19e");
  public static final OntModel FIRST_MOCK_EXTRACTED_ELEMENTS = ModelFactory.createOntologyModel();
  public static final OntModel SECOND_MOCK_EXTRACTED_ELEMENTS = ModelFactory.createOntologyModel();


  public static final Map<UUID, OntModel> MOCKED_EXTRACTED_MODEL_ELEMENTS;
  public static final Map<UUID, ProvidedContext> MOCKED_PROVIDED_CONTEXTS;

  /* EXPECTED TEMPLATES FOR MOCK DATA */
  public static final Map<UUID, String> EXPECTED_WITH_CONTEXT;
  public static final Map<UUID, String> EXPECTED_WITHOUT_CONTEXT;

  static {
    MOCKED_EXTRACTED_MODEL_ELEMENTS = new HashMap<>();
    MOCKED_EXTRACTED_MODEL_ELEMENTS.put(FIRST_MOCK_UUID, FIRST_MOCK_EXTRACTED_ELEMENTS);
    MOCKED_EXTRACTED_MODEL_ELEMENTS.put(SECOND_MOCK_UUID, SECOND_MOCK_EXTRACTED_ELEMENTS);

    MOCKED_PROVIDED_CONTEXTS = new HashMap<>();
    MOCKED_PROVIDED_CONTEXTS.put(FIRST_MOCK_UUID, new ProvidedContext(FIRST_MOCK_UUID, "FIRST-CONTEXT"));
    MOCKED_PROVIDED_CONTEXTS.put(SECOND_MOCK_UUID, new ProvidedContext(SECOND_MOCK_UUID, "SECOND-CONTEXT"));


    EXPECTED_WITH_CONTEXT = new HashMap<>();
    EXPECTED_WITH_CONTEXT.put(FIRST_MOCK_UUID,
        "Ontology with name <span>ONTOLOGY-TEST-NAME</span> from <span>FIRST-CONTEXT</span> requires verification for <span>FIRST-ELEMENT</span>  is verified");
    EXPECTED_WITH_CONTEXT.put(SECOND_MOCK_UUID,
        "Ontology with name <span>ONTOLOGY-TEST-NAME</span> from <span>SECOND-CONTEXT</span> requires verification for <span>SECOND-ELEMENT</span>  is verified");

    EXPECTED_WITHOUT_CONTEXT = new HashMap<>();
    EXPECTED_WITHOUT_CONTEXT.put(FIRST_MOCK_UUID,
        "Ontology with name <span>ONTOLOGY-TEST-NAME</span> requires verification for <span>FIRST-ELEMENT</span>  is verified");
    EXPECTED_WITHOUT_CONTEXT.put(SECOND_MOCK_UUID,
        "Ontology with name <span>ONTOLOGY-TEST-NAME</span> requires verification for <span>SECOND-ELEMENT</span>  is verified");
  }

}
