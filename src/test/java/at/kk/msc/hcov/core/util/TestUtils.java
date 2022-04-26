package at.kk.msc.hcov.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;

public class TestUtils {

  public static OntModel getMovieModel() {
    OntModel movieModel = ModelFactory.createOntologyModel();
    RDFDataMgr.read(movieModel, "src/test/resources/movie.ttl");
    return movieModel;
  }

  public static Function<OntModel, List<OntModel>> getSampleMovieModelExtrator() {
    return ontModel -> {
      // Sample data extractor finding all subclasses of persons and their declared properties
      List<OntModel> returnModels = new ArrayList<>();

      Resource resource = ontModel.getOntClass("http://xmlns.com/foaf/0.1/Person");
      List<OntClass> personSubclasses = ontModel.listClasses().filterKeep(
          ontClass -> ontClass.hasSuperClass(resource)
      ).toList();

      personSubclasses.forEach(personSubclass -> {
            OntModel elementsToBeVerified = ModelFactory.createOntologyModel();
            OntClass classCopy = elementsToBeVerified.createClass(personSubclass.getURI());
            personSubclass.listDeclaredProperties().filterKeep(OntProperty::isObjectProperty).forEach(
                objectProperty -> {
                  ObjectProperty objectPropertyCopy = elementsToBeVerified.createObjectProperty(objectProperty.getURI());
                  objectPropertyCopy.addDomain(classCopy);
                  objectPropertyCopy.addRange(objectProperty.getRange());
                }
            );
            returnModels.add(elementsToBeVerified);
          }
      );
      return returnModels;
    };
  }

  public static List<OntModel> getSampleMovieModelExtractedElements() {
    return getSampleMovieModelExtrator().apply(getMovieModel());
  }

  public static OntModel getPizzaModel() {
    OntModel pizzaModel = ModelFactory.createOntologyModel();
    RDFDataMgr.read(pizzaModel, "src/test/resources/pizza.owl.xml");
    return pizzaModel;
  }

  public static List<OntModel> getPizzaSubModels() {
    OntModel pizzaModel = getPizzaModel();

    OntClass namedPizzaClass = pizzaModel.getOntClass("http://www.co-ode.org/ontologies/pizza/pizza.owl#NamedPizza");
    List<OntModel> pizzaClassesAsModels =
        pizzaModel
            .listClasses()
            .filterKeep(ontClass -> ontClass.hasSuperClass(namedPizzaClass))
            .mapWith(ontClass -> {
              // extract simple hierarchy pairs in the form of XYZ_PIZZA -IS_A-> NAMED_PIZZA
              // creates 23 pairs
              OntModel ontModel = ModelFactory.createOntologyModel();
              OntClass namedPizzaClassCopy = ontModel.createClass(namedPizzaClass.getURI());
              namedPizzaClassCopy.addSubClass(ontModel.createClass(ontClass.getURI()));
              return ontModel;
            })
            .toList();

    return pizzaClassesAsModels;
  }

}
