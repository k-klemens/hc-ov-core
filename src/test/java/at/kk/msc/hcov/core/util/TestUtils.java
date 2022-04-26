package at.kk.msc.hcov.core.util;

import java.util.List;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

public class TestUtils {

  public static OntModel getMovieModel() {
    OntModel movieModel = ModelFactory.createOntologyModel();
    RDFDataMgr.read(movieModel, "src/test/resources/movie.ttl");
    return movieModel;
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
