package at.kk.msc.hcov.core.service.processing;

import at.kk.msc.hcov.core.persistence.metadata.exception.VerificationDoesNotExistException;
import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.processing.exception.DataProcessorException;
import at.kk.msc.hcov.core.service.processing.model.DataProcessorResults;
import java.util.List;

/**
 * Processes the data obtained from the crowdsourcing platform.
 */
public interface IDataProcessor {

  /**
   * Loads the results for a given verificiation from the {@link at.kk.msc.hcov.core.service.crowdsourcing.impl.CrowdsourcingManager} and
   * executes the processor plugins specified for the verificaiton.
   *
   * @param verificationName name of the verificaiton to load and to process
   * @return a list of {@link DataProcessorResults} objects containting the results of all plugins
   * @throws PluginLoadingError                if a required processor plugin cannot be loaded.
   * @throws VerificationDoesNotExistException if the given verificationName does not exist.
   * @throws DataProcessorException            if an error occured during data processing.
   */
  List<DataProcessorResults> loadResultsAndProcess(String verificationName)
      throws PluginLoadingError, VerificationDoesNotExistException, DataProcessorException;

}
