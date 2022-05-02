package at.kk.msc.hcov.core.service.crowdsourcing.exception;

/**
 * Exception to be thrown if an error occursing during processing by the crowdsourcing manager.
 */
public class CrowdsourcingManagerException extends Exception {

  public CrowdsourcingManagerException(String message, Throwable cause) {
    super(message, cause);
  }
}
